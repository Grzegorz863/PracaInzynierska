package pl.tcps.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.tcps.dbEntities.PetrolPricesEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class PetrolPricesResponse {

    @JsonProperty("pb95_price")
    private Float pb95Price;

    @JsonProperty("pb98_price")
    private Float pb98Price;

    @JsonProperty("on_price")
    private Float onPrice;

    @JsonProperty("lpg_price")
    private Float lpgPrice;

    @JsonProperty("insert_date")
    private ZonedDateTime insertDate;

    public PetrolPricesResponse() {
        this.insertDate = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Warsaw"));
    }

    public PetrolPricesResponse(Float pb95Price, Float pb98Price, Float onPrice, Float lpgPrice) {
        this.pb95Price = pb95Price;
        this.pb98Price = pb98Price;
        this.onPrice = onPrice;
        this.lpgPrice = lpgPrice;
        this.insertDate = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Europe/Warsaw"));
    }

    public PetrolPricesResponse(PetrolPricesEntity petrolPricesEntity, ZonedDateTime insertDate){
        this.pb95Price = petrolPricesEntity.getPb95Price();
        this.pb98Price = petrolPricesEntity.getPb98Price();
        this.onPrice = petrolPricesEntity.getOnPrice();
        this.lpgPrice = petrolPricesEntity.getLpgPrice();
        this.insertDate = insertDate;
    }

    public Float getPb95Price() {
        return pb95Price;
    }

    public void setPb95Price(Float pb95Price) {
        this.pb95Price = pb95Price;
    }

    public Float getPb98Price() {
        return pb98Price;
    }

    public void setPb98Price(Float pb98Price) {
        this.pb98Price = pb98Price;
    }

    public Float getOnPrice() {
        return onPrice;
    }

    public void setOnPrice(Float onPrice) {
        this.onPrice = onPrice;
    }

    public Float getLpgPrice() {
        return lpgPrice;
    }

    public void setLpgPrice(Float lpgPrice) {
        this.lpgPrice = lpgPrice;
    }

    public ZonedDateTime getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(ZonedDateTime insertDate) {
        this.insertDate = insertDate;
    }
}
