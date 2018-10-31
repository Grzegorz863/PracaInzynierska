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
        //Collection<HistoricPricesEntity> historicPricesEntities = historicPricesRepository
       //         .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        Collection<HistoricPb95PricesEntity> historicPb95PricesEntities = historicPb95PricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        Collection<HistoricPb98PricesEntity> historicPb98PricesEntities = historicPb98PricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        Collection<HistoricOnPricesEntity> historicOnPricesEntities = historicOnPricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        Collection<HistoricLpgPricesEntity> historicLpgPricesEntities = historicLpgPricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        OptionalDouble averagePb95Price = historicPb95PricesEntities.stream().mapToDouble(HistoricPb95PricesEntity::getPb95Price).average();
        OptionalDouble averagePb98Price = historicPb98PricesEntities.stream().mapToDouble(HistoricPb98PricesEntity::getPb98Price).average();
        OptionalDouble averageOnPrice = historicOnPricesEntities.stream().mapToDouble(HistoricOnPricesEntity::getOnPrice).average();
        OptionalDouble averageLpgPrice = historicLpgPricesEntities.stream().mapToDouble(HistoricLpgPricesEntity::getLpgPrice).average();

        PetrolPricesResponse petrolPricesResponse = new PetrolPricesResponse();

        if (averagePb95Price.isPresent())
            petrolPricesResponse.setPb95Price((float) averagePb95Price.getAsDouble());
        else
            petrolPricesResponse.setPb95Price(0f);

        if (averagePb98Price.isPresent())
            petrolPricesResponse.setPb98Price((float) averagePb98Price.getAsDouble());
        else
            petrolPricesResponse.setPb98Price(0f);

        if (averageOnPrice.isPresent())
            petrolPricesResponse.setOnPrice((float) averageOnPrice.getAsDouble());
        else
            petrolPricesResponse.setOnPrice(0f);

        if (averageLpgPrice.isPresent())
            petrolPricesResponse.setLpgPrice((float) averageLpgPrice.getAsDouble());
        else
            petrolPricesResponse.setLpgPrice(0f);

        return petrolPricesResponse;
    }

    @Override
    public void addHistoricPrices(Long stationId, Long userId, Float pb95Price, Float pb98Price, Float onPrice, Float lpgPrice) {

        if(pb95Price != null)
            historicPb95PricesRepository.save(new HistoricPb95PricesEntity(stationId, userId, pb95Price));

        if(pb98Price != null)
            historicPb98PricesRepository.save(new HistoricPb98PricesEntity(stationId, userId, pb98Price));

        if(onPrice != null)
            historicOnPricesRepository.save(new HistoricOnPricesEntity(stationId, userId, onPrice));

        if(lpgPrice != null)
            historicLpgPricesRepository.save(new HistoricLpgPricesEntity(stationId, userId, lpgPrice));
    }
}
