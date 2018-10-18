package pl.tcps.tcps.pojo.responses;

import com.google.gson.annotations.SerializedName;

public class PetrolPriceRecycleViewItem {

    @SerializedName("pb95_price")
    private Float pb95Price;

    @SerializedName("pb98_price")
    private Float pb98Price;

    @SerializedName("on_price")
    private Float onPrice;

    @SerializedName("lpg_price")
    private Float lpgPrice;

    public PetrolPriceRecycleViewItem() {
    }

    public PetrolPriceRecycleViewItem(Float pb95Price, Float pb98Price, Float onPrice, Float lpgPrice) {
        this.pb95Price = pb95Price;
        this.pb98Price = pb98Price;
        this.onPrice = onPrice;
        this.lpgPrice = lpgPrice;
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
