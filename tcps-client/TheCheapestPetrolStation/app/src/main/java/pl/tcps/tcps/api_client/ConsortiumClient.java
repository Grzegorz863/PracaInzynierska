package pl.tcps.tcps.api_client;

import java.util.Collection;

import pl.tcps.tcps.pojo.responses.ConsortiumResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ConsortiumClient {

    @GET("consortiums")
    Call<Collection<ConsortiumResponse>> getAllConsortiums(@Header("Authorization") String accessToken);
}
