package pl.tcps.tcps.pojo.responses;

import com.google.gson.annotations.SerializedName;

public class GeoLocationResponse {

    @SerializedName("station_name")
    private String stationName;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    public GeoLocationResponse() {
    }

    public GeoLocationResponse(String stationName, Double latitude, Double longitude) {
        this.stationName = stationName;
        this.latitude = latitude;
        this.longitude = longitude;
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

}
