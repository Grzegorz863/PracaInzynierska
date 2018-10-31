package pl.tcps.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.tcps.dbEntities.PetrolStationEntity;

public class PetrolStationSpecificInfoResponse {

    @JsonProperty("station_id")
    private long stationId;

    @JsonProperty("station_name")
    private String stationName;

    @JsonProperty("consortium_name")
    private String consortiumName;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("has_food")
    private Boolean hasFood;

    @JsonProperty("description")
    private String description;

    @JsonProperty("current_prices")
    private PetrolPricesAndDateResponse currentPetrolPricesResponse;

    @JsonProperty("historic_prices")
    private PetrolPricesResponse historicPetrolPricesResponse;

    @JsonProperty("address")
    private AddressResponse stationAddress;

    public PetrolStationSpecificInfoResponse() {
    }

    public PetrolStationSpecificInfoResponse(PetrolStationEntity petrolStationEntity, String consortiumName,
                                             Double rating, PetrolPricesAndDateResponse currentPetrolPricesResponse,
                                             PetrolPricesResponse historicPetrolPricesResponse) {
        this.stationId = petrolStationEntity.getStationId();
        this.stationName = petrolStationEntity.getStationName();
        this.consortiumName = consortiumName;
        this.rating = rating;
        this.hasFood = petrolStationEntity.getHasFood();
        this.description = petrolStationEntity.getDescription();
        this.currentPetrolPricesResponse = currentPetrolPricesResponse;
        this.historicPetrolPricesResponse = historicPetrolPricesResponse;
        this.stationAddress = new AddressResponse(petrolStationEntity.getStreet(), petrolStationEntity.getApartmentNumber(),
                petrolStationEntity.getCity(), petrolStationEntity.getPostalCode());
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

    public PetrolPricesResponse getHistoricPetrolPricesResponse() {
        return historicPetrolPricesResponse;
    }

    public void setHistoricPetrolPricesResponse(PetrolPricesResponse historicPetrolPricesResponse) {
        this.historicPetrolPricesResponse = historicPetrolPricesResponse;
    }

    public AddressResponse getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(AddressResponse stationAddress) {
        this.stationAddress = stationAddress;
    }
}
