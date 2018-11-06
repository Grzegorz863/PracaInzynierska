package pl.tcps.dbEntities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "historic_pb98_prices", schema = "tcpsdb", catalog = "")
public class HistoricPb98PricesEntity {
    private long historicPb98PriceId;
    private Long userId;
    private long stationId;
    private float pb98Price;
    private Timestamp insertDate;
    private UsersEntity usersByUserId;
    private PetrolStationEntity petrolStationsByStationId;

    public HistoricPb98PricesEntity() {
    }

    public HistoricPb98PricesEntity(long stationId, long userId, float pb98Price) {
        this.userId = userId;
        this.stationId = stationId;
        this.pb98Price = pb98Price;
    }

    @Id
    @Column(name = "historic_pb98_price_id", nullable = false)
    public long getHistoricPb98PriceId() {
        return historicPb98PriceId;
    }

    public void setHistoricPb98PriceId(long historicPb98PriceId) {
        this.historicPb98PriceId = historicPb98PriceId;
    }

    @Basic
    @Column(name = "user_id", nullable = true)
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
    @Column(name = "pb98_price", nullable = false, precision = 0)
    public float getPb98Price() {
        return pb98Price;
    }

    public void setPb98Price(float pb98Price) {
        this.pb98Price = pb98Price;
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
        HistoricPb98PricesEntity that = (HistoricPb98PricesEntity) o;
        return historicPb98PriceId == that.historicPb98PriceId &&
                userId == that.userId &&
                stationId == that.stationId &&
                Float.compare(that.pb98Price, pb98Price) == 0 &&
                Objects.equals(insertDate, that.insertDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(historicPb98PriceId, userId, stationId, pb98Price, insertDate);
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
