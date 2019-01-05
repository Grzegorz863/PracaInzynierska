package pl.tcps.tcps.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.StationDetailsActivity;
import pl.tcps.tcps.api_client.PetrolPricesClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.filters.UpdatePriceInputFilter;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.responses.PetrolPricesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdatePricesDialog extends AppCompatDialogFragment {

    private StationDetailsActivity activity;
    private EditText etPb95Price;
    private EditText etPb98Price;
    private EditText etOnPrice;
    private EditText etLpgPrice;
    private ProgressBar progressBar;

    private Boolean stationHasPetrolPrice = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        activity = (StationDetailsActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.update_prices_dialog_layout, null);
        bindViews(view);
        startProgressBar();

        Long stationId = getArguments().getLong(getString(R.string.key_station_id));
        AccessTokenDetails accessTokenDetails = getArguments().getParcelable(getString(R.string.key_access_token_details));

        Retrofit retrofit = RetrofitBuilder.createRetrofit(activity);
        PetrolPricesClient petrolPricesClient = retrofit.create(PetrolPricesClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<PetrolPricesResponse> call = petrolPricesClient.getPetrolPrice(authHeader, stationId);
        call.enqueue(new Callback<PetrolPricesResponse>() {
            @Override
            public void onResponse(Call<PetrolPricesResponse> call, Response<PetrolPricesResponse> response) {
                PetrolPricesResponse petrolPricesResponse = response.body();
                if (response.isSuccessful() && petrolPricesResponse != null) {
                    fillViews(petrolPricesResponse);
                    stationHasPetrolPrice = existPetrolPrices(petrolPricesResponse);
                    stopProgressBar();
                } else {
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                        fillViews(new PetrolPricesResponse(0f, 0f, 0f, 0f));
                    else
                        Toast.makeText(activity, "Error on server", Toast.LENGTH_SHORT).show();

                    stopProgressBar();
                }
            }

            @Override
            public void onFailure(Call<PetrolPricesResponse> call, Throwable t) {
                stopProgressBar();
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view)
                .setTitle("Update petrol prices")
                .setCancelable(true)
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (stationHasPetrolPrice)
                            updatePetrolPrices(authHeader, petrolPricesClient, stationId);
                        else
                            createPetrolPrices(authHeader, petrolPricesClient, stationId);
                    }
                });


        return builder.create();
    }

    private boolean existPetrolPrices(PetrolPricesResponse petrolPricesResponse) {
        return petrolPricesResponse.getPb95Price() != 0 || petrolPricesResponse.getPb98Price() != 0 ||
                petrolPricesResponse.getOnPrice() != 0 || petrolPricesResponse.getLpgPrice() != 0;
    }

    private void createPetrolPrices(String authHeader, PetrolPricesClient petrolPricesClient, Long stationId) {

        Map<String, Object> bodyData = createBodyData(stationId);
        if (bodyData == null)
            return;

        startProgressBar();
        Call<PetrolPricesResponse> call = petrolPricesClient.createPetrolPrice(authHeader, bodyData);
        call.enqueue(new Callback<PetrolPricesResponse>() {
            @Override
            public void onResponse(Call<PetrolPricesResponse> call, Response<PetrolPricesResponse> response) {
                PetrolPricesResponse petrolPricesResponse = response.body();
                stopProgressBar();
                if (response.isSuccessful() && petrolPricesResponse != null) {
                    activity.refreshActivityContent();
                    Toast.makeText(activity, "Prices have been added!", Toast.LENGTH_SHORT).show();
                } else if (response.code() == HttpURLConnection.HTTP_SEE_OTHER)
                    Toast.makeText(activity, "Petrol at this station already has prices", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity, "Error on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PetrolPricesResponse> call, Throwable t) {
                stopProgressBar();
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePetrolPrices(String authHeader, PetrolPricesClient petrolPricesClient, Long stationId) {

        Map<String, Object> bodyData = createBodyData(stationId);
        if (bodyData == null)
            return;

        startProgressBar();
        Call<PetrolPricesResponse> call = petrolPricesClient.updatePetrolPrice(authHeader, bodyData);
        call.enqueue(new Callback<PetrolPricesResponse>() {
            @Override
            public void onResponse(Call<PetrolPricesResponse> call, Response<PetrolPricesResponse> response) {
                stopProgressBar();
                if (response.isSuccessful()) {
                    if (response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                        activity.refreshActivityContent();
                        Toast.makeText(activity, "Prices have been updated!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                        Toast.makeText(activity, "This station did not have petrol prices yet!", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(activity, "Error on server", Toast.LENGTH_SHORT).show();

                    if (response.code() == 422) //UNPROCESSABLE_ENTIcTY
                        Toast.makeText(activity, "Enter at least one price!", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(activity, "Error on server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PetrolPricesResponse> call, Throwable t) {
                stopProgressBar();
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String, Object> createBodyData(Long stationId) {

        Map<String, Object> bodyData = new HashMap<>();
        bodyData.put("station_id", stationId);

        String pb95Price = etPb95Price.getText().toString();
        String pb98Price = etPb98Price.getText().toString();
        String onPrice = etOnPrice.getText().toString();
        String lpgPrice = etLpgPrice.getText().toString();

        if (TextUtils.isEmpty(pb95Price) && TextUtils.isEmpty(pb98Price) && TextUtils.isEmpty(onPrice) && TextUtils.isEmpty(lpgPrice))
            return null;

        if (!TextUtils.isEmpty(pb95Price))
            bodyData.put("pb95_price", pb95Price);

        if (!TextUtils.isEmpty(pb98Price))
            bodyData.put("pb98_price", pb98Price);

        if (!TextUtils.isEmpty(onPrice))
            bodyData.put("on_price", onPrice);

        if (!TextUtils.isEmpty(lpgPrice))
            bodyData.put("lpg_price", lpgPrice);

        return bodyData;
    }

    private void stopProgressBar() {
        progressBar.setVisibility(View.GONE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void startProgressBar() {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void fillViews(PetrolPricesResponse prices) {
        etPb95Price.setHint(prices.getPb95Price().toString());
        etPb98Price.setHint(prices.getPb98Price().toString());
        etOnPrice.setHint(prices.getOnPrice().toString());
        etLpgPrice.setHint(prices.getLpgPrice().toString());
    }

    private void bindViews(View view) {
        etPb95Price = view.findViewById(R.id.update_prices_dialog_pb95_price_value);
        etPb95Price.setFilters(new InputFilter[]{new UpdatePriceInputFilter()});

        etPb98Price = view.findViewById(R.id.update_prices_dialog_pb98_price_value);
        etPb98Price.setFilters(new InputFilter[]{new UpdatePriceInputFilter()});

        etOnPrice = view.findViewById(R.id.update_prices_dialog_on_price_value);
        etOnPrice.setFilters(new InputFilter[]{new UpdatePriceInputFilter()});

        etLpgPrice = view.findViewById(R.id.update_prices_dialog_lpg_price_value);
        etLpgPrice.setFilters(new InputFilter[]{new UpdatePriceInputFilter()});

        progressBar = view.findViewById(R.id.update_prices_dialog_progress_bar);
    }
}
