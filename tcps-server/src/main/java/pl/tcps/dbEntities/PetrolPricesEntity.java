package pl.tcps.dbEntities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "petrol_prices", schema = "tcpsdb", catalog = "")
public class PetrolPricesEntity {
    private long priceId;
    private long stationId;
    private double pb95Price;
    private double pb98Price;
    private double onPrice;
    private double lpgPrice;
    private PetrolStationsEntity petrolStationsByStationId;

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
    public double getPb95Price() {
        return pb95Price;
    }

    public void setPb95Price(double pb95Price) {
        this.pb95Price = pb95Price;
    }

    @Basic
    @Column(name = "pb98_price", nullable = true, precision = 0)
    public double getPb98Price() {
        return pb98Price;
    }

    public void setPb98Price(double pb98Price) {
        this.pb98Price = pb98Price;
    }

    @Basic
    @Column(name = "on_price", nullable = true, precision = 0)
    public double getOnPrice() {
        return onPrice;
    }

    public void setOnPrice(double onPrice) {
        this.onPrice = onPrice;
    }

    @Basic
    @Column(name = "lpg_price", nullable = true, precision = 0)
    public double getLpgPrice() {
        return lpgPrice;
    }

    public void setLpgPrice(double lpgPrice) {
        this.lpgPrice = lpgPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetrolPricesEntity that = (PetrolPricesEntity) o;
        return priceId == that.priceId &&
                stationId == that.stationId &&
                Double.compare(that.pb95Price, pb95Price) == 0 &&
                Double.compare(that.pb98Price, pb98Price) == 0 &&
                Double.compare(that.onPrice, onPrice) == 0 &&
                Double.compare(that.lpgPrice, lpgPrice) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(priceId, stationId, pb95Price, pb98Price, onPrice, lpgPrice);
    }

    @ManyToOne
    @JoinColumn(name = "station_id", referencedColumnName = "station_id", nullable = false)
    public PetrolStationsEntity getPetrolStationsByStationId() {
        return petrolStationsByStationId;
    }

    public void setPetrolStationsByStationId(PetrolStationsEntity petrolStationsByStationId) {
        this.petrolStationsByStationId = petrolStationsByStationId;
    }
}