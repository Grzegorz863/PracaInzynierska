package pl.tcps.tcps.pojo.login;

import com.google.gson.annotations.SerializedName;

public class LoginUser {

    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("username")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("scope")
    private String scope;

    public LoginUser(String grantType, String userName, String password, String scope) {
        this.grantType = grantType;
        this.userName = userName;
        this.password = password;
        this.scope = scope;
    }

    public LoginUser() {
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
