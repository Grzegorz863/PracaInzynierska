package pl.tcps.dbEntities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "historic_lpg_prices", schema = "tcpsdb", catalog = "")
public class HistoricLpgPricesEntity {
    private long historicLpgPriceId;
    private long userId;
    private long stationId;
    private float lpgPrice;
    private Timestamp insertDate;
    private UsersEntity usersByUserId;
    private PetrolStationEntity petrolStationsByStationId;

    public HistoricLpgPricesEntity() {
    }

    public HistoricLpgPricesEntity(long stationId, long userId, float lpgPrice) {
        this.userId = userId;
        this.stationId = stationId;
        this.lpgPrice = lpgPrice;
    }

    @Id
    @Column(name = "historic_lpg_price_id", nullable = false)
    public long getHistoricLpgPriceId() {
        return historicLpgPriceId;
    }

    public void setHistoricLpgPriceId(long historicLpgPriceId) {
        this.historicLpgPriceId = historicLpgPriceId;
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
    @Column(name = "lpg_price", nullable = false, precision = 0)
    public float getLpgPrice() {
        return lpgPrice;
    }

    public void setLpgPrice(float lpgPrice) {
        this.lpgPrice = lpgPrice;
    }

    @Basic
    @Column(name = "insert_date", nullable = false, insertable = false, updatable = false)
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
        HistoricLpgPricesEntity that = (HistoricLpgPricesEntity) o;
        return historicLpgPriceId == that.historicLpgPriceId &&
                userId == that.userId &&
                stationId == that.stationId &&
                Float.compare(that.lpgPrice, lpgPrice) == 0 &&
                Objects.equals(insertDate, that.insertDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(historicLpgPriceId, userId, stationId, lpgPrice, insertDate);
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
