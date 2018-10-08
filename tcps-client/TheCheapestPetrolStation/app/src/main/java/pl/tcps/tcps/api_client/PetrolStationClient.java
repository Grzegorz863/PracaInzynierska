package pl.tcps.tcps.api_client;

import java.util.Map;

import pl.tcps.tcps.pojo.PetrolStationResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PetrolStationClient {

    @FormUrlEncoded
    @POST("/station")
    Call<PetrolStationResponse> createPetrolStation(@Header("Authorization") String accessToken, @FieldMap Map<String, Object> name);
}
