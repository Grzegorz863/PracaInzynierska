package pl.tcps.tcps.api_client;


import java.util.Map;

import pl.tcps.tcps.pojo.responses.CreatePetrolStationResponse;
import pl.tcps.tcps.pojo.responses.StationRatingResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RatingClient {

    @GET("station/{stationId}/rating")
    Call<Double> getStationRatingForOneUser(@Header("Authorization") String accessToken,
                                            @Path("stationId") Long stationId);

    @FormUrlEncoded
    @POST("station/rating")
    Call<StationRatingResponse> createStationRating(@Header("Authorization") String accessToken, @FieldMap Map<String, Object> name);

    @FormUrlEncoded
    @PUT("station/rating")
    Call<StationRatingResponse> updateStationRating(@Header("Authorization") String accessToken, @FieldMap Map<String, Object> name);
}
