package pl.tcps.dbEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "tcpsdb", catalog = "")
public class UsersEntity {
    private long userId;
    private String userPassword;
    private String userName;
    private String userRole;
    private Boolean isEnabled;
    private String firstName;
    private String lastName;
    private String email;

    @JsonIgnore
    private Collection<HistoricPricesEntity> historicPricesByUserId;

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
    public String getUserFirstName() {
        return firstName;
    }

    public void setUserFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = true, length = 45)
    public String getUserLastName() {
        return lastName;
    }

    public void setUserLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 45)
    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String email) {
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
                Objects.equals(isEnabled, that.isEnabled);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, userPassword, userName, userRole, isEnabled);
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<HistoricPricesEntity> getHistoricPricesByUserId() {
        return historicPricesByUserId;
    }

    public void setHistoricPricesByUserId(Collection<HistoricPricesEntity> historicPricesByUserId) {
        this.historicPricesByUserId = historicPricesByUserId;
    }

    @OneToMany(mappedBy = "usersByUserId")
    public Collection<RatingsEntity> getRatingsByUserId() {
        return ratingsByUserId;
    }

    public void setRatingsByUserId(Collection<RatingsEntity> ratingsByUserId) {
        this.ratingsByUserId = ratingsByUserId;
    }
}