package pl.tcps.pojo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.tcps.deserializers.PetrolStationLocationDeserializer;

@JsonDeserialize(using = PetrolStationLocationDeserializer.class)
public class PetrolStationLocationCoordinates {

    private Double latitude;
    private Double longitude;

    public PetrolStationLocationCoordinates() {
    }

    public PetrolStationLocationCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
