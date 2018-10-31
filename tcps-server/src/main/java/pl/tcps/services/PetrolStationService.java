package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.dbEntities.RatingsEntity;
import pl.tcps.exceptions.*;
import pl.tcps.pojo.CreatePetrolStationParameter;
import pl.tcps.pojo.PetrolPricesResponse;
import pl.tcps.pojo.PetrolStationResponseRecycleViewItem;
import pl.tcps.pojo.PetrolStationSpecificInfoResponse;

import java.io.IOException;
import java.util.Collection;

@Service
public interface PetrolStationService {

    @PreAuthorize("hasAuthority('android_user')")
    PetrolStationSpecificInfoResponse getPetrolStationSpecificInfo(Long stationId) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    PetrolStationEntity createPetrolStation(CreatePetrolStationParameter createPetrolStationParameter)
            throws PetrolStationAlreadyExistsException, WrongPetrolStationAddressException,
            EntityNotFoundException, IOException;

    @PreAuthorize("hasAuthority('android_user')")
    Collection<PetrolStationResponseRecycleViewItem> findPetrolStationByDistance(Double latitude, Double longitude, Double distance)
            throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    Double getStationRatingForOneUser(Long userId, Long stationId) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    RatingsEntity createStationRating(Long userId, Long stationId, Double rate) throws StationRatedAlreadyByThisUserException;

    @PreAuthorize("hasAuthority('android_user')")
    void updateStationRating(Long userId, Long stationId, Double newRate) throws NoRatingToUpdateException;

    @PreAuthorize("hasAuthority('android_user')")
    PetrolPricesResponse getPetrolPricesForStation(Long stationId) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    PetrolPricesEntity createPetrolPricesForStation(Long stationId, Long userId, Float pb95Price, Float pb98Price,
                                                    Float onPrice, Float lpgPrice) throws PetrolPricesAlreadyExistsException;

    @PreAuthorize("hasAuthority('android_user')")
    void updatePetrolPricesForStation(Long stationId, Long userId, Float pb95Price, Float pb98Price,
                                                    Float onPrice, Float lpgPrice) throws PetrolPricesNotExistsException;
}
