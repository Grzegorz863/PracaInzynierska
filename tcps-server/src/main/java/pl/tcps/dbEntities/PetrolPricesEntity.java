package pl.tcps.dbEntities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "petrol_prices", schema = "tcpsdb", catalog = "")
public class PetrolPricesEntity {
    private long priceId;
    private long stationId;
    private float pb95Price;
    private float pb98Price;
    private float onPrice;
    private float lpgPrice;
    private PetrolStationEntity petrolStationsByStationId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetrolPricesEntity that = (PetrolPricesEntity) o;
        return priceId == that.priceId &&
                stationId == that.stationId &&
                Float.compare(that.pb95Price, pb95Price) == 0 &&
                Float.compare(that.pb98Price, pb98Price) == 0 &&
                Float.compare(that.onPrice, onPrice) == 0 &&
                Float.compare(that.lpgPrice, lpgPrice) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(priceId, stationId, pb95Price, pb98Price, onPrice, lpgPrice);
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
