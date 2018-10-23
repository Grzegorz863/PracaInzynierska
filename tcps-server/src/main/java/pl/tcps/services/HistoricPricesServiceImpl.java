package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.HistoricPricesEntity;
import pl.tcps.pojo.PetrolPricesResponse;
import pl.tcps.repositories.HistoricPricesRepository;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.OptionalDouble;

@Service
public class HistoricPricesServiceImpl implements HistoricPricesService {

    private HistoricPricesRepository historicPricesRepository;

    @Autowired
    public HistoricPricesServiceImpl(HistoricPricesRepository historicPricesRepository) {
        this.historicPricesRepository = historicPricesRepository;
    }

    @Override
    public PetrolPricesResponse countTwoWeeksHistoricPricesAverage(Long stationId, ZonedDateTime insertDate) {

        insertDate = insertDate.minusWeeks(2);
        Timestamp insertDateMovedTwoWeeks = Timestamp.from(insertDate.toInstant());
        Collection<HistoricPricesEntity> historicPricesEntities = historicPricesRepository
                .findByStationIdAndInsertDateAfter(stationId, insertDateMovedTwoWeeks);

        OptionalDouble averagePb95Price = historicPricesEntities.stream().mapToDouble(HistoricPricesEntity::getPb95Price).average();
        OptionalDouble averagePb98Price = historicPricesEntities.stream().mapToDouble(HistoricPricesEntity::getPb98Price).average();
        OptionalDouble averageOnPrice = historicPricesEntities.stream().mapToDouble(HistoricPricesEntity::getOnPrice).average();
        OptionalDouble averageLpgPrice = historicPricesEntities.stream().mapToDouble(HistoricPricesEntity::getLpgPrice).average();

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
}
