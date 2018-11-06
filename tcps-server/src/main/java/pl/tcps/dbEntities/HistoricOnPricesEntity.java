package pl.tcps.dbEntities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "historic_on_prices", schema = "tcpsdb", catalog = "")
public class HistoricOnPricesEntity {
    private long historicOnPriceId;
    private Long userId;
    private long stationId;
    private float onPrice;
    private Timestamp insertDate;
    private UsersEntity usersByUserId;
    private PetrolStationEntity petrolStationsByStationId;

    public HistoricOnPricesEntity() {
    }

    public HistoricOnPricesEntity(long stationId, long userId, float onPrice) {
        this.userId = userId;
        this.stationId = stationId;
        this.onPrice = onPrice;
    }

    @Id
    @Column(name = "historic_on_price_id", nullable = false)
    public long getHistoricOnPriceId() {
        return historicOnPriceId;
    }

    public void setHistoricOnPriceId(long historicOnPriceId) {
        this.historicOnPriceId = historicOnPriceId;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
    @Column(name = "on_price", nullable = false, precision = 0)
    public float getOnPrice() {
        return onPrice;
    }

    public void setOnPrice(float onPrice) {
        this.onPrice = onPrice;
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
        HistoricOnPricesEntity that = (HistoricOnPricesEntity) o;
        return historicOnPriceId == that.historicOnPriceId &&
                userId == that.userId &&
                stationId == that.stationId &&
                Float.compare(that.onPrice, onPrice) == 0 &&
                Objects.equals(insertDate, that.insertDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(historicOnPriceId, userId, stationId, onPrice, insertDate);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = true, insertable = false, updatable = false)
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
