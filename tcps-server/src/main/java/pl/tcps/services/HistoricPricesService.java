package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.pojo.PetrolPricesResponse;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public interface HistoricPricesService {

    @PreAuthorize("hasAuthority('android_user')")
    PetrolPricesResponse countTwoWeeksHistoricPricesAverage(Long stationId, ZonedDateTime insertDate);
}
