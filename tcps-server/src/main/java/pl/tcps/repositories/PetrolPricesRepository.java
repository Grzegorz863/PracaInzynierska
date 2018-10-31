package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.tcps.dbEntities.PetrolPricesEntity;

import java.util.Optional;

public interface PetrolPricesRepository extends JpaRepository<PetrolPricesEntity, Long> {

    Optional<PetrolPricesEntity> findByStationId(Long stationId);

    Boolean existsByStationId(Long stationId);

    @Modifying
    @Transactional
    @Query("update PetrolPricesEntity p set p.pb95Price = :pb95_price where p.stationId = :station_id")
    void updatePb95Price(@Param("station_id") Long stationId, @Param("pb95_price") Float pb95Price);

    @Modifying
    @Transactional
    @Query("update PetrolPricesEntity p set p.pb98Price = :pb98_price where p.stationId = :station_id")
    void updatePb98Price(@Param("station_id") Long stationId, @Param("pb98_price") Float pb98Price);

    @Modifying
    @Transactional
    @Query("update PetrolPricesEntity p set p.onPrice = :on_price where p.stationId = :station_id")
    void updateOnPrice(@Param("station_id") Long stationId, @Param("on_price") Float onPrice);

    @Modifying
    @Transactional
    @Query("update PetrolPricesEntity p set p.lpgPrice = :lpg_price where p.stationId = :station_id")
    void updateLpgPrice(@Param("station_id") Long stationId, @Param("lpg_price") Float lpgPrice);
}
