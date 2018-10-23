package pl.tcps.tcps.pojo;

import com.google.gson.annotations.SerializedName;

import pl.tcps.tcps.pojo.responses.PetrolPriceRecycleViewItem;

public class PetrolStationRecycleViewItem {

    @SerializedName("station_id")
    private long stationId;

    @SerializedName("station_name")
    private String stationName;

    @SerializedName("consortium_name")
    private String consortiumName;

    @SerializedName("prices")
    private PetrolPriceRecycleViewItem priceRecycleViewItem;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("distance")
    private Double distance;

    public PetrolStationRecycleViewItem() {
    }

    public PetrolStationRecycleViewItem(long stationId, String stationName, String consortiumName,
                                        PetrolPriceRecycleViewItem priceRecycleViewItem, Double rating, Double distance) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.consortiumName = consortiumName;
        this.priceRecycleViewItem = priceRecycleViewItem;
        this.rating = rating;
        this.distance = distance;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getConsortiumName() {
        return consortiumName;
    }

    public void setConsortiumName(String consortiumName) {
        this.consortiumName = consortiumName;
    }

    public PetrolPriceRecycleViewItem getPriceRecycleViewItem() {
        return priceRecycleViewItem;
    }

    public void setPriceRecycleViewItem(PetrolPriceRecycleViewItem priceRecycleViewItem) {
        this.priceRecycleViewItem = priceRecycleViewItem;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
