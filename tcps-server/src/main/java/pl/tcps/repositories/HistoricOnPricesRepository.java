package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.HistoricOnPricesEntity;
import pl.tcps.dbEntities.HistoricPb95PricesEntity;
import pl.tcps.dbEntities.HistoricPricesEntity;

import java.sql.Timestamp;
import java.util.List;

public interface HistoricOnPricesRepository extends JpaRepository<HistoricOnPricesEntity, Long> {

    List<HistoricOnPricesEntity> findByStationIdAndInsertDateAfter(Long stationId, Timestamp insertDate);
}
