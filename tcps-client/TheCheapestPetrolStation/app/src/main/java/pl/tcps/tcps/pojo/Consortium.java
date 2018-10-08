package pl.tcps.tcps.pojo;

import com.google.gson.annotations.SerializedName;

public class Consortium {

    @SerializedName("consortium_id")
    private long consortiumId;

    @SerializedName("consortium_name")
    private String consortiumName;

    @SerializedName("logo")
    private String logo;

    public Consortium(long consortiumId, String consortiumName, String logo) {
        this.consortiumId = consortiumId;
        this.consortiumName = consortiumName;
        this.logo = logo;
    }

    public Consortium() {
    }

    public long getConsortiumId() {
        return consortiumId;
    }

    public void setConsortiumId(long consortiumId) {
        this.consortiumId = consortiumId;
    }

    public String getConsortiumName() {
        return consortiumName;
    }

    public void setConsortiumName(String consortiumName) {
        this.consortiumName = consortiumName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
