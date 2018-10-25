package pl.tcps.tcps.pojo.responses;

import com.google.gson.annotations.SerializedName;

public class StationRatingResponse {

    @SerializedName("user_id")
    private Long userId;

    @SerializedName("station_id")
    private Long stationId;

    @SerializedName("rate")
    private Double rate;

    public StationRatingResponse() {
    }

    public StationRatingResponse(Long userId, Long stationId, Double rate) {
        this.userId = userId;
        this.stationId = stationId;
        this.rate = rate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
