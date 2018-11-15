package pl.tcps.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.dbEntities.RatingsEntity;
import pl.tcps.exceptions.*;
import pl.tcps.pojo.*;
import pl.tcps.repositories.PetrolStationRepository;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class PetrolStationServiceImpl implements PetrolStationService {

    private PetrolStationRepository petrolStationRepository;
    private ConsortiumService consortiumService;
    private PetrolPricesService petrolPricesService;
    private RatingService ratingService;
    private HistoricPricesService historicPricesService;

    @Autowired
    public PetrolStationServiceImpl(PetrolStationRepository petrolStationRepository, ConsortiumService consortiumService,
                                    PetrolPricesService petrolPricesService, RatingService ratingService,
                                    HistoricPricesService historicPricesService) {
        this.petrolStationRepository = petrolStationRepository;
        this.consortiumService = consortiumService;
        this.petrolPricesService = petrolPricesService;
        this.ratingService = ratingService;
        this.historicPricesService = historicPricesService;
    }

    @Override
    public PetrolStationSpecificInfoResponse getPetrolStationSpecificInfo(final Long stationId) throws EntityNotFoundException {

        PetrolStationEntity petrolStationEntity = petrolStationRepository.findByStationId(stationId);
        if(petrolStationEntity == null)
            throw new EntityNotFoundException("petrol station not found");

        String consortiumName = consortiumService.getConsortiumName(petrolStationEntity.getConsortiumId());
        Double rating = ratingService.countAverageRatingForPetrolStation(petrolStationEntity);
        PetrolPricesAndDateResponse currentPetrolPrices = petrolPricesService.getPetrolPricesWithInsertDate(petrolStationEntity);
        PetrolPricesResponse historicPetrolPrices = historicPricesService.countTwoWeeksHistoricPricesAverage(stationId,
                ZonedDateTime.now(ZoneId.of("Europe/Warsaw")));
        return new PetrolStationSpecificInfoResponse(petrolStationEntity, consortiumName, rating,
                currentPetrolPrices, historicPetrolPrices);
    }

    @Override
    public Double getStationRatingForOneUser(Long userId, Long stationId) throws EntityNotFoundException {
        return ratingService.getStationRatingForOneUser(userId, stationId);
    }

    @Override
    public Double getStationAverageRating(Long stationId) throws EntityNotFoundException {
        if(!petrolStationRepository.existsByStationId(stationId))
            throw new EntityNotFoundException("petrol station not found");

        return ratingService.countAverageRatingForPetrolStation(stationId);
    }

    @Override
    public RatingsEntity createStationRating(Long userId, Long stationId, Double rate) throws StationRatedAlreadyByThisUserException {
        return ratingService.createStationRating(userId, stationId, rate);
    }

    @Override
    public void updateStationRating(Long userId, Long stationId, Double newRate) throws NoRatingToUpdateException{
        ratingService.updateStationRating(userId, stationId, newRate);
    }

    @Override
    public PetrolPricesResponse getPetrolPricesForStation(Long stationId) throws EntityNotFoundException{
        if(!petrolStationRepository.existsByStationId(stationId))
            throw new EntityNotFoundException("Station with given ID does not exist!");

        return petrolPricesService.getPetrolPricesByStationId(stationId);
    }

    @Override
    public PetrolPricesEntity createPetrolPricesForStation(Long stationId, Long userId, Float pb95Price, Float pb98Price,
                                                           Float onPrice, Float lpgPrice) throws PetrolPricesAlreadyExistsException{

        return petrolPricesService.createPetrolPricesForStation(stationId, userId, pb95Price, pb98Price, onPrice, lpgPrice);
    }

    @Override
    public void updatePetrolPricesForStation(Long stationId, Long userId, Float pb95Price, Float pb98Price,
                                             Float onPrice, Float lpgPrice) throws PetrolPricesNotExistsException{

        petrolPricesService.updatePetrolPricesForStation(stationId, userId, pb95Price, pb98Price, onPrice, lpgPrice);
    }

    @Override
    public Collection<PetrolStationResponseRecycleViewItem> findPetrolStationByDistance(Double currentLatitude, Double currentLongitude, Double distanceInKM)
            throws EntityNotFoundException {

        Double actualDistance;
        Double distanceInMeters = distanceInKM * 1000;
        List<PetrolStationResponseRecycleViewItem> petrolStationsLocatedInDistance = new ArrayList<>();
        Collection<PetrolStationEntity> petrolStationEntities = petrolStationRepository.findAll();

        for (PetrolStationEntity petrolStationEntity : petrolStationEntities){
            actualDistance = countDistanceBetweenTwoPoints(currentLatitude, currentLongitude,
                    petrolStationEntity.getLatitude(), petrolStationEntity.getLongitude());
            if(actualDistance<=distanceInMeters)
                petrolStationsLocatedInDistance.add(preparePetrolStationToDeserialize(petrolStationEntity, actualDistance));
        }

        if(petrolStationsLocatedInDistance.isEmpty())
            throw new EntityNotFoundException("petrol station in chosen distance not found");

        return petrolStationsLocatedInDistance;
    }

    private PetrolStationResponseRecycleViewItem preparePetrolStationToDeserialize(PetrolStationEntity petrolStationEntity,
                                                                                   Double distanceInMeters) {

        String consortiuName = consortiumService.getConsortiumName(petrolStationEntity.getConsortiumId());
        PetrolPricesAndDateResponse petrolPrices = petrolPricesService.getPetrolPricesWithInsertDate(petrolStationEntity);
        Double rating = ratingService.countAverageRatingForPetrolStation(petrolStationEntity);
        return new PetrolStationResponseRecycleViewItem(petrolStationEntity, consortiuName,petrolPrices,
                rating, distanceInMeters);
    }

    @Override
    public PetrolStationEntity createPetrolStation(CreatePetrolStationParameter stationData)
            throws PetrolStationAlreadyExistsException, WrongPetrolStationAddressException, EntityNotFoundException, IOException {

        PetrolStationEntity petrolStationEntity;
        ConsortiumsEntity consortiumsEntity = consortiumService.getConsortium(stationData.getConsortiumName());

        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> responseFromGoogle = rest.getForEntity(
                "https://maps.googleapis.com/maps/api/geocode/json?address={street} {number}, {city}&sensor=false&key=AIzaSyDwKGwZZQdhb3_pzexvUyTX4oZy2MGrypg",
                String.class, stationData.getStreet(), stationData.getApartmentNumber(), stationData.getCity());

        if(responseFromGoogle.getStatusCode() != HttpStatus.OK)
            throw new WrongPetrolStationAddressException("Problem with Google API");

        ObjectMapper mapper = new ObjectMapper();
        PetrolStationLocationCoordinates coordinates = mapper.readValue(responseFromGoogle.getBody(), PetrolStationLocationCoordinates.class);

       if(!petrolStationRepository.existsByStationName(stationData.getStationName())
                && !petrolStationRepository.existsByCityAndStreetAndApartmentNumber(
                        stationData.getCity(), stationData.getStreet(), stationData.getApartmentNumber())){

            petrolStationEntity = new PetrolStationEntity(stationData, coordinates,
                   consortiumsEntity.getConsortiumId());

            petrolStationEntity = petrolStationRepository.saveAndFlush(petrolStationEntity);
        }
        else
            throw new PetrolStationAlreadyExistsException("Can not create petrol station with the same name or address");

        return petrolStationEntity;
    }

    @Override
    public GeoLocationResponse getStationGeoLocation(Long stationId) throws EntityNotFoundException {
        PetrolStationEntity petrolStationEntity = petrolStationRepository.findByStationId(stationId);
        if (petrolStationEntity == null)
            throw new EntityNotFoundException(String.format("Station with id %d does not exist!", stationId));
        return new GeoLocationResponse(petrolStationEntity.getStationName(), petrolStationEntity.getLatitude(),petrolStationEntity.getLongitude());
    }

    @Override
    public Collection<PetrolStationMapMarker> findPetrolStationByDistanceForMap(Double currentLatitude, Double currentLongitude, Double distanceInKM) throws EntityNotFoundException {

        Double actualDistance;
        Double distanceInMeters = distanceInKM * 1000;
        List<PetrolStationMapMarker> petrolStationsForMap = new ArrayList<>();
        Collection<PetrolStationEntity> petrolStationEntities = petrolStationRepository.findAll();

        for (PetrolStationEntity petrolStationEntity : petrolStationEntities){
            actualDistance = countDistanceBetweenTwoPoints(currentLatitude, currentLongitude,
                    petrolStationEntity.getLatitude(), petrolStationEntity.getLongitude());
            if(actualDistance<=distanceInMeters)
                petrolStationsForMap.add(preparePetrolStationToDeserializeForMap(petrolStationEntity, actualDistance));
        }

        if(petrolStationsForMap.isEmpty())
            throw new EntityNotFoundException("petrol station in chosen distance not found");

        return petrolStationsForMap;
    }

    @Override
    public Collection<PetrolStationReloadRecycleViewResponse> reloadSpecificPetrolStations(Double currentLatitude,
                                                                                           Double currentLongitude, List<Long> stationsId)
            throws EntityNotFoundException {

        List<PetrolStationReloadRecycleViewResponse> responseStations = new ArrayList<>();
        for(Long stationId : stationsId){
            PetrolPricesResponse prices = getPetrolPricesForStation(stationId);
            Double rating = getStationAverageRating(stationId);
            PetrolStationEntity petrolStationEntity = petrolStationRepository.findByStationId(stationId);
            if(petrolStationEntity == null)
                throw new EntityNotFoundException(String.format("Station with id %d does not exist!", stationId));

            Double distance = countDistanceBetweenTwoPoints(currentLatitude, currentLongitude,
                    petrolStationEntity.getLatitude(), petrolStationEntity.getLongitude());

            responseStations.add(new PetrolStationReloadRecycleViewResponse(stationId, prices, distance, rating));
        }
        return responseStations;
    }

    private PetrolStationMapMarker preparePetrolStationToDeserializeForMap(PetrolStationEntity petrolStationEntity, Double actualDistance) {
        AddressResponse addressResponse = new AddressResponse(petrolStationEntity.getStreet(),
                petrolStationEntity.getApartmentNumber(), petrolStationEntity.getCity(), petrolStationEntity.getPostalCode());

        return new PetrolStationMapMarker(petrolStationEntity.getStationId(), petrolStationEntity.getStationName(),
                petrolStationEntity.getLatitude(), petrolStationEntity.getLongitude(), actualDistance, addressResponse);
    }

    private Double countDistanceBetweenTwoPoints(Double latitudePoint1, Double longitudePoint1,
                                                 Double latitudePoint2, Double longitudePoint2) {

        final Integer earthRadius = 6371;

        double longitudeDistance = Math.toRadians(longitudePoint1 - longitudePoint2);
        double latitudeDistance = Math.toRadians(latitudePoint1 - latitudePoint2);
        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(latitudePoint1)) * Math.cos(Math.toRadians(latitudePoint2))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        DecimalFormat df = new DecimalFormat("#.##");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));

        return Double.parseDouble(df.format(earthRadius * c * 1000));
    }
}