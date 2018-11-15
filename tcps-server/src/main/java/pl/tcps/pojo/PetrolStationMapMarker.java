package pl.tcps.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PetrolStationMapMarker {

    @JsonProperty("station_id")
    private Long stationId;

    @JsonProperty("station_name")
    private String stationName;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("distance")
    private Double distance;

    @JsonProperty("address")
    private AddressResponse address;

    public PetrolStationMapMarker() {
    }

    public PetrolStationMapMarker(Long stationId, String stationName, Double latitude, Double longitude,
                                  Double distance,  AddressResponse address) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }
}
