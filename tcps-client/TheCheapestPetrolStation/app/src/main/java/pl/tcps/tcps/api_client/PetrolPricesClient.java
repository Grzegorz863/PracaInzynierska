package pl.tcps.tcps.api_client;

import java.util.Map;

import pl.tcps.tcps.pojo.responses.PetrolPricesResponse;
import pl.tcps.tcps.pojo.responses.StationRatingResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PetrolPricesClient {

    @GET("station/{stationId}/prices")
    Call<PetrolPricesResponse> getPetrolPrice(@Header("Authorization") String accessToken,
                                                          @Path("stationId") Long stationId);

    @FormUrlEncoded
    @POST("station/prices")
    Call<PetrolPricesResponse> createPetrolPrice(@Header("Authorization") String accessToken, @FieldMap Map<String, Object> body);

    @FormUrlEncoded
    @PUT("station/prices")
    Call<PetrolPricesResponse> updatePetrolPrice(@Header("Authorization") String accessToken, @FieldMap Map<String, Object> body);
}
