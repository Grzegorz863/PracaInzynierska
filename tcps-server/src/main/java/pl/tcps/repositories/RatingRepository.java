package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.RatingsEntity;

public interface RatingRepository extends JpaRepository<RatingsEntity, Long> {
}
