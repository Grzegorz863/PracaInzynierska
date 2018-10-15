package pl.tcps.tcps.api_client;

import java.util.Collection;
import java.util.Map;

import pl.tcps.tcps.pojo.PetrolStationResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface PetrolStationClient {

    @FormUrlEncoded
    @POST("station")
    Call<PetrolStationResponse> createPetrolStation(@Header("Authorization") String accessToken, @FieldMap Map<String, Object> name);

    @GET("station/distance/{latitude}/{longitude}/{distance}")
    Call<Collection<PetrolStationResponse>> findPetrolStationByDistance(@Header("Authorization") String accessToken,
                                                                        @Path("latitude") Double latitude,
                                                                        @Path("longitude") Double longitude,
                                                                        @Path("distance") Double distance);
}
