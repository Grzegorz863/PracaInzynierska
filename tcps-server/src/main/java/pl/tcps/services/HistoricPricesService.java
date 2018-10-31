package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.pojo.PetrolPricesAndDateResponse;
import pl.tcps.pojo.PetrolPricesResponse;

import java.time.ZonedDateTime;

@Service
public interface HistoricPricesService {

    @PreAuthorize("hasAuthority('android_user')")
    PetrolPricesResponse countTwoWeeksHistoricPricesAverage(Long stationId, ZonedDateTime insertDate);

    @PreAuthorize("hasAuthority('android_user')")
    void addHistoricPrices(Long stationId, Long userId, Float pb95Price, Float pb98Price, Float onPrice, Float lpgPrice);
}
