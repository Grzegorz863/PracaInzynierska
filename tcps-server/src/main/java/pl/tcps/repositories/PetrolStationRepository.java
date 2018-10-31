package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.tcps.dbEntities.PetrolStationEntity;

import java.util.List;

public interface PetrolStationRepository extends JpaRepository<PetrolStationEntity, Long> {

    PetrolStationEntity findByStationId(Long stationId);

    Boolean existsByStationId(Long stationId);

    Boolean existsByStationName(String stationName);

    Boolean existsByCityAndStreetAndApartmentNumber(String city, String street, String apartmentNumber);

}
