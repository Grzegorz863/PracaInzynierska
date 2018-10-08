package pl.tcps.tcps.api_client;

import java.util.Collection;

import pl.tcps.tcps.pojo.Consortium;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ConsortiumClient {

    @GET("consortiums/id/{consortiumId}")
    Call<String> getConsortium(@Header("Authorization") String accessToken, @Path("consortiumId") Integer consortiumId);

    @GET("consortiums")
    Call<Collection<Consortium>> getAllConsortiums(@Header("Authorization") String accessToken);
}
