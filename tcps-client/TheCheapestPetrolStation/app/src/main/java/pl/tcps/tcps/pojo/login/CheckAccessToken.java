package pl.tcps.tcps.pojo.login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckAccessToken {

    @SerializedName("aud")
    private List<String> aud = null;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("scope")
    private List<String> scope = null;

    @SerializedName("active")
    private Boolean active;

    @SerializedName("exp")
    private Integer exp;

    @SerializedName("authorities")
    private List<String> authorities = null;

    @SerializedName("client_id")
    private String clientId;

    public CheckAccessToken() {
    }

    public CheckAccessToken(List<String> aud, String userName, List<String> scope, Boolean active,
                            Integer exp, List<String> authorities, String clientId) {
        this.aud = aud;
        this.userName = userName;
        this.scope = scope;
        this.active = active;
        this.exp = exp;
        this.authorities = authorities;
        this.clientId = clientId;
    }

    public List<String> getAud() {
        return aud;
    }

    public void setAud(List<String> aud) {
        this.aud = aud;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
