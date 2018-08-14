package pl.tcps.dbEntities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "petrol_stations", schema = "tcpsdb", catalog = "")
public class PetrolStationsEntity {
    private long stationId;
    private long consortiumId;
    private double longitude;
    private double latitude;
    private byte hasFood;
    private int apartmentNumber;
    private String postalCode;
    private String stationName;
    private String city;
    private String street;
    private String description;
    private Collection<HistoricPricesEntity> historicPricesByStationId;
    private Collection<PetrolPricesEntity> petrolPricesByStationId;
    private ConsortiumsEntity consortiumsByConsortiumId;
    private Collection<RatingsEntity> ratingsByStationId;

    @Id
    @Column(name = "station_id", nullable = false)
    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    @Basic
    @Column(name = "consortium_id", nullable = false)
    public long getConsortiumId() {
        return consortiumId;
    }

    public void setConsortiumId(long consortiumId) {
        this.consortiumId = consortiumId;
    }

    @Basic
    @Column(name = "longitude", nullable = false, precision = 0)
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "latitude", nullable = false, precision = 0)
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "has_food", nullable = false)
    public byte getHasFood() {
        return hasFood;
    }

    public void setHasFood(byte hasFood) {
        this.hasFood = hasFood;
    }

    @Basic
    @Column(name = "apartment_number", nullable = false)
    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    @Basic
    @Column(name = "postal_code", nullable = true, length = 6)
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Basic
    @Column(name = "station_name", nullable = false, length = 100)
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Basic
    @Column(name = "city", nullable = false, length = 50)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "street", nullable = false, length = 70)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetrolStationsEntity that = (PetrolStationsEntity) o;
        return stationId == that.stationId &&
                consortiumId == that.consortiumId &&
                Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.latitude, latitude) == 0 &&
                hasFood == that.hasFood &&
                apartmentNumber == that.apartmentNumber &&
                Objects.equals(postalCode, that.postalCode) &&
                Objects.equals(stationName, that.stationName) &&
                Objects.equals(city, that.city) &&
                Objects.equals(street, that.street) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stationId, consortiumId, longitude, latitude, hasFood, apartmentNumber, postalCode, stationName, city, street, description);
    }

    @OneToMany(mappedBy = "petrolStationsByStationId")
    public Collection<HistoricPricesEntity> getHistoricPricesByStationId() {
        return historicPricesByStationId;
    }

    public void setHistoricPricesByStationId(Collection<HistoricPricesEntity> historicPricesByStationId) {
        this.historicPricesByStationId = historicPricesByStationId;
    }

    @OneToMany(mappedBy = "petrolStationsByStationId")
    public Collection<PetrolPricesEntity> getPetrolPricesByStationId() {
        return petrolPricesByStationId;
    }

    public void setPetrolPricesByStationId(Collection<PetrolPricesEntity> petrolPricesByStationId) {
        this.petrolPricesByStationId = petrolPricesByStationId;
    }

    @ManyToOne
    @JoinColumn(name = "consortium_id", referencedColumnName = "consortium_id", nullable = false, insertable = false, updatable = false)
    public ConsortiumsEntity getConsortiumsByConsortiumId() {
        return consortiumsByConsortiumId;
    }

    public void setConsortiumsByConsortiumId(ConsortiumsEntity consortiumsByConsortiumId) {
        this.consortiumsByConsortiumId = consortiumsByConsortiumId;
    }

    @OneToMany(mappedBy = "petrolStationsByStationId")
    public Collection<RatingsEntity> getRatingsByStationId() {
        return ratingsByStationId;
    }

    public void setRatingsByStationId(Collection<RatingsEntity> ratingsByStationId) {
        this.ratingsByStationId = ratingsByStationId;
    }
}
