package pl.tcps.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.PetrolStationAlreadyExistsException;
import pl.tcps.exceptions.WrongPetrolStationAddressException;
import pl.tcps.pojo.CreatePetrolStationParameter;
import pl.tcps.pojo.PetrolStationResponseRecycleViewItem;
import pl.tcps.pojo.PetrolStationSpecificInfoResponse;
import pl.tcps.services.PetrolStationService;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/station")
public class PetrolStationController {

    private final PetrolStationService petrolStationService;

    @Autowired
    public PetrolStationController(PetrolStationService petrolStationService) {
        this.petrolStationService = petrolStationService;
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
            throws EntityNotFoundException{

        Collection<PetrolStationResponseRecycleViewItem> petrolStationEntitiesByDistance =
                petrolStationService.findPetrolStationByDistance(latitude, longitude, distance);

        return new ResponseEntity<>(petrolStationEntitiesByDistance, HttpStatus.OK);
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
