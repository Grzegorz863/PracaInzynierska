package pl.tcps.tcps.api_client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ConsortiumsClient {

    @GET("consortiums/id/{consortiumId}")
    Call<String> getConsortium(@Header("Authorization") String accessToken, @Path("consortiumId") Integer consortiumId);
}
