package pl.tcps.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.tcps.dbEntities.PetrolPricesEntity;


public class PetrolPricesResponseRecycleViewItem {

    @JsonProperty("pb95_price")
    private Float pb95Price;

    @JsonProperty("pb98_price")
    private Float pb98Price;

    @JsonProperty("on_price")
    private Float onPrice;

    @JsonProperty("lpg_price")
    private Float lpgPrice;

    public PetrolPricesResponseRecycleViewItem() {
    }

    public PetrolPricesResponseRecycleViewItem(Float pb95Price, Float pb98Price, Float onPrice, Float lpgPrice) {
        this.pb95Price = pb95Price;
        this.pb98Price = pb98Price;
        this.onPrice = onPrice;
        this.lpgPrice = lpgPrice;
    }

    public PetrolPricesResponseRecycleViewItem(PetrolPricesEntity petrolPricesEntity){
        this.pb95Price = petrolPricesEntity.getPb95Price();
        this.pb98Price = petrolPricesEntity.getPb98Price();
        this.onPrice = petrolPricesEntity.getOnPrice();
        this.lpgPrice = petrolPricesEntity.getLpgPrice();
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
}
