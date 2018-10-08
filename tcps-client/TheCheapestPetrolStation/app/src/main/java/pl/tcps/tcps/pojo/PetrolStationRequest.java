package pl.tcps.tcps.pojo;


import com.google.gson.annotations.SerializedName;

public class PetrolStationRequest {

    @SerializedName("station_name")
    private String stationName;

    @SerializedName("street")
    private String street;

    @SerializedName("apartment_number")
    private String apartmentNumber;

    @SerializedName("city")
    private String city;

    @SerializedName("postal_code")
    private String postalCode;

    @SerializedName("consortium_name")
    private String selectedConsortiumName;

    @SerializedName("has_food")
    private Boolean hasFood;

    @SerializedName("description")
    private String description;

    public PetrolStationRequest(String stationName, String street, String apartmentNumber, String city,
                                String postalCode, String selectedConsortiumName,
                                Boolean hasFood, String description) {
        this.stationName = stationName;
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.selectedConsortiumName = selectedConsortiumName;
        this.hasFood = hasFood;
        this.description = description;
    }

    public PetrolStationRequest() {
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    public String getSelectedConsortiumName() {
        return selectedConsortiumName;
    }

    public void setSelectedConsortiumName(String selectedConsortiumName) {
        this.selectedConsortiumName = selectedConsortiumName;
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
