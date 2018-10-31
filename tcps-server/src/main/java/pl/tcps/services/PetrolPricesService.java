package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.exceptions.PetrolPricesAlreadyExistsException;
import pl.tcps.exceptions.PetrolPricesNotExistsException;
import pl.tcps.pojo.PetrolPricesAndDateResponse;
import pl.tcps.pojo.PetrolPricesResponse;

@Service
public interface PetrolPricesService {

    @PreAuthorize("hasAuthority('android_user')")
    PetrolPricesAndDateResponse getPetrolPricesWithInsertDate(PetrolStationEntity petrolStationEntity);

    @PreAuthorize("hasAuthority('android_user')")
    PetrolPricesResponse getPetrolPricesByStationId(Long stationId);

    @PreAuthorize("hasAuthority('android_user')")
    PetrolPricesEntity createPetrolPricesForStation(Long stationId, Long userId, Float pb95Price, Float pb98Price,
                                                    Float onPrice, Float lpgPrice) throws PetrolPricesAlreadyExistsException;

    @PreAuthorize("hasAuthority('android_user')")
    void updatePetrolPricesForStation(Long stationId, Long userId, Float pb95Price, Float pb98Price,
                                                    Float onPrice, Float lpgPrice) throws PetrolPricesNotExistsException;
}
