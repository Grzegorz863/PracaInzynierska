package pl.tcps.tcps.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.MainActivity;
import pl.tcps.tcps.api_client.ConsortiumClient;
import pl.tcps.tcps.api_client.PetrolStationClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.pojo.responses.ConsortiumResponse;
import pl.tcps.tcps.pojo.responses.CreatePetrolStationResponse;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddStationFragment extends Fragment{

    private MainActivity mainActivity;
    private final List<String> consortiumsNames = new ArrayList<>();
    private View addStationFragment;

    private TextView tvStationName;
    private TextView tvStreet;
    private TextView tvApartmentNumber;
    private TextView tvCity;
    private TextView tvPostalCode;
    private Spinner spinnerConsortiumName;
    private Switch switchHasFood;
    private TextView tvDescription;
    private Button addStationButton;

    public AddStationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        addStationFragment = inflater.inflate(R.layout.fragment_add_station, container, false);
        mainActivity = (MainActivity)getActivity();
        mainActivity.setActionBarTitle("Add Petrol Stations");
        bindViews();
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        AccessTokenDetails accessTokenDetails = args.getParcelable(getString(R.string.key_access_token_details));

        Retrofit retrofit = RetrofitBuilder.createRetrofit(addStationFragment.getContext());
        ConsortiumClient consortiumClient = retrofit.create(ConsortiumClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<Collection<ConsortiumResponse>> call = consortiumClient.getAllConsortiums(authHeader);

        call.enqueue(new Callback<Collection<ConsortiumResponse>>() {
            @Override
            public void onResponse(Call<Collection<ConsortiumResponse>> call, final Response<Collection<ConsortiumResponse>> response) {
                if(response.isSuccessful()){
                    final Collection<ConsortiumResponse> consortiumResponses = response.body();
                    consortiumResponses.forEach(consortiumResponse ->consortiumsNames.add(consortiumResponse.getConsortiumName()));

                    Spinner consortiumsSpinner = addStationFragment.findViewById(R.id.add_station_consortium_name_spinner);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(addStationFragment.getContext(), R.layout.support_simple_spinner_dropdown_item, consortiumsNames);
                    dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    consortiumsSpinner.setAdapter(dataAdapter);
                    }else
                        Toast.makeText(addStationFragment.getContext(),"Problem with consortium name on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Collection<ConsortiumResponse>> call, Throwable t) {
                Toast.makeText(addStationFragment.getContext(),"Connection error with server", Toast.LENGTH_SHORT).show();
            }
        });

        addStationButton.setOnClickListener(v -> createNewStation(v, addStationFragment, accessTokenDetails));

        return addStationFragment;
    }

    private void bindViews() {
        tvStationName = addStationFragment.findViewById(R.id.add_station_name);
        tvStreet = addStationFragment.findViewById(R.id.add_station_street);
        tvApartmentNumber = addStationFragment.findViewById(R.id.add_station_apartment_number);
        tvCity = addStationFragment.findViewById(R.id.add_station_city);
        tvPostalCode = addStationFragment.findViewById(R.id.add_station_postal_code);
        spinnerConsortiumName = addStationFragment.findViewById(R.id.add_station_consortium_name_spinner);
        switchHasFood = addStationFragment.findViewById(R.id.add_station_has_food_switch);
        tvDescription = addStationFragment.findViewById(R.id.add_station_description);
        addStationButton = addStationFragment.findViewById(R.id.add_station_add_station_button);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setCheckedSecondItem();
    }

    private void createNewStation(View addStationButton, View addStationFragment,
                                  AccessTokenDetails accessTokenDetails){

        String stationName = tvStationName.getText().toString();
        if (TextUtils.isEmpty(stationName)) {
            Toast.makeText(addStationFragment.getContext(), "Enter station name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String street = tvStreet.getText().toString();
        if (TextUtils.isEmpty(street)) {
            Toast.makeText(addStationFragment.getContext(), "Enter street name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String apartmentNumber = tvApartmentNumber.getText().toString();
        if (TextUtils.isEmpty(apartmentNumber)) {
            Toast.makeText(addStationFragment.getContext(), "Enter apartment number!", Toast.LENGTH_SHORT).show();
            return;
        }

        String city = tvCity.getText().toString();
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(addStationFragment.getContext(), "Enter city name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String postalCode = tvPostalCode.getText().toString();
        if (TextUtils.isEmpty(postalCode)) {
            Toast.makeText(addStationFragment.getContext(), "Enter postal code!", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedConsortiumName = consortiumsNames.get(spinnerConsortiumName.getSelectedItemPosition());
        if (TextUtils.isEmpty(apartmentNumber)) {
            Toast.makeText(addStationFragment.getContext(), "Select consortium name!", Toast.LENGTH_SHORT).show();
            return;
        }

        Boolean hasFood = switchHasFood.isChecked();

        String description = tvDescription.getText().toString();
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(addStationFragment.getContext(), "Enter description!", Toast.LENGTH_SHORT).show();
            return;
        }

        String authorizationHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();

        Retrofit retrofit = RetrofitBuilder.createRetrofit(addStationFragment.getContext());
        PetrolStationClient petrolStationClient = retrofit.create(PetrolStationClient.class);

        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("station_name", stationName);
        fieldMap.put("street", street);
        fieldMap.put("apartment_number", apartmentNumber);
        fieldMap.put("city", city);
        fieldMap.put("postal_code", postalCode);
        fieldMap.put("consortium_name", selectedConsortiumName);
        fieldMap.put("has_food", hasFood);
        fieldMap.put("description", description);

        Call<CreatePetrolStationResponse> call = petrolStationClient.createPetrolStation(authorizationHeader, fieldMap);

        call.enqueue(new Callback<CreatePetrolStationResponse>() {
            @Override
            public void onResponse(Call<CreatePetrolStationResponse> call, Response<CreatePetrolStationResponse> response) {
                if(response.isSuccessful()){
                    CreatePetrolStationResponse createPetrolStationResponse = response.body();
                    Toast.makeText(addStationFragment.getContext(), "Created station: "
                            + createPetrolStationResponse.getStationName(), Toast.LENGTH_SHORT).show();
                }else{
                    if(response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                        Toast.makeText(addStationFragment.getContext(), "You entered wrong address!",
                                Toast.LENGTH_SHORT).show();
                    else {
                        if(response.code() == HttpURLConnection.HTTP_SEE_OTHER)
                            Toast.makeText(addStationFragment.getContext(),
                                    "There is already station with the same name or address!",
                                    Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(addStationFragment.getContext(), "Add station error!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreatePetrolStationResponse> call, Throwable t) {
                Toast.makeText(addStationFragment.getContext(), "Add station error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cleanTextViews(){
        tvStationName.setText(null);
        tvStreet.setText(null);
        tvApartmentNumber.setText(null);
        tvCity.setText(null);
        tvPostalCode.setText(null);
        switchHasFood.setChecked(false);
        tvDescription.setText(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_refresh:
                cleanTextViews();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}