package pl.tcps.dbEntities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "historic_pb95_prices", schema = "tcpsdb", catalog = "")
public class HistoricPb95PricesEntity {
    private long historicPb95PriceId;
    private long userId;
    private long stationId;
    private float pb95Price;
    private Timestamp insertDate;
    private UsersEntity usersByUserId;
    private PetrolStationEntity petrolStationsByStationId;

    public HistoricPb95PricesEntity() {
    }

    public HistoricPb95PricesEntity(long stationId, long userId , float pb95Price) {
        this.userId = userId;
        this.stationId = stationId;
        this.pb95Price = pb95Price;
    }

    @Id
    @Column(name = "historic_pb95_price_id", nullable = false)
    public long getHistoricPb95PriceId() {
        return historicPb95PriceId;
    }

    public void setHistoricPb95PriceId(long historicPb95PriceId) {
        this.historicPb95PriceId = historicPb95PriceId;
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
    @Column(name = "pb95_price", nullable = false, precision = 0)
    public float getPb95Price() {
        return pb95Price;
    }

    public void setPb95Price(float pb95Price) {
        this.pb95Price = pb95Price;
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
        HistoricPb95PricesEntity that = (HistoricPb95PricesEntity) o;
        return historicPb95PriceId == that.historicPb95PriceId &&
                userId == that.userId &&
                stationId == that.stationId &&
                Float.compare(that.pb95Price, pb95Price) == 0 &&
                Objects.equals(insertDate, that.insertDate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(historicPb95PriceId, userId, stationId, pb95Price, insertDate);
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
