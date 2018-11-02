package pl.tcps.tcps.api_client;

import pl.tcps.tcps.pojo.responses.UserDetailsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UserClient {

    @GET("users/me/info")
    Call<UserDetailsResponse> getLoggedUserDetails(@Header("Authorization") String accessToken);
}
