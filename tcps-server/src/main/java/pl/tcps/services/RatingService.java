package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.dbEntities.RatingsEntity;
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.NoRatingToUpdateException;
import pl.tcps.exceptions.StationRatedAlreadyByThisUserException;

@Service
public interface RatingService {

    @PreAuthorize("hasAuthority('android_user')")
    Double countAverageRatingForPetrolStation(PetrolStationEntity petrolStationEntity);

    @PreAuthorize("hasAuthority('android_user')")
    Double getStationRatingForOneUser(Long userId, Long stationId) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    RatingsEntity createStationRating(Long userId, Long stationId, Double rate) throws StationRatedAlreadyByThisUserException;

    @PreAuthorize("hasAuthority('android_user')")
    void updateStationRating(Long userId, Long stationId, Double newRate) throws NoRatingToUpdateException;
}
