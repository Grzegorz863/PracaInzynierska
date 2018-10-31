package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.HistoricLpgPricesEntity;
import pl.tcps.dbEntities.HistoricPb95PricesEntity;
import pl.tcps.dbEntities.HistoricPricesEntity;

import java.sql.Timestamp;
import java.util.List;

public interface HistoricLpgPricesRepository extends JpaRepository<HistoricLpgPricesEntity, Long> {

    List<HistoricLpgPricesEntity> findByStationIdAndInsertDateAfter(Long stationId, Timestamp insertDate);
}
