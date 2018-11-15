package pl.tcps.tcps.api_client;

import java.util.List;
import java.util.Map;

import pl.tcps.tcps.pojo.responses.CreatePetrolStationResponse;
import pl.tcps.tcps.pojo.PetrolStationRecycleViewItem;
import pl.tcps.tcps.pojo.responses.GeoLocationResponse;
import pl.tcps.tcps.pojo.responses.PetrolStationMapMarker;
import pl.tcps.tcps.pojo.responses.PetrolStationReloadRecycleViewResponse;
import pl.tcps.tcps.pojo.responses.PetrolStationSpecificInfoResponse;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface PetrolStationClient {

    @FormUrlEncoded
    @POST("station")
    Call<CreatePetrolStationResponse> createPetrolStation(@Header("Authorization") String accessToken, @FieldMap Map<String, Object> name);

    @GET("station/distance/{latitude}/{longitude}/{distance}")
    Call<List<PetrolStationRecycleViewItem>> findPetrolStationByDistance(@Header("Authorization") String accessToken,
                                                                         @Path("latitude") Double latitude,
                                                                         @Path("longitude") Double longitude,
                                                                         @Path("distance") Double distance);

    @GET("station/{station_id}/details")
    Call<PetrolStationSpecificInfoResponse> getPetrolStationSpecificInfo(@Header("Authorization") String accessToken,
                                                                         @Path("station_id") Long stationId);

    @GET("station/{station_id}/location")
    Call<GeoLocationResponse> getPetrolStationGeoLocation(@Header("Authorization") String accessToken,
                                                          @Path("station_id") Long stationId);

    @GET("station/map/{latitude}/{longitude}/{distance}")
    Call<List<PetrolStationMapMarker>> findPetrolStationByDistanceForMap(@Header("Authorization") String accessToken,
                                                                         @Path("latitude") Double latitude,
                                                                         @Path("longitude") Double longitude,
                                                                         @Path("distance") Double distance);

    @GET("station/reload/{latitude}/{longitude}")
    Call<List<PetrolStationReloadRecycleViewResponse>> reloadSpecificPetrolStations(@Header("Authorization") String accessToken,
                                                                                    @Path("latitude") Double latitude,
                                                                                    @Path("longitude") Double longitude,
                                                                                    @Query("stations_id") List<Long> stationsId);

}
