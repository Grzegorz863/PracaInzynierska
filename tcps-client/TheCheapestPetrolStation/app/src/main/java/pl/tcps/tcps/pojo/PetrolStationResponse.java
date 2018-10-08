package pl.tcps.tcps.pojo;

import com.google.gson.annotations.SerializedName;

public class PetrolStationResponse {

    @SerializedName("station_name")
    private String stationName;

    @SerializedName("consortium_id")
    private Long consortiumId;

    @SerializedName("street")
    private String street;

    @SerializedName("apartment_number")
    private String apartmentNumber;

    @SerializedName("city")
    private String city;

    @SerializedName("postal_code")
    private String postalCode;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("has_food")
    private Boolean hasFood;

    @SerializedName("description")
    private String description;

    public PetrolStationResponse() {
    }

    public PetrolStationResponse(String stationName, Long consortiumId, String street,
                                 String apartmentNumber, String city, String postalCode,
                                 Double longitude, Double latitude, Boolean hasFood,
                                 String description) {
        this.stationName = stationName;
        this.consortiumId = consortiumId;
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.hasFood = hasFood;
        this.description = description;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Long getConsortiumId() {
        return consortiumId;
    }

    public void setConsortiumId(Long consortiumId) {
        this.consortiumId = consortiumId;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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
}
