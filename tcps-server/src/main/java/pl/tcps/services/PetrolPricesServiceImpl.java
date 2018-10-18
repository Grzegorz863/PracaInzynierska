package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.repositories.PetrolPricesRepository;

import java.util.Optional;

@Service
public class PetrolPricesServiceImpl implements PetrolPricesService {

    private PetrolPricesRepository petrolPricesRepository;

    @Autowired
    public PetrolPricesServiceImpl(PetrolPricesRepository petrolPricesRepository) {
        this.petrolPricesRepository = petrolPricesRepository;
    }


    @Override
    public PetrolPricesEntity getPetrolPricesByStationId(PetrolStationEntity petrolStationEntity) {
        Optional<PetrolPricesEntity> petrolPricesEntity = petrolPricesRepository.findByStationId(petrolStationEntity.getStationId());

        return petrolPricesEntity.orElseGet(() -> petrolPricesEntity.orElse(
                new PetrolPricesEntity(0, 0,0 ,0f, 0f, 0f, 0f)));
    }
}
