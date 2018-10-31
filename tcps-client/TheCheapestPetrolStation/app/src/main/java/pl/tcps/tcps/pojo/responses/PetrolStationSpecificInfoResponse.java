package pl.tcps.tcps.pojo.responses;

import com.google.gson.annotations.SerializedName;

public class PetrolStationSpecificInfoResponse {

    @SerializedName("station_id")
    private long stationId;

    @SerializedName("station_name")
    private String stationName;

    @SerializedName("consortium_name")
    private String consortiumName;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("has_food")
    private Boolean hasFood;

    @SerializedName("description")
    private String description;

    @SerializedName("current_prices")
    private PetrolPricesAndDateResponse currentPetrolPricesResponse;

    @SerializedName("historic_prices")
    private PetrolPricesAndDateResponse historicPetrolPricesResponse;

    @SerializedName("address")
    private AddressResponse stationAddress;

    public PetrolStationSpecificInfoResponse() {
    }

    public PetrolStationSpecificInfoResponse(long stationId, String stationName, String consortiumName,
                                             Double rating, Boolean hasFood, String description,
                                             PetrolPricesAndDateResponse currentPetrolPricesResponse,
                                             PetrolPricesAndDateResponse historicPetrolPricesResponse,
                                             AddressResponse stationAddress) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.consortiumName = consortiumName;
        this.rating = rating;
        this.hasFood = hasFood;
        this.description = description;
        this.currentPetrolPricesResponse = currentPetrolPricesResponse;
        this.historicPetrolPricesResponse = historicPetrolPricesResponse;
        this.stationAddress = stationAddress;
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

    public PetrolPricesAndDateResponse getCurrentPetrolPricesResponse() {
        return currentPetrolPricesResponse;
    }

    public void setCurrentPetrolPricesResponse(PetrolPricesAndDateResponse currentPetrolPricesResponse) {
        this.currentPetrolPricesResponse = currentPetrolPricesResponse;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getHasFood() {
        return hasFood;
    }

    public void setHasFood(Boolean hasFood) {
        this.hasFood = hasFood;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PetrolPricesAndDateResponse getHistoricPetrolPricesResponse() {
        return historicPetrolPricesResponse;
    }

    public void setHistoricPetrolPricesResponse(PetrolPricesAndDateResponse historicPetrolPricesResponse) {
        this.historicPetrolPricesResponse = historicPetrolPricesResponse;
    }

    public AddressResponse getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(AddressResponse stationAddress) {
        this.stationAddress = stationAddress;
    }
}
