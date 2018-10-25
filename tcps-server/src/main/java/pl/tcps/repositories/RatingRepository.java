package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.tcps.dbEntities.RatingsEntity;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingsEntity, Long> {

    List<RatingsEntity> findByStationId(Long stationId);

    Boolean existsByUserIdAndStationId(Long userId, Long stationsId);

    RatingsEntity findByUserIdAndStationId(Long userId, Long stationId);

    @Modifying
    @Transactional
    @Query("update RatingsEntity r set r.rate = :new_rate where r.ratingId = :rating_id")
    void updateStationRating(@Param("rating_id") Long ratingId, @Param("new_rate") Double newRate);
}
