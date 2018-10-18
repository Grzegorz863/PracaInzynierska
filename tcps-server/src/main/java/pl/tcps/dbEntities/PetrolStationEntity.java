package pl.tcps.dbEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.tcps.pojo.CreatePetrolStationParameter;
import pl.tcps.pojo.PetrolStationLocationCoordinates;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "petrol_stations", schema = "tcpsdb", catalog = "")
public class PetrolStationEntity {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long stationId;

    @JsonProperty("consortium_id")
    private long consortiumId;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("latitude")
    private double latitude;

    private byte hasFood;

    @JsonProperty("apartment_number")
    private String apartmentNumber;

    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("station_name")
    private String stationName;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("description")
    private String description;

    @JsonIgnore
    private Collection<HistoricPricesEntity> historicPricesByStationId;

    @JsonIgnore
    private Collection<PetrolPricesEntity> petrolPricesByStationId;

    @JsonIgnore
    private ConsortiumsEntity consortiumsByConsortiumId;

    @JsonIgnore
    private Collection<RatingsEntity> ratingsByStationId;

    public PetrolStationEntity() {
    }

    public PetrolStationEntity(CreatePetrolStationParameter petrolStationParameter,
                               PetrolStationLocationCoordinates coordinates, Long consortiumId) {
        this.consortiumId = consortiumId;
        this.longitude = coordinates.getLongitude();
        this.latitude = coordinates.getLatitude();
        this.hasFood = (byte)(petrolStationParameter.getHasFood()?1:0);
        this.apartmentNumber = petrolStationParameter.getApartmentNumber();
        this.postalCode = petrolStationParameter.getPostalCode();
        this.stationName = petrolStationParameter.getStationName();
        this.city = petrolStationParameter.getCity();
        this.street = petrolStationParameter.getStreet();
        this.description = petrolStationParameter.getDescription();
    }

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
    @JsonProperty("has_food")
    @Column(name = "has_food", nullable = false)
    public Boolean getHasFood() {
        return hasFood!=0;
    }

    @JsonProperty("has_food")
    public void setHasFood(Boolean hasFood) {
        this.hasFood = (byte)(hasFood?1:0);
    }

    @Basic
    @Column(name = "apartment_number", nullable = false, length = 20)
    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
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
        PetrolStationEntity that = (PetrolStationEntity) o;
        return stationId == that.stationId &&
                consortiumId == that.consortiumId &&
                Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.latitude, latitude) == 0 &&
                hasFood == that.hasFood &&
                Objects.equals(apartmentNumber, that.apartmentNumber) &&
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
