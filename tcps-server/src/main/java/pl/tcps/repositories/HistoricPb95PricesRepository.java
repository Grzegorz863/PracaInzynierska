package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.HistoricPb95PricesEntity;
import pl.tcps.dbEntities.HistoricPricesEntity;

import java.sql.Timestamp;
import java.util.List;

public interface HistoricPb95PricesRepository extends JpaRepository<HistoricPb95PricesEntity, Long> {

    List<HistoricPb95PricesEntity> findByStationIdAndInsertDateAfter(Long stationId, Timestamp insertDate);
    HistoricPb95PricesEntity findFirstByStationIdOrderByInsertDateDesc(Long stationId);
}
