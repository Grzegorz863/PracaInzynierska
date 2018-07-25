package pl.tcps.dbEntities;

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
    private Collection<HistoricPricesEntity> historicPricesByUserId;
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
    @Column(name = "user_password", nullable = false, length = 60)
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Basic
    @Column(name = "user_name", nullable = false, length = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_role", nullable = false, length = 25)
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return userId == that.userId &&
                Objects.equals(userPassword, that.userPassword) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(userRole, that.userRole);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, userPassword, userName, userRole);
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
