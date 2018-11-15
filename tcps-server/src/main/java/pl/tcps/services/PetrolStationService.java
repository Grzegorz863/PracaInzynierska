package pl.tcps.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.dbEntities.RatingsEntity;
import pl.tcps.exceptions.*;
import pl.tcps.pojo.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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
    Double getStationAverageRating(Long stationId) throws EntityNotFoundException;

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

    @PreAuthorize("hasAuthority('android_user')")
    GeoLocationResponse getStationGeoLocation(Long stationId) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    Collection<PetrolStationMapMarker> findPetrolStationByDistanceForMap(Double latitude, Double longitude, Double distance)
            throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    Collection<PetrolStationReloadRecycleViewResponse> reloadSpecificPetrolStations(Double latitude, Double longitude,
                                                                                    List<Long> stationsId) throws EntityNotFoundException;
}
