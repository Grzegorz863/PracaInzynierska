package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.dbEntities.RatingsEntity;
import pl.tcps.repositories.RatingRepository;

import java.util.Collection;
import java.util.OptionalDouble;

@Service
public class RatingServiceImpl implements RatingService {

    private RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Double countAverageRatingForPetrolStation(PetrolStationEntity petrolStationEntity) {

        Collection<RatingsEntity> ratingsEntities = ratingRepository.findByStationId(petrolStationEntity.getStationId());
        if(ratingsEntities==null)
            return 0d;

        OptionalDouble optionalAverage = ratingsEntities.stream().mapToDouble(RatingsEntity::getRate).average();

        return optionalAverage.isPresent() ? optionalAverage.getAsDouble() : 0d;
    }
}
