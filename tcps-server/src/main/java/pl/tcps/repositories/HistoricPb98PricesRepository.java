package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.HistoricPb95PricesEntity;
import pl.tcps.dbEntities.HistoricPb98PricesEntity;
import pl.tcps.dbEntities.HistoricPricesEntity;

import java.sql.Timestamp;
import java.util.List;

public interface HistoricPb98PricesRepository extends JpaRepository<HistoricPb98PricesEntity, Long> {

    List<HistoricPb98PricesEntity> findByStationIdAndInsertDateAfter(Long stationId, Timestamp insertDate);

    HistoricPb98PricesEntity findFirstByStationIdOrderByInsertDateDesc(Long stationId);
}
