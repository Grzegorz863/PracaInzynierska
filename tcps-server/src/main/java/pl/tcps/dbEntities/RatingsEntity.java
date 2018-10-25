package pl.tcps.dbEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ratings", schema = "tcpsdb", catalog = "")
public class RatingsEntity {

    @JsonIgnore
    private long ratingId;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("station_id")
    private long stationId;

    @JsonProperty("rate")
    private double rate;

    @JsonIgnore
    private UsersEntity usersByUserId;

    @JsonIgnore
    private PetrolStationEntity petrolStationsByStationId;

    public RatingsEntity() {
    }

    public RatingsEntity(long userId, long stationId, double rate) {
        this.userId = userId;
        this.stationId = stationId;
        this.rate = rate;
    }

    @Id
    @Column(name = "rating_id", nullable = false)
    public long getRatingId() {
        return ratingId;
    }

    public void setRatingId(long ratingId) {
        this.ratingId = ratingId;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "station_id", nullable = false)
    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    @Basic
    @Column(name = "rate", nullable = true, precision = 0)
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingsEntity that = (RatingsEntity) o;
        return ratingId == that.ratingId &&
                userId == that.userId &&
                stationId == that.stationId &&
                Double.compare(that.rate, rate) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ratingId, userId, stationId, rate);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
    public UsersEntity getUsersByUserId() {
        return usersByUserId;
    }

    public void setUsersByUserId(UsersEntity usersByUserId) {
        this.usersByUserId = usersByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "station_id", referencedColumnName = "station_id", nullable = false, insertable = false, updatable = false)
    public PetrolStationEntity getPetrolStationsByStationId() {
        return petrolStationsByStationId;
    }

    public void setPetrolStationsByStationId(PetrolStationEntity petrolStationsByStationId) {
        this.petrolStationsByStationId = petrolStationsByStationId;
    }
}
