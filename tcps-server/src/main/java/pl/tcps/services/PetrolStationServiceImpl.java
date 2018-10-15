package pl.tcps.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.PetrolStationAlreadyExistsException;
import pl.tcps.exceptions.WrongPetrolStationAddressException;
import pl.tcps.pojo.CreatePetrolStationParameter;
import pl.tcps.pojo.PetrolStationLocationCoordinates;
import pl.tcps.repositories.ConsortiumRepository;
import pl.tcps.repositories.PetrolStationRepository;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
public class PetrolStationServiceImpl implements PetrolStationService {

    private PetrolStationRepository petrolStationRepository;
    private ConsortiumRepository consortiumRepository;

    @Autowired
    public PetrolStationServiceImpl(PetrolStationRepository petrolStationRepository, ConsortiumRepository consortiumRepository) {
        this.petrolStationRepository = petrolStationRepository;
        this.consortiumRepository = consortiumRepository;
    }

    @Override
    public PetrolStationEntity findPetrolStation(Long stationId) throws EntityNotFoundException {

        PetrolStationEntity petrolStationEntity = petrolStationRepository.findByStationId(stationId);
        if(petrolStationEntity == null)
            throw new EntityNotFoundException("petrol station not found");
        return petrolStationRepository.findByStationId(stationId);
    }

    @Override
    public Collection<PetrolStationEntity> findPetrolStationByDistance(Double currentLatitude, Double currentLongitude, Double distanceInKM)
            throws EntityNotFoundException {

        Double actualDistance;
        Double distanceInMeters = distanceInKM * 1000;
        List<PetrolStationEntity> petrolStationsLocatedInDistance = new ArrayList<>();
        Collection<PetrolStationEntity> petrolStationEntities = petrolStationRepository.findAll();

        for (PetrolStationEntity petrolStationEntity : petrolStationEntities){
            actualDistance = countDistanceBetweenTwoPoints(currentLatitude, currentLongitude,
                    petrolStationEntity.getLatitude(), petrolStationEntity.getLongitude());
            if(actualDistance<=distanceInMeters)
                petrolStationsLocatedInDistance.add(petrolStationEntity);
        }

        if(petrolStationsLocatedInDistance.isEmpty())
            throw new EntityNotFoundException("petrol station in chosen distance not found");

        return petrolStationsLocatedInDistance;
    }

    @Override
    public PetrolStationEntity createPetrolStation(CreatePetrolStationParameter stationData)
            throws PetrolStationAlreadyExistsException, WrongPetrolStationAddressException, IOException {

        PetrolStationEntity petrolStationEntity = new PetrolStationEntity();

        ConsortiumsEntity consortiumsEntity = consortiumRepository.findByConsortiumName(stationData.getConsortiumName());

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

            petrolStationEntity.setStationName(stationData.getStationName());
            petrolStationEntity.setCity(stationData.getCity());
            petrolStationEntity.setStreet(stationData.getStreet());
            petrolStationEntity.setApartmentNumber(stationData.getApartmentNumber());
            petrolStationEntity.setPostalCode(stationData.getPostalCode());
            petrolStationEntity.setDescription(stationData.getDescription());
            petrolStationEntity.setHasFood(stationData.getHasFood());
            petrolStationEntity.setLatitude(coordinates.getLatitude());
            petrolStationEntity.setLongitude(coordinates.getLongitude());
            petrolStationEntity.setConsortiumId(consortiumsEntity.getConsortiumId());
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