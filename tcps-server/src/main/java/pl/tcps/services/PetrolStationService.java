package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.PetrolStationAlreadyExistsException;
import pl.tcps.exceptions.WrongPetrolStationAddressException;
import pl.tcps.pojo.CreatePetrolStationParameter;

import java.io.IOException;
import java.util.Collection;

@Service
public interface PetrolStationService {

    @PreAuthorize("hasAuthority('android_user')")
    PetrolStationEntity findPetrolStation(Long stationId) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    PetrolStationEntity createPetrolStation(CreatePetrolStationParameter createPetrolStationParameter)
            throws PetrolStationAlreadyExistsException, WrongPetrolStationAddressException, IOException;

    @PreAuthorize("hasAuthority('android_user')")
    Collection<PetrolStationEntity> findPetrolStationByDistance(Double latitude, Double longitude, Double distance)
            throws EntityNotFoundException;

}
