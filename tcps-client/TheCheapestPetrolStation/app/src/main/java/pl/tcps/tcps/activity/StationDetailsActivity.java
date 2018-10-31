package pl.tcps.tcps.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import pl.tcps.tcps.R;
import pl.tcps.tcps.api_client.PetrolStationClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.fragment.RatingDialog;
import pl.tcps.tcps.fragment.UpdatePricesDialog;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.responses.AddressResponse;
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

    private TextView tvStationName;
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
    private RatingBar rbRating;
    private TextView tvDescription;
    private Button bMakeRate;
    private Button bLeadMe;
    private Button bUpdatePrices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_details);
        bindViews();
        context = this;
        Intent intent = getIntent();
        accessTokenDetails = intent.getParcelableExtra(getString(R.string.key_access_token_details));
        chosenStationId = intent.getLongExtra(getString(R.string.key_station_id), 0);
        distance = intent.getDoubleExtra(getString(R.string.key_distance), 0);

        refreshActivityContent();
        setOnClickListenerForMakeRateButton(chosenStationId);
        setOnClickListenerForLeadMeButton();
        setOnClickListenerForUpdatePricesButton(chosenStationId);

    }

    public void refreshActivityContent(){
        Retrofit retrofit = RetrofitBuilder.createRetrofit(this);
        PetrolStationClient petrolStationClient = retrofit.create(PetrolStationClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<PetrolStationSpecificInfoResponse> call = petrolStationClient.getPetrolStationSpecyficInfo(authHeader, chosenStationId);
        call.enqueue(new Callback<PetrolStationSpecificInfoResponse>() {
            @Override
            public void onResponse(Call<PetrolStationSpecificInfoResponse> call, Response<PetrolStationSpecificInfoResponse> response) {
                PetrolStationSpecificInfoResponse specificInfo = response.body();
                if (response.isSuccessful() && specificInfo != null) {
                    fillViews(specificInfo, distance);
                } else
                if(response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                    Toast.makeText(context, "No station was found!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Error on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PetrolStationSpecificInfoResponse> call, Throwable t) {
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
    }

}
