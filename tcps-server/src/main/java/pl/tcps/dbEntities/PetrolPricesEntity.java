package pl.tcps.dbEntities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "petrol_prices", schema = "tcpsdb", catalog = "")
public class PetrolPricesEntity {

    private long priceId;
    private long stationId;
    private long userId;
    private float pb95Price;
    private float pb98Price;
    private float onPrice;
    private float lpgPrice;
    private Timestamp insertDate;
    private UsersEntity usersByUserId;
    private PetrolStationEntity petrolStationsByStationId;

    public PetrolPricesEntity() { }

    public PetrolPricesEntity(long priceId, long stationId, long userId, float pb95Price, float pb98Price,
                              float onPrice, float lpgPrice, Timestamp insertDate) {
        this.priceId = priceId;
        this.stationId = stationId;
        this.userId = userId;
        this.pb95Price = pb95Price;
        this.pb98Price = pb98Price;
        this.onPrice = onPrice;
        this.lpgPrice = lpgPrice;
        this.insertDate = insertDate;
    }

    @Id
    @Column(name = "price_id", nullable = false)
    public long getPriceId() {
        return priceId;
    }

    public void setPriceId(long priceId) {
        this.priceId = priceId;
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
    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "pb95_price", nullable = true, precision = 0)
    public float getPb95Price() {
        return pb95Price;
    }

    public void setPb95Price(float pb95Price) {
        this.pb95Price = pb95Price;
    }

    @Basic
    @Column(name = "pb98_price", nullable = true, precision = 0)
    public float getPb98Price() {
        return pb98Price;
    }

    public void setPb98Price(float pb98Price) {
        this.pb98Price = pb98Price;
    }

    @Basic
    @Column(name = "on_price", nullable = true, precision = 0)
    public float getOnPrice() {
        return onPrice;
    }

    public void setOnPrice(float onPrice) {
        this.onPrice = onPrice;
    }

    @Basic
    @Column(name = "lpg_price", nullable = true, precision = 0)
    public float getLpgPrice() {
        return lpgPrice;
    }

    public void setLpgPrice(float lpgPrice) {
        this.lpgPrice = lpgPrice;
    }

    @Basic
    @Column(name = "insert_date", nullable = false)
    public Timestamp getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Timestamp insertDate) {
        this.insertDate = insertDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetrolPricesEntity that = (PetrolPricesEntity) o;
        return priceId == that.priceId &&
                stationId == that.stationId &&
                userId == that.userId &&
                Float.compare(that.pb95Price, pb95Price) == 0 &&
                Float.compare(that.pb98Price, pb98Price) == 0 &&
                Float.compare(that.onPrice, onPrice) == 0 &&
                Float.compare(that.lpgPrice, lpgPrice) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(priceId, stationId, userId, pb95Price, pb98Price, onPrice, lpgPrice);
    }

    @ManyToOne
    @JoinColumn(name = "station_id", referencedColumnName = "station_id", nullable = false, insertable = false, updatable = false)
    public PetrolStationEntity getPetrolStationsByStationId() {
        return petrolStationsByStationId;
    }

    public void setPetrolStationsByStationId(PetrolStationEntity petrolStationsByStationId) {
        this.petrolStationsByStationId = petrolStationsByStationId;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
    public UsersEntity getUsersByUserId() {
        return usersByUserId;
    }

    public void setUsersByUserId(UsersEntity usersByUserId) {
        this.usersByUserId = usersByUserId;
    }
}
