package pl.tcps.dbEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "consortiums", schema = "tcpsdb", catalog = "")
public class ConsortiumsEntity {

    @JsonProperty("consortium_id")
    private long consortiumId;

    @JsonProperty("consortium_name")
    private String consortiumName;

    @JsonProperty("logo")
    private String logo;

    @JsonIgnore
    private Collection<PetrolStationEntity> petrolStationsByConsortiumId;

    @Id
    @Column(name = "consortium_id", nullable = false)
    public long getConsortiumId() {
        return consortiumId;
    }

    public void setConsortiumId(long consortiumId) {
        this.consortiumId = consortiumId;
    }

    @Basic
    @Column(name = "consortium_name", nullable = false, length = 255)
    public String getConsortiumName() {
        return consortiumName;
    }

    public void setConsortiumName(String consortiumName) {
        this.consortiumName = consortiumName;
    }

    @Basic
    @Column(name = "logo", nullable = true, length = 255)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsortiumsEntity that = (ConsortiumsEntity) o;
        return consortiumId == that.consortiumId &&
                Objects.equals(consortiumName, that.consortiumName) &&
                Objects.equals(logo, that.logo);
    }

    @Override
    public int hashCode() {

        return Objects.hash(consortiumId, consortiumName, logo);
    }

    @OneToMany(mappedBy = "consortiumsByConsortiumId")
    public Collection<PetrolStationEntity> getPetrolStationsByConsortiumId() {
        return petrolStationsByConsortiumId;
    }

    public void setPetrolStationsByConsortiumId(Collection<PetrolStationEntity> petrolStationsByConsortiumId) {
        this.petrolStationsByConsortiumId = petrolStationsByConsortiumId;
    }
}
