package pl.tcps.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class CreatePetrolStationParameter {

    private String stationName;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("apartment_number")
    private String apartmentNumber;

    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("description")
    private String description;

    @JsonProperty("has_food")
    private Boolean hasFood;

    @JsonProperty("consortium_name")
    private String consortiumName;

    public CreatePetrolStationParameter() {
    }

    @JsonCreator
    public CreatePetrolStationParameter(@JsonProperty("station_name") String stationName,
                                        @JsonProperty("city") String city,
                                        @JsonProperty("street") String street,
                                        @JsonProperty("apartment_number") String apartmentNumber,
                                        @JsonProperty("postal_code") String postalCode,
                                        @JsonProperty("description") String description,
                                        @JsonProperty("has_food") Boolean hasFood,
                                        @JsonProperty("consortium_name") String consortiumName) {
        this.stationName = stationName;
        this.city = city;
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.postalCode = postalCode;
        this.description = description;
        this.hasFood = hasFood;
        this.consortiumName = consortiumName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasFood() {
        return hasFood;
    }

    public void setHasFood(Boolean hasFood) {
        this.hasFood = hasFood;
    }

    public String getConsortiumName() {
        return consortiumName;
    }

    public void setConsortiumName(String consortiumName) {
        this.consortiumName = consortiumName;
    }
}
