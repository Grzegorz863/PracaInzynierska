package pl.tcps.tcps.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.willy.ratingbar.BaseRatingBar;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

import pl.tcps.tcps.R;
import pl.tcps.tcps.api_client.PetrolStationClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.fragment.RatingDialog;
import pl.tcps.tcps.fragment.UpdatePricesDialog;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.responses.AddressResponse;
import pl.tcps.tcps.pojo.responses.GeoLocationResponse;
import pl.tcps.tcps.pojo.responses.PetrolStationSpecificInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StationDetailsActivity extends AppCompatActivity {

    private AccessTokenDetails accessTokenDetails;
    private Context context;
    private Double distance;
    private Long chosenStationId;
    private Integer recycleViewStationIndex;

    private TextView tvStationName;
    private ImageView ivAddressSymbol;
    private TextView tvStationAddress;
    private TextView tvCurrentPb95Price;
    private TextView tvCurrentPb98Price;
    private TextView tvCurrentOnPrice;
    private TextView tvCurrentLpgPrice;
    private TextView tvConsortiumName;
    private TextView tvDistance;
    private TextView tvPriceDateUpdate;
    private TextView tvHistoricPb95Price;
    private TextView tvHistoricPb98Price;
    private TextView tvHistoricOnPrice;
    private TextView tvHistoricLpgPrice;
    private ImageView ivHasFood;
    private BaseRatingBar rbRating;
    private TextView tvDescription;
    private Button bMakeRate;
    private Button bLeadMe;
    private Button bUpdatePrices;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
           actionBar.setDisplayHomeAsUpEnabled(true);
           actionBar.setTitle(R.string.action_bar_station_details);
        }
        bindViews();
        context = this;
        Intent intent = getIntent();
        accessTokenDetails = intent.getParcelableExtra(getString(R.string.key_access_token_details));
        chosenStationId = intent.getLongExtra(getString(R.string.key_station_id), 0);
        distance = intent.getDoubleExtra(getString(R.string.key_distance), 0);
        recycleViewStationIndex = intent.getIntExtra(getString(R.string.key_recycleview_station_index), -1);

        refreshActivityContent();
        setOnClickListenerForMakeRateButton(chosenStationId);
        setOnClickListenerForLeadMeButton();
        setOnClickListenerForUpdatePricesButton(chosenStationId);
        setOnClickListenerForAddressTextViewAndImageView(chosenStationId);

    }

    public void refreshActivityContent(){
        startProgressBar();
        Retrofit retrofit = RetrofitBuilder.createRetrofit(this);
        PetrolStationClient petrolStationClient = retrofit.create(PetrolStationClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<PetrolStationSpecificInfoResponse> call = petrolStationClient.getPetrolStationSpecificInfo(authHeader, chosenStationId);
        call.enqueue(new Callback<PetrolStationSpecificInfoResponse>() {
            @Override
            public void onResponse(Call<PetrolStationSpecificInfoResponse> call, Response<PetrolStationSpecificInfoResponse> response) {
                PetrolStationSpecificInfoResponse specificInfo = response.body();
                if (response.isSuccessful() && specificInfo != null) {
                    fillViews(specificInfo, distance);
                    stopProgressBar();
                } else {
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                        Toast.makeText(context, "No station was found!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Error on server", Toast.LENGTH_SHORT).show();
                    stopProgressBar();
                }
            }

            @Override
            public void onFailure(Call<PetrolStationSpecificInfoResponse> call, Throwable t) {
                stopProgressBar();
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fillViews(PetrolStationSpecificInfoResponse specificInfo, Double distance) {
        tvStationName.setText(specificInfo.getStationName());

        AddressResponse address = specificInfo.getStationAddress();
        tvStationAddress.setText(getString(R.string.station_details_station_address, address.getStreet(), address.getApartmentNumber(), address.getCity()));

        String currentPb95Price = String.format(Locale.ENGLISH,"%.2f", specificInfo.getCurrentPetrolPricesResponse().getPb95Price());
        tvCurrentPb95Price.setText(getString(R.string.recycleview_pb95_text_value_price_par, currentPb95Price));

        String currentPb98Price = String.format(Locale.ENGLISH,"%.2f", specificInfo.getCurrentPetrolPricesResponse().getPb98Price());
        tvCurrentPb98Price.setText(getString(R.string.recycleview_pb98_text_value_price_par, currentPb98Price));

        String currentOnPrice = String.format(Locale.ENGLISH,"%.2f", specificInfo.getCurrentPetrolPricesResponse().getOnPrice());
        tvCurrentOnPrice.setText(getString(R.string.recycleview_on_text_value_price_par, currentOnPrice));

        String currentLpgPrice = String.format(Locale.ENGLISH,"%.2f", specificInfo.getCurrentPetrolPricesResponse().getLpgPrice());
        tvCurrentLpgPrice.setText(getString(R.string.recycleview_lpg_text_value_price_par, currentLpgPrice));

        tvConsortiumName.setText(specificInfo.getConsortiumName());

        tvDistance.setText(prepareDistanceText(distance));

        tvPriceDateUpdate.setText(countTimeFromLastUp(specificInfo.getCurrentPetrolPricesResponse().getInsertDate()));

        String historicPb95Price = String.format(Locale.ENGLISH,"%.2f", specificInfo.getHistoricPetrolPricesResponse().getPb95Price());
        tvHistoricPb95Price.setText(getString(R.string.recycleview_pb95_text_value_price_par, historicPb95Price));

        String historicPb98Price = String.format(Locale.ENGLISH,"%.2f", specificInfo.getHistoricPetrolPricesResponse().getPb98Price());
        tvHistoricPb98Price.setText(getString(R.string.recycleview_pb98_text_value_price_par, historicPb98Price));

        String historicOnPrice = String.format(Locale.ENGLISH,"%.2f", specificInfo.getHistoricPetrolPricesResponse().getOnPrice());
        tvHistoricOnPrice.setText(getString(R.string.recycleview_on_text_value_price_par, historicOnPrice));

        String historicLpgPrice = String.format(Locale.ENGLISH,"%.2f", specificInfo.getHistoricPetrolPricesResponse().getLpgPrice());
        tvHistoricLpgPrice.setText(getString(R.string.recycleview_lpg_text_value_price_par, historicLpgPrice));

        ivHasFood.setImageResource(chooseImageResource(specificInfo.getHasFood()));

        rbRating.setRating(specificInfo.getRating().floatValue());

        tvDescription.setText(specificInfo.getDescription());

        View view = findViewById(R.id.station_details_root);
        view.setVisibility(View.VISIBLE);
    }

    private void setOnClickListenerForAddressTextViewAndImageView(Long chosenStationId){
        tvStationAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPetrolStationCoordinates(false);
            }
        });

        ivAddressSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPetrolStationCoordinates(false);
            }
        });
    }

    private void setOnClickListenerForMakeRateButton(Long chosenStationId) {
        bMakeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingDialog ratingDialog = new RatingDialog();
                Bundle args = new Bundle();
                args.putParcelable(getString(R.string.key_access_token_details), accessTokenDetails);
                args.putLong(getString(R.string.key_station_id), chosenStationId);
                ratingDialog.setArguments(args);
                ratingDialog.show(getSupportFragmentManager(), "rating_dialog");
            }
        });
    }

    private void setOnClickListenerForLeadMeButton() {
        bLeadMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPetrolStationCoordinates(true);
            }
        });
    }

    private void setOnClickListenerForUpdatePricesButton(Long chosenStationId) {
        bUpdatePrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePricesDialog updatePricesDialog = new UpdatePricesDialog();
                Bundle args = new Bundle();
                args.putParcelable(getString(R.string.key_access_token_details), accessTokenDetails);
                args.putLong(getString(R.string.key_station_id), chosenStationId);
                updatePricesDialog.setArguments(args);
                updatePricesDialog.show(getSupportFragmentManager(), "update_prices_dialog");
            }
        });
    }

    private void stopProgressBar(){
        View view = findViewById(R.id.station_details_root);
        view.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void startProgressBar(){
        View view = findViewById(R.id.station_details_root);
        view.setVisibility(View.INVISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void findPetrolStationCoordinates(Boolean isNavigation) {
        startProgressBar();
        Retrofit retrofit = RetrofitBuilder.createRetrofit(context);
        PetrolStationClient petrolStationClient = retrofit.create(PetrolStationClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<GeoLocationResponse> call = petrolStationClient.getPetrolStationGeoLocation(authHeader, chosenStationId);
        call.enqueue(new Callback<GeoLocationResponse>() {
            @Override
            public void onResponse(Call<GeoLocationResponse> call, Response<GeoLocationResponse> response) {
                GeoLocationResponse geoLocation = response.body();
                stopProgressBar();
                if(response.isSuccessful() && geoLocation != null){
                    if(isNavigation)
                        startGoogleMaps(String.format(Locale.ENGLISH, "google.navigation:q=%s,%s",
                                geoLocation.getLatitude().toString(), geoLocation.getLongitude().toString()));
                    else
                        startGoogleMaps(String.format(Locale.ENGLISH, "geo:0,0?q=%s,%s(%s)",
                                geoLocation.getLatitude().toString(), geoLocation.getLongitude().toString(), geoLocation.getStationName()));
                }else
                if(response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                    Toast.makeText(context, "No station was found!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Error on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GeoLocationResponse> call, Throwable t) {
                stopProgressBar();
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startGoogleMaps(String uriString){
        Uri uri = Uri.parse(uriString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null){
            mapIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mapIntent);
        }else
            Toast.makeText(context, "Please, install Google Maps!", Toast.LENGTH_LONG).show();

    }

    private String countTimeFromLastUp(ZonedDateTime insertDate) {

        Period periodFromLastUp = Period.between(insertDate.toLocalDate(), ZonedDateTime.now().toLocalDate());

        if (insertDate.toInstant().equals(Instant.EPOCH))
            return "Updated: never";

        if(periodFromLastUp.getYears() != 0)
            return getString(R.string.station_details_last_update, String.valueOf(periodFromLastUp.getYears()), "years");

        if(periodFromLastUp.getMonths() != 0)
            return getString(R.string.station_details_last_update, String.valueOf(periodFromLastUp.getMonths()), "months");

        if(periodFromLastUp.getDays() !=0 )
            return getString(R.string.station_details_last_update, String.valueOf(periodFromLastUp.getDays()), "days");

        Duration durationFromLastUp = Duration.between(insertDate, ZonedDateTime.now(ZoneId.systemDefault()));
        Long secondsFromLastUp = durationFromLastUp.getSeconds();
        Long hoursFromLastUp = secondsFromLastUp/3600;

        if (hoursFromLastUp != 0)
            return getString(R.string.station_details_last_update, hoursFromLastUp.toString(), "hours");

        Long minutesFromLastUp = secondsFromLastUp/60;
        if (minutesFromLastUp != 0)
            return getString(R.string.station_details_last_update, minutesFromLastUp.toString(), "minutes");

        if(secondsFromLastUp < 0)
            return getString(R.string.station_details_last_update, String.valueOf(0), "seconds");

        if(secondsFromLastUp != 0)
            return getString(R.string.station_details_last_update, secondsFromLastUp.toString(), "seconds");

        return getString(R.string.station_details_last_update, String.valueOf(0), "seconds");
    }

    private Integer chooseImageResource(Boolean hasFood) {
        if (hasFood)
            return R.drawable.ic_is_food_black_24dp;
        else
            return R.drawable.ic_no_food_black_24dp;
    }

    private String prepareDistanceText(Double distance) {
        String distanceTextToShow;
        String distanceText;
        if(distance < 1000) {
            distanceText = String.format(Locale.ENGLISH,"%.2f", distance);
            distanceTextToShow = getString(R.string.recycleview_distance, distanceText, "m");
        }else{
            distance = distance/1000;
            distanceText = String.format(Locale.ENGLISH,"%.2f", distance);
            distanceTextToShow = getString(R.string.recycleview_distance, distanceText, "km");
        }

        return distanceTextToShow;
    }

    private void bindViews() {
        tvStationName = findViewById(R.id.station_details_station_name);
        ivAddressSymbol = findViewById(R.id.station_details_location_symbol);
        tvStationAddress = findViewById(R.id.station_details_address);
        tvCurrentPb95Price = findViewById(R.id.station_details_pb95_price);
        tvCurrentPb98Price = findViewById(R.id.station_details_pb98_price);
        tvCurrentOnPrice = findViewById(R.id.station_details_on_price);
        tvCurrentLpgPrice = findViewById(R.id.station_details_lpg_price);
        tvConsortiumName = findViewById(R.id.station_details_consortium_name);
        tvDistance = findViewById(R.id.station_details_distance);
        tvPriceDateUpdate = findViewById(R.id.station_details_last_update_ago);
        tvHistoricPb95Price = findViewById(R.id.station_details_pb95_price_historic);
        tvHistoricPb98Price = findViewById(R.id.station_details_pb98_price_historic);
        tvHistoricOnPrice = findViewById(R.id.station_details_on_price_historic);
        tvHistoricLpgPrice = findViewById(R.id.station_details_lpg_price_historic);
        ivHasFood = findViewById(R.id.station_details_has_food_symbol);
        rbRating = findViewById(R.id.station_details_rating_bar);
        tvDescription = findViewById(R.id.station_details_description);
        bMakeRate = findViewById(R.id.station_details_rate_button);
        bLeadMe = findViewById(R.id.station_details_maps_button);
        bUpdatePrices = findViewById(R.id.station_details_update_prices_button);
        progressBar = findViewById(R.id.station_details_progress_bar);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.key_recycleview_station_index), recycleViewStationIndex);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.station_details_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.station_details_refresh:
                refreshActivityContent();
                return true;

            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.key_recycleview_station_index), recycleViewStationIndex);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
