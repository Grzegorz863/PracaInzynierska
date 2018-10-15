package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.PetrolStationEntity;

import java.util.List;

public interface PetrolStationRepository extends JpaRepository<PetrolStationEntity, Long> {

    PetrolStationEntity findByStationId(Long stationId);

    Boolean existsByStationName(String stationName);

    Boolean existsByCityAndStreetAndApartmentNumber(String city, String street, Integer apartmentNumber);

}
