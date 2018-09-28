package pl.tcps.tcps.pojo.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class AccessTokenDetails implements Parcelable{

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName("expires_in")
    private Integer expiresIn;

    @SerializedName("scope")
    private String scope;

    public static final Parcelable.Creator<AccessTokenDetails> CREATOR = new Parcelable.Creator<AccessTokenDetails>(){

        @Override
        public AccessTokenDetails createFromParcel(Parcel source) {
            return new AccessTokenDetails(source);
        }

        @Override
        public AccessTokenDetails[] newArray(int size) {
            return new AccessTokenDetails[0];
        }
    };

    public AccessTokenDetails(Parcel parcel) {
        this.accessToken = parcel.readString();
        this.tokenType = parcel.readString();
        this.refreshToken = parcel.readString();
        this.expiresIn = parcel.readInt();
        this.scope = parcel.readString();
    }

    public AccessTokenDetails(String accessToken, String tokenType, String refreshToken, Integer expiresIn, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeString(tokenType);
        dest.writeString(refreshToken);
        dest.writeInt(expiresIn);
        dest.writeString(scope);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
