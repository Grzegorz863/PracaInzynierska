package pl.tcps.dbEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "tcpsdb", catalog = "")
public class UsersEntity {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long userId;

    @JsonIgnore
    private String userPassword;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_role")
    private String userRole;

    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonIgnore
    private Collection<HistoricPricesEntity> historicPricesByUserId;

    @JsonIgnore
    private Collection<HistoricPb95PricesEntity> historicPb95PricesByStationId;

    @JsonIgnore
    private Collection<HistoricPb98PricesEntity> historicPb98PricesByStationId;

    @JsonIgnore
    private Collection<HistoricOnPricesEntity> historicOnPricesByStationId;

    @JsonIgnore
    private Collection<HistoricLpgPricesEntity> historicLpgPricesByStationId;

    @JsonIgnore
    private Collection<RatingsEntity> ratingsByUserId;

    @Id
    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 60)
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Basic
    @Column(name = "user_role", nullable = false, length = 25)
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Basic
    @Column(name = "enabled", nullable = false, length = 25)
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isActive) {
        this.isEnabled = isActive;
    }

    @Basic
    @Column(name = "first_name", nullable = true, length = 45)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = true, length = 45)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 45)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return userId == that.userId &&
                Objects.equals(userPassword, that.userPassword) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(userRole, that.userRole)&&
                Objects.equals(isEnabled, that.isEnabled) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, userPassword, userName, userRole, isEnabled, firstName, lastName, email);
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<HistoricPricesEntity> getHistoricPricesByUserId() {
        return historicPricesByUserId;
    }

    public void setHistoricPricesByUserId(Collection<HistoricPricesEntity> historicPricesByUserId) {
        this.historicPricesByUserId = historicPricesByUserId;
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<HistoricPb95PricesEntity> getHistoricPb95PricesByStationId() {
        return historicPb95PricesByStationId;
    }

    public void setHistoricPb95PricesByStationId(Collection<HistoricPb95PricesEntity> historicPb95PricesByStationId) {
        this.historicPb95PricesByStationId = historicPb95PricesByStationId;
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<HistoricPb98PricesEntity> getHistoricPb98PricesByStationId() {
        return historicPb98PricesByStationId;
    }

    public void setHistoricPb98PricesByStationId(Collection<HistoricPb98PricesEntity> historicPb98PricesByStationId) {
        this.historicPb98PricesByStationId = historicPb98PricesByStationId;
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<HistoricOnPricesEntity> getHistoricOnPricesByStationId() {
        return historicOnPricesByStationId;
    }

    public void setHistoricOnPricesByStationId(Collection<HistoricOnPricesEntity> historicOnPricesByStationId) {
        this.historicOnPricesByStationId = historicOnPricesByStationId;
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<HistoricLpgPricesEntity> getHistoricLpgPricesByStationId() {
        return historicLpgPricesByStationId;
    }

    public void setHistoricLpgPricesByStationId(Collection<HistoricLpgPricesEntity> historicLpgPricesByStationId) {
        this.historicLpgPricesByStationId = historicLpgPricesByStationId;
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<RatingsEntity> getRatingsByUserId() {
        return ratingsByUserId;
    }

    public void setRatingsByUserId(Collection<RatingsEntity> ratingsByUserId) {
        this.ratingsByUserId = ratingsByUserId;
    }
}