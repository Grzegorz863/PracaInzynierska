package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.dbEntities.RatingsEntity;
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.NoRatingToUpdateException;
import pl.tcps.exceptions.StationRatedAlreadyByThisUserException;
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
        return _countAverageRatingForPetrolStation(petrolStationEntity.getStationId());
    }

    @Override
    public Double countAverageRatingForPetrolStation(Long stationId) {
        return _countAverageRatingForPetrolStation(stationId);
    }


    @Override
    public Double getStationRatingForOneUser(Long userId, Long stationId) throws EntityNotFoundException {

        if (ratingRepository.existsByUserIdAndStationId(userId, stationId)) {
            RatingsEntity ratingsEntity = ratingRepository.findByUserIdAndStationId(userId, stationId);
            return ratingsEntity.getRate();
        } else
            throw new EntityNotFoundException("This user did not rate that petrol station");

    }

    @Override
    public RatingsEntity createStationRating(Long userId, Long stationId, Double rate) throws StationRatedAlreadyByThisUserException {

        if (ratingRepository.existsByUserIdAndStationId(userId, stationId))
            throw new StationRatedAlreadyByThisUserException("This user already added rating to that station");

        RatingsEntity ratingsEntity = new RatingsEntity(userId, stationId, rate);
        ratingsEntity = ratingRepository.save(ratingsEntity);

        return ratingsEntity;
    }

    @Override
    public void updateStationRating(Long userId, Long stationId, Double newRate) throws NoRatingToUpdateException {

        if (!ratingRepository.existsByUserIdAndStationId(userId, stationId))
            throw new NoRatingToUpdateException("User did not rate this station");

        RatingsEntity ratingsEntity = ratingRepository.findByUserIdAndStationId(userId, stationId);
        ratingRepository.updateStationRating(ratingsEntity.getRatingId(), newRate);
    }

    private Double _countAverageRatingForPetrolStation(Long stationId) {
        Collection<RatingsEntity> ratingsEntities = ratingRepository.findByStationId(stationId);
        if (ratingsEntities == null)
            return 0d;

        OptionalDouble optionalAverage = ratingsEntities.stream().mapToDouble(RatingsEntity::getRate).average();

        return optionalAverage.isPresent() ? optionalAverage.getAsDouble() : 0d;
    }
}
