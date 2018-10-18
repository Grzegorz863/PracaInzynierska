package pl.tcps.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.tcps.dbEntities.PetrolStationEntity;

public class PetrolStationResponseRecycleViewItem {

    @JsonProperty("station_id")
    private long stationId;

    @JsonProperty("station_name")
    private String stationName;

    @JsonProperty("consortium_name")
    private String consortiumName;

    @JsonProperty("prices")
    private PetrolPricesResponseRecycleViewItem petrolPricesResponseRecycleViewItem;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("distance")
    private Double distance;

    public PetrolStationResponseRecycleViewItem() {
    }

    public PetrolStationResponseRecycleViewItem(PetrolStationEntity petrolStationEntity, String consortiumName,
                                                PetrolPricesResponseRecycleViewItem petrolPricesResponse,
                                                Double rating, Double distance) {
        this.stationId = petrolStationEntity.getStationId();
        this.stationName = petrolStationEntity.getStationName();
        this.consortiumName = consortiumName;
        this.petrolPricesResponseRecycleViewItem = petrolPricesResponse;
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

    public PetrolPricesResponseRecycleViewItem getPetrolPricesResponseRecycleViewItem() {
        return petrolPricesResponseRecycleViewItem;
    }

    public void setPetrolPricesResponseRecycleViewItem(PetrolPricesResponseRecycleViewItem petrolPricesResponseRecycleViewItem) {
        this.petrolPricesResponseRecycleViewItem = petrolPricesResponseRecycleViewItem;
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
