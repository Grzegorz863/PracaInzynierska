package pl.tcps.tcps.api_client;

import java.util.Map;

import okhttp3.ResponseBody;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.login.LoginUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginClient {

    @FormUrlEncoded
    @Headers({"Authorization: Basic YW5kcm9pZF9hcHA6dGhlX2NoZWFwZXN0X3BldHJvbF9zdGF0aW9u"})
    @POST("oauth/token")
    Call<AccessTokenDetails> accessToken(@FieldMap Map<String, Object> parametersMap);
}
