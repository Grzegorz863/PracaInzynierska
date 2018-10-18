package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolStationEntity;

@Service
public interface RatingService {

    @PreAuthorize("hasAuthority('android_user')")
    Double countAverageRatingForPetrolStation(PetrolStationEntity petrolStationEntity);
}
