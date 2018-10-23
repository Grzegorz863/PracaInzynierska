package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.HistoricPricesEntity;

import java.sql.Timestamp;
import java.util.List;

public interface HistoricPricesRepository extends JpaRepository<HistoricPricesEntity, Long> {

    List<HistoricPricesEntity> findByStationIdAndInsertDateAfter(Long stationId, Timestamp insertDate);
}
