package pl.tcps.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.dbEntities.RatingsEntity;
import pl.tcps.exceptions.*;
import pl.tcps.pojo.*;
import pl.tcps.services.PetrolStationService;
import pl.tcps.services.UserService;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/station")
public class PetrolStationController {

    private final PetrolStationService petrolStationService;
    private UserService userService;

    @Autowired
    public PetrolStationController(PetrolStationService petrolStationService, UserService userService) {
        this.petrolStationService = petrolStationService;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/id/{station_id}", produces = "application/json")
    public ResponseEntity<PetrolStationSpecificInfoResponse> getPetrolStationSpecyficInfo(@PathVariable("station_id") Long stationId) throws EntityNotFoundException {

        PetrolStationSpecificInfoResponse petrolStationSpecificInfo = petrolStationService.getPetrolStationSpecificInfo(stationId);

        return new ResponseEntity<>(petrolStationSpecificInfo, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/distance/{latitude}/{longitude}/{distance}", produces = "application/json")
    public ResponseEntity<Collection<PetrolStationResponseRecycleViewItem>> findPetrolStationByDistance(@PathVariable("latitude") Double latitude,
                                                                                                        @PathVariable("longitude") Double longitude,
                                                                                                        @PathVariable("distance") Double distance)
            throws EntityNotFoundException {

        Collection<PetrolStationResponseRecycleViewItem> petrolStationEntitiesByDistance =
                petrolStationService.findPetrolStationByDistance(latitude, longitude, distance);

        return new ResponseEntity<>(petrolStationEntitiesByDistance, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{stationId}/prices", produces = "application/json")
    public ResponseEntity<PetrolPricesResponse> getPetrolPricesForStation(@PathVariable("stationId") Long stationId) throws EntityNotFoundException{
        return new ResponseEntity<>(petrolStationService.getPetrolPricesForStation(stationId), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/prices", produces = "application/json")
    public ResponseEntity<PetrolPricesEntity> createPetrolPricesForStation(@RequestParam("station_id") Long stationId,
                                                                           @RequestParam(value = "pb95_price", required = false) Float pb95Price,
                                                                           @RequestParam(value = "pb98_price", required = false) Float pb98Price,
                                                                           @RequestParam(value = "on_price", required = false) Float onPrice,
                                                                           @RequestParam(value = "lpg_price", required = false) Float lpgPrice,
                                                                           Authentication authentication, UriComponentsBuilder uriComponentsBuilder)
                                                                            throws PetrolPricesAlreadyExistsException, WrongRequestParametersException {
        if(pb95Price == null && pb98Price == null && onPrice == null && lpgPrice == null)
            throw new WrongRequestParametersException("Not entered not even one petrol price!");

        String userName = authentication.getName();
        Long userId = userService.getUserIdByName(userName);

        PetrolPricesEntity petrolPricesEntity = petrolStationService.createPetrolPricesForStation(stationId, userId, pb95Price, pb98Price, onPrice, lpgPrice);

        HttpHeaders httpHeaders = new HttpHeaders();
        URI locationUri = uriComponentsBuilder
                .path("/id")
                .path(String.valueOf(petrolPricesEntity.getStationId()))
                .build()
                .toUri();
        httpHeaders.setLocation(locationUri);

        return new ResponseEntity<>(petrolPricesEntity, httpHeaders, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/prices", produces = "application/json")
    public void updatePetrolPricesForStation(@RequestParam("station_id") Long stationId,
                                             @RequestParam(value = "pb95_price", required = false) Float pb95Price,
                                             @RequestParam(value = "pb98_price", required = false) Float pb98Price,
                                             @RequestParam(value = "on_price", required = false) Float onPrice,
                                             @RequestParam(value = "lpg_price", required = false) Float lpgPrice,
                                             Authentication authentication) throws PetrolPricesNotExistsException, WrongRequestParametersException {

        if(pb95Price == null && pb98Price == null && onPrice == null && lpgPrice == null)
            throw new WrongRequestParametersException("Not entered not even one petrol price!");

        String userName = authentication.getName();
        Long userId = userService.getUserIdByName(userName);

        petrolStationService.updatePetrolPricesForStation(stationId, userId, pb95Price, pb98Price, onPrice, lpgPrice);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{stationId}/rating", produces = "application/json")
    public ResponseEntity<Double> getStationRatingForOneUser(@PathVariable("stationId") Long stationId,
                                                             Authentication authentication) throws EntityNotFoundException {
        String userName = authentication.getName();
        Long userId = userService.getUserIdByName(userName);
        return new ResponseEntity<>(petrolStationService.getStationRatingForOneUser(userId, stationId), HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/rating", produces = "application/json")
    public void updateStationRating(@RequestParam("station_id") Long stationId,
                                                            @RequestParam("new_rate") Double newRate,
                                                            Authentication authentication) throws NoRatingToUpdateException{

        String userName = authentication.getName();
        Long userId = userService.getUserIdByName(userName);

        petrolStationService.updateStationRating(userId, stationId, newRate);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/rating", produces = "application/json")
    public ResponseEntity<RatingsEntity>  createStationRating(@RequestParam("station_id") Long stationId,
                                                              @RequestParam("rate") Double rate, Authentication authentication,
                                                              UriComponentsBuilder uriComponentsBuilder)
            throws StationRatedAlreadyByThisUserException{

        String userName = authentication.getName();
        Long userId = userService.getUserIdByName(userName);

        RatingsEntity ratingsEntity = petrolStationService.createStationRating(userId, stationId, rate);

        HttpHeaders httpHeaders = new HttpHeaders();
        URI locationUri = uriComponentsBuilder
                .path("/id")
                .path(String.valueOf(ratingsEntity.getStationId()))
                .build()
                .toUri();
        httpHeaders.setLocation(locationUri);

        return new ResponseEntity<>(ratingsEntity, httpHeaders, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{stationId}/location", produces = "application/json")
    public ResponseEntity<GeoLocationResponse> getStationGeoLocation(@PathVariable("stationId") Long stationId) throws EntityNotFoundException{
        GeoLocationResponse geoLocation = petrolStationService.getStationGeoLocation(stationId);
        return new ResponseEntity<>(geoLocation, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<PetrolStationEntity> createPetrolStation(@RequestParam("station_name") String stationName,
                                                                   @RequestParam("city") String city,
                                                                   @RequestParam("street") String street,
                                                                   @RequestParam("apartment_number") String apartmentNumber,
                                                                   @RequestParam("postal_code") String postalCode,
                                                                   @RequestParam("description") String description,
                                                                   @RequestParam("has_food") Boolean hasFood,
                                                                   @RequestParam("consortium_name") String consortiumName,
                                                                   UriComponentsBuilder uriComponentsBuilder)
            throws PetrolStationAlreadyExistsException, WrongPetrolStationAddressException, EntityNotFoundException, IOException {

        CreatePetrolStationParameter createPetrolStationParameter = new CreatePetrolStationParameter(stationName,
                city, street, apartmentNumber, postalCode, description, hasFood, consortiumName);
        PetrolStationEntity petrolStationEntity = petrolStationService.createPetrolStation(createPetrolStationParameter);

        HttpHeaders httpHeaders = new HttpHeaders();
        URI locationUri = uriComponentsBuilder
                .path("/id")
                .path(String.valueOf(petrolStationEntity.getStationId()))
                .build()
                .toUri();
        httpHeaders.setLocation(locationUri);

        return new ResponseEntity<>(petrolStationEntity, httpHeaders, HttpStatus.CREATED);
    }
}
