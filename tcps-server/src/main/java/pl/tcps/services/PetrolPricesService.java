package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.PetrolPricesEntity;
import pl.tcps.dbEntities.PetrolStationEntity;

@Service
public interface PetrolPricesService {

    @PreAuthorize("hasAuthority('android_user')")
    PetrolPricesEntity getPetrolPricesByStationId(PetrolStationEntity petrolStationEntity);

}
