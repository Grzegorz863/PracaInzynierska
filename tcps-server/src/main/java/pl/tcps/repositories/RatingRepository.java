package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.RatingsEntity;

import java.util.List;

public interface RatingRepository extends JpaRepository<RatingsEntity, Long> {

    List<RatingsEntity> findByStationId(Long stationId);
}
