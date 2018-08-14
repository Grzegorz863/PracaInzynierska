package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.HistoricPricesEntity;

public interface HistoricPricesRepository extends JpaRepository<HistoricPricesEntity, Long> {
}
