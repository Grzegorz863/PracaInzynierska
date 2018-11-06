package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.exceptions.PetrolPricesAlreadyExistsException;
import pl.tcps.exceptions.PetrolPricesNotExistsException;
import pl.tcps.pojo.PetrolPricesAndDateResponse;
import pl.tcps.pojo.PetrolPricesResponse;
import pl.tcps.repositories.PetrolPricesRepository;

import java.time.ZoneId;
import java.util.Optional;

@Service
public class PetrolPricesServiceImpl implements PetrolPricesService {

    private PetrolPricesRepository petrolPricesRepository;
    private HistoricPricesService historicPricesService;

    @Autowired
    public PetrolPricesServiceImpl(PetrolPricesRepository petrolPricesRepository, HistoricPricesService historicPricesService) {
        this.petrolPricesRepository = petrolPricesRepository;
        this.historicPricesService = historicPricesService;
    }

    @Override
    public PetrolPricesAndDateResponse getPetrolPricesWithInsertDate(PetrolStationEntity petrolStationEntity) {
        Optional<PetrolPricesEntity> petrolPricesEntity = petrolPricesRepository.findByStationId(petrolStationEntity.getStationId());

        return petrolPricesEntity.map(petrolPricesEntity1 -> new PetrolPricesAndDateResponse(petrolPricesEntity1,
                petrolPricesEntity1.getInsertDate().toLocalDateTime().atZone(ZoneId.of("Europe/Warsaw"))))
                .orElseGet(() -> new PetrolPricesAndDateResponse(0f, 0f, 0f, 0f));
    }

    @Override
    public PetrolPricesResponse getPetrolPricesByStationId(Long stationId) {
        Optional<PetrolPricesEntity> petrolPricesEntity = petrolPricesRepository.findByStationId(stationId);

        return petrolPricesEntity.map(PetrolPricesResponse::new).orElseGet(() -> new PetrolPricesResponse(0f, 0f, 0f, 0f));
    }

    @Override
    public PetrolPricesEntity createPetrolPricesForStation(Long stationId, Long userId, Float pb95Price, Float pb98Price,
                                                           Float onPrice, Float lpgPrice) throws PetrolPricesAlreadyExistsException{

        if(petrolPricesRepository.existsByStationId(stationId))
            throw new PetrolPricesAlreadyExistsException("Petrol prices already exists for this station");

        PetrolPricesEntity petrolPricesEntity = new PetrolPricesEntity(stationId, userId);
        petrolPricesEntity = preparePetrolPrices(petrolPricesEntity, pb95Price, pb98Price, onPrice, lpgPrice);

        petrolPricesEntity = petrolPricesRepository.save(petrolPricesEntity);
        historicPricesService.addHistoricPrices(stationId, userId, pb95Price, pb98Price, onPrice, lpgPrice);

        return petrolPricesEntity;
    }

    @Override
    public void updatePetrolPricesForStation(Long stationId, Long userId, Float pb95Price, Float pb98Price,
                                             Float onPrice, Float lpgPrice) throws PetrolPricesNotExistsException{

        if(!petrolPricesRepository.existsByStationId(stationId))
            throw new PetrolPricesNotExistsException("Petrol prices do not exists for this station");

        if(pb95Price != null)
            petrolPricesRepository.updatePb95Price(stationId, userId, pb95Price);

        if(pb98Price != null)
            petrolPricesRepository.updatePb98Price(stationId, userId, pb98Price);

        if(onPrice != null)
            petrolPricesRepository.updateOnPrice(stationId, userId, onPrice);

        if(lpgPrice != null)
            petrolPricesRepository.updateLpgPrice(stationId, userId, lpgPrice);

        historicPricesService.addHistoricPrices(stationId, userId, pb95Price, pb98Price, onPrice, lpgPrice);
    }

    private PetrolPricesEntity preparePetrolPrices(PetrolPricesEntity petrolPricesEntity, Float pb95Price,
                                                   Float pb98Price, Float onPrice, Float lpgPrice) {

        if(pb95Price == null)
            petrolPricesEntity.setPb95Price(0f);
        else
            petrolPricesEntity.setPb95Price(pb95Price);

        if(pb98Price == null)
            petrolPricesEntity.setPb98Price(0f);
        else
            petrolPricesEntity.setPb98Price(pb98Price);

        if(onPrice == null)
            petrolPricesEntity.setOnPrice(0f);
        else
            petrolPricesEntity.setOnPrice(onPrice);

        if(lpgPrice == null)
            petrolPricesEntity.setLpgPrice(0f);
        else
            petrolPricesEntity.setLpgPrice(lpgPrice);

        return petrolPricesEntity;
    }
}
