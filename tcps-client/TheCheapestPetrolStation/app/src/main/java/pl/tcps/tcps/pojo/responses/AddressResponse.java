package pl.tcps.tcps.pojo.responses;

import com.google.gson.annotations.SerializedName;

public class AddressResponse {

    @SerializedName("street")
    private String street;

    @SerializedName("apartment_number")
    private String apartmentNumber;

    @SerializedName("city")
    private String city;

    @SerializedName("postal_code")
    private String postalCode;

    public AddressResponse() {
    }

    public AddressResponse(String street, String apartmentNumber, String city, String postalCode) {
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.postalCode = postalCode;
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
}
