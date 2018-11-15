package pl.tcps.tcps.pojo.responses;

import com.google.gson.annotations.SerializedName;

public class PetrolStationReloadRecycleViewResponse {

    @SerializedName("station_id")
    private Long stationId;

    @SerializedName("prices")
    private PetrolPricesResponse petrolPricesResponse;

    @SerializedName("distance")
    private Double distance;

    @SerializedName("rating")
    private Double rating;

    public PetrolStationReloadRecycleViewResponse() {
    }

    public PetrolStationReloadRecycleViewResponse(Long stationId, PetrolPricesResponse petrolPricesResponse,
                                                  Double distance, Double rating) {
        this.stationId = stationId;
        this.petrolPricesResponse = petrolPricesResponse;
        this.distance = distance;
        this.rating = rating;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public PetrolPricesResponse getPetrolPricesResponse() {
        return petrolPricesResponse;
    }

    public void setPetrolPricesResponse(PetrolPricesResponse petrolPricesResponse) {
        this.petrolPricesResponse = petrolPricesResponse;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
