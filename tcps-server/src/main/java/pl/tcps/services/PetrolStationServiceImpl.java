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
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.PetrolStationAlreadyExistsException;
import pl.tcps.exceptions.WrongPetrolStationAddressException;
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
        PetrolPricesResponse currentPetrolPrices = petrolPricesService.getPetrolPricesByStationId(petrolStationEntity);
        PetrolPricesResponse historicPetrolPrices = historicPricesService.countTwoWeeksHistoricPricesAverage(stationId,
                ZonedDateTime.now(ZoneId.of("Europe/Warsaw")));
        return new PetrolStationSpecificInfoResponse(petrolStationEntity, consortiumName, rating,
                currentPetrolPrices, historicPetrolPrices);
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
        PetrolPricesResponse petrolPrices = petrolPricesService.getPetrolPricesByStationId(petrolStationEntity);
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

            petrolStationEntity = petrolStationRepository.save(petrolStationEntity);
        }
        else
            throw new PetrolStationAlreadyExistsException("Can not create petrol station with the same name or address");

        return petrolStationEntity;
    }

    private Double countDistanceBetweenTwoPoints(Double latitudePoint1, Double longitudePoint1,
                                                 Double latitudePoint2, Double longitudePoint2){

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