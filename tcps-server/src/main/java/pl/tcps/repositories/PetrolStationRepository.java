package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.PetrolStationsEntity;

public interface PetrolStationRepository extends JpaRepository<PetrolStationsEntity, Long> {
}
