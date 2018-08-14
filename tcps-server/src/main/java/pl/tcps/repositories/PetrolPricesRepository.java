package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.PetrolPricesEntity;

public interface PetrolPricesRepository extends JpaRepository<PetrolPricesEntity, Long> {
}
