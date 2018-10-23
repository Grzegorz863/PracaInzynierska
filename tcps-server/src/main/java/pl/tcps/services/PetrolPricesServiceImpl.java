package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.pojo.PetrolPricesResponse;
import pl.tcps.repositories.PetrolPricesRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class PetrolPricesServiceImpl implements PetrolPricesService {

    private PetrolPricesRepository petrolPricesRepository;

    @Autowired
    public PetrolPricesServiceImpl(PetrolPricesRepository petrolPricesRepository) {
        this.petrolPricesRepository = petrolPricesRepository;
    }


    @Override
    public PetrolPricesResponse getPetrolPricesByStationId(PetrolStationEntity petrolStationEntity) {
        Optional<PetrolPricesEntity> petrolPricesEntity = petrolPricesRepository.findByStationId(petrolStationEntity.getStationId());

        if (petrolPricesEntity.isPresent())
            return new PetrolPricesResponse(petrolPricesEntity.get(),
                    petrolPricesEntity.get().getInsertDate().toLocalDateTime().atZone(ZoneId.of("Europe/Warsaw")));
        else
            return new PetrolPricesResponse(0f, 0f, 0f, 0f);
    }

//    @Override
//    public PetrolPricesResponse getPetrolPricesByStationId(PetrolStationEntity petrolStationEntity) {
//        Optional<PetrolPricesEntity> petrolPricesEntity = petrolPricesRepository.findByStationId(petrolStationEntity.getStationId());
//
//        return petrolPricesEntity.orElseGet(() -> petrolPricesEntity.orElse(
//                new PetrolPricesEntity(0, 0,0 ,0f, 0f, 0f,
//                        0f, Timestamp.from(Instant.EPOCH))));
//    }
}
