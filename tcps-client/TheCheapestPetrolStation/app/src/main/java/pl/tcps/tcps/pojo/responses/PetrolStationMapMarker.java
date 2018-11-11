package pl.tcps.tcps.pojo.responses;

import com.google.gson.annotations.SerializedName;

public class PetrolStationMapMarker {

    @SerializedName("station_id")
    private Long stationId;

    @SerializedName("station_name")
    private String stationName;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("address")
    private AddressResponse address;

    public PetrolStationMapMarker() {
    }

    public PetrolStationMapMarker(Long stationId, String stationName, Double latitude, Double longitude, AddressResponse address) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }
}
