package pl.tcps.tcps.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.StationDetailsActivity;
import pl.tcps.tcps.api_client.RatingClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.responses.StationRatingResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RatingDialog extends AppCompatDialogFragment {

    private Boolean userRatedStation = false;
    private StationDetailsActivity activity;

    private ProgressBar progressBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        activity = (StationDetailsActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog_layout, null);
        progressBar = view.findViewById(R.id.add_rate_progress_bar);
        startProgressBar();
        RatingBar stationRate = view.findViewById(R.id.add_rate_rating_bar);

        Long stationId = getArguments().getLong(getString(R.string.key_station_id));
        AccessTokenDetails accessTokenDetails = getArguments().getParcelable(getString(R.string.key_access_token_details));

        Retrofit retrofit = RetrofitBuilder.createRetrofit(activity);
        RatingClient ratingClient = retrofit.create(RatingClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<Double> call = ratingClient.getStationRatingForOneUser(authHeader, stationId);
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                Double rate = response.body();
                if(response.isSuccessful() && rate!=null){
                    stationRate.setRating(rate.floatValue());
                    userRatedStation = true;
                }else
                    if(response.code() != HttpURLConnection.HTTP_NOT_FOUND)
                        Toast.makeText(activity, "Error on server", Toast.LENGTH_SHORT).show();

                stopProgressBar();
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                stopProgressBar();
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        stationRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser && rating<1f)
                    ratingBar.setRating(1f);
            }
        });

        builder.setView(view)
                .setTitle("Rate Station")
                .setCancelable(true)
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startProgressBar();
                        if(userRatedStation)
                            updateRate(authHeader, ratingClient, stationId, stationRate.getRating());
                        else
                            createRate(authHeader, ratingClient, stationId, stationRate.getRating());
                    }
                });

        return builder.create();
    }

    private void createRate(String authHeader, RatingClient ratingClient, Long stationId, float rating) {
        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("station_id", stationId);
        bodyData.put("rate", rating);
        Call<StationRatingResponse> call = ratingClient.createStationRating(authHeader, bodyData);
        call.enqueue(new Callback<StationRatingResponse>() {
            @Override
            public void onResponse(Call<StationRatingResponse> call, Response<StationRatingResponse> response) {
                StationRatingResponse stationRatingResponse = response.body();
                stopProgressBar();
                if(response.isSuccessful() && stationRatingResponse != null){
                    activity.refreshActivityContent();
                    Toast.makeText(activity, "You rated station first time on: " + stationRatingResponse.getRate(), Toast.LENGTH_SHORT).show();
                }else
                    if(response.code() == HttpURLConnection.HTTP_SEE_OTHER)
                        Toast.makeText(activity, "You already rated this station!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(activity, "Error on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StationRatingResponse> call, Throwable t) {
                stopProgressBar();
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRate(String authHeader, RatingClient ratingClient, Long stationId, float rating) {
        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("station_id", stationId);
        bodyData.put("new_rate", rating);
        Call<StationRatingResponse> call = ratingClient.updateStationRating(authHeader, bodyData);
        call.enqueue(new Callback<StationRatingResponse>() {
            @Override
            public void onResponse(Call<StationRatingResponse> call, Response<StationRatingResponse> response) {
                stopProgressBar();
                if(response.isSuccessful()) {
                    if (response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                        activity.refreshActivityContent();
                        Toast.makeText(activity, "You updated rate on: " + rating, Toast.LENGTH_SHORT).show();
                    }
                }else
                    if(response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                        Toast.makeText(activity, "You did not rate this station yet!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(activity, "Error on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<StationRatingResponse> call, Throwable t) {
                stopProgressBar();
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stopProgressBar(){
        progressBar.setVisibility(View.GONE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void startProgressBar(){
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
    }
}
