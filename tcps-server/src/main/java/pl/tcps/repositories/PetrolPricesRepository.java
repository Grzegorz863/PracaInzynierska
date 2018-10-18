package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.PetrolPricesEntity;

import java.util.Optional;

public interface PetrolPricesRepository extends JpaRepository<PetrolPricesEntity, Long> {

    Optional<PetrolPricesEntity> findByStationId(Long stationId);
}
