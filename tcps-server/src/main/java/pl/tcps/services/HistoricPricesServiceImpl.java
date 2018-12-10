package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.*;
import pl.tcps.pojo.PetrolPricesAndDateResponse;
import pl.tcps.pojo.PetrolPricesResponse;
import pl.tcps.repositories.*;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.OptionalDouble;

@Service
public class HistoricPricesServiceImpl implements HistoricPricesService {

    private HistoricPricesRepository historicPricesRepository;
    private HistoricPb95PricesRepository historicPb95PricesRepository;
    private HistoricPb98PricesRepository historicPb98PricesRepository;
    private HistoricOnPricesRepository historicOnPricesRepository;
    private HistoricLpgPricesRepository historicLpgPricesRepository;

    @Autowired
    public HistoricPricesServiceImpl(HistoricPricesRepository historicPricesRepository,
                                     HistoricPb95PricesRepository historicPb95PricesRepository,
                                     HistoricPb98PricesRepository historicPb98PricesRepository,
                                     HistoricOnPricesRepository historicOnPricesRepository,
                                     HistoricLpgPricesRepository historicLpgPricesRepository) {
        this.historicPricesRepository = historicPricesRepository;
        this.historicPb95PricesRepository = historicPb95PricesRepository;
        this.historicPb98PricesRepository = historicPb98PricesRepository;
        this.historicOnPricesRepository = historicOnPricesRepository;
        this.historicLpgPricesRepository = historicLpgPricesRepository;
    }

    @Override
    public PetrolPricesResponse countTwoWeeksHistoricPricesAverage(Long stationId, ZonedDateTime insertDate) {

        insertDate = insertDate.minusWeeks(2);
        Timestamp insertDateMovedTwoWeeks = Timestamp.from(insertDate.toInstant());

        Collection<HistoricPb95PricesEntity> historicPb95PricesEntities = historicPb95PricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        Collection<HistoricPb98PricesEntity> historicPb98PricesEntities = historicPb98PricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        Collection<HistoricOnPricesEntity> historicOnPricesEntities = historicOnPricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        Collection<HistoricLpgPricesEntity> historicLpgPricesEntities = historicLpgPricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        OptionalDouble averagePb95Price = historicPb95PricesEntities.stream().filter(item -> item.getPb95Price() != 0).mapToDouble(HistoricPb95PricesEntity::getPb95Price).average();
        OptionalDouble averagePb98Price = historicPb98PricesEntities.stream().filter(item -> item.getPb98Price() != 0).mapToDouble(HistoricPb98PricesEntity::getPb98Price).average();
        OptionalDouble averageOnPrice = historicOnPricesEntities.stream().filter(item -> item.getOnPrice() != 0).mapToDouble(HistoricOnPricesEntity::getOnPrice).average();
        OptionalDouble averageLpgPrice = historicLpgPricesEntities.stream().filter(item -> item.getLpgPrice() != 0).mapToDouble(HistoricLpgPricesEntity::getLpgPrice).average();

        PetrolPricesResponse petrolPricesResponse = new PetrolPricesResponse();

        if (averagePb95Price.isPresent())
            petrolPricesResponse.setPb95Price((float) averagePb95Price.getAsDouble());
        else {
            HistoricPb95PricesEntity lastPb95Price = historicPb95PricesRepository
                    .findFirstByStationIdOrderByInsertDateDesc(stationId);
            if (lastPb95Price != null)
                petrolPricesResponse.setPb95Price(lastPb95Price.getPb95Price());
            else
                petrolPricesResponse.setPb95Price(0f);
        }

        if (averagePb98Price.isPresent())
            petrolPricesResponse.setPb98Price((float) averagePb98Price.getAsDouble());
        else {
            HistoricPb98PricesEntity lastPb98Price = historicPb98PricesRepository
                    .findFirstByStationIdOrderByInsertDateDesc(stationId);
            if (lastPb98Price != null)
                petrolPricesResponse.setPb98Price(lastPb98Price.getPb98Price());
            else
                petrolPricesResponse.setPb98Price(0f);
        }

        if (averageOnPrice.isPresent())
            petrolPricesResponse.setOnPrice((float) averageOnPrice.getAsDouble());
        else {
            HistoricOnPricesEntity lastOnPrice = historicOnPricesRepository
                    .findFirstByStationIdOrderByInsertDateDesc(stationId);
            if (lastOnPrice != null)
                petrolPricesResponse.setOnPrice(lastOnPrice.getOnPrice());
            else
                petrolPricesResponse.setOnPrice(0f);
        }

        if (averageLpgPrice.isPresent())
            petrolPricesResponse.setLpgPrice((float) averageLpgPrice.getAsDouble());
        else {
            HistoricLpgPricesEntity lastLpgPrice = historicLpgPricesRepository
                    .findFirstByStationIdOrderByInsertDateDesc(stationId);
            if (lastLpgPrice != null)
                petrolPricesResponse.setLpgPrice(lastLpgPrice.getLpgPrice());
            else
                petrolPricesResponse.setLpgPrice(0f);
        }

        return petrolPricesResponse;
    }

    @Override
    public void addHistoricPrices(Long stationId, Long userId, Float pb95Price, Float pb98Price, Float onPrice, Float lpgPrice) {

        HistoricPb95PricesEntity lastPb95Price = historicPb95PricesRepository
                .findFirstByStationIdOrderByInsertDateDesc(stationId);
        if (lastPb95Price != null) {
            if (pb95Price != null && pb95Price != lastPb95Price.getPb95Price())
                historicPb95PricesRepository.save(new HistoricPb95PricesEntity(stationId, userId, pb95Price));
        } else if (pb95Price != null)
            historicPb95PricesRepository.save(new HistoricPb95PricesEntity(stationId, userId, pb95Price));

        HistoricPb98PricesEntity lastPb98Price = historicPb98PricesRepository
                .findFirstByStationIdOrderByInsertDateDesc(stationId);
        if(lastPb98Price != null) {
            if (pb98Price != null && pb98Price != lastPb98Price.getPb98Price())
                historicPb98PricesRepository.save(new HistoricPb98PricesEntity(stationId, userId, pb98Price));
        }else if (pb98Price != null)
            historicPb98PricesRepository.save(new HistoricPb98PricesEntity(stationId, userId, pb98Price));

        HistoricOnPricesEntity lastOnPrice = historicOnPricesRepository
                .findFirstByStationIdOrderByInsertDateDesc(stationId);
        if(lastOnPrice != null) {
            if (onPrice != null && onPrice != lastOnPrice.getOnPrice())
                historicOnPricesRepository.save(new HistoricOnPricesEntity(stationId, userId, onPrice));
        }else if (onPrice != null)
            historicOnPricesRepository.save(new HistoricOnPricesEntity(stationId, userId, onPrice));

        HistoricLpgPricesEntity lastLpgPrice = historicLpgPricesRepository
                .findFirstByStationIdOrderByInsertDateDesc(stationId);
        if(lastLpgPrice != null) {
            if (lpgPrice != null && lpgPrice != lastLpgPrice.getLpgPrice())
                historicLpgPricesRepository.save(new HistoricLpgPricesEntity(stationId, userId, lpgPrice));
        }else if (lpgPrice != null)
            historicLpgPricesRepository.save(new HistoricLpgPricesEntity(stationId, userId, lpgPrice));
    }
}
