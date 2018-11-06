package pl.tcps.tcps.api_client;

import java.util.Map;

import pl.tcps.tcps.pojo.responses.UserDetailsResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserClient {

    @GET("users/me/info")
    Call<UserDetailsResponse> getLoggedUserDetails(@Header("Authorization") String accessToken);

    @PUT("users/me/password")
    Call<Void> changeUserPassword(@Header("Authorization") String accessToken,
                                  @HeaderMap Map<String, String> headerMap);

    @DELETE("users/me")
    Call<Void> deleteUser(@Header("Authorization") String accessToken, @Header("password") String password);
}
