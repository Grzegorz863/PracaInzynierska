package pl.tcps.tcps.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import pl.tcps.tcps.pojo.Consortium;
import pl.tcps.tcps.pojo.PetrolStationResponse;
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

    public AddStationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View addStationFragment = inflater.inflate(R.layout.fragment_add_station, container, false);
        mainActivity = (MainActivity)getActivity();
        mainActivity.setActionBarTitle("Add Petrol Stations");

        Button addStationButton = addStationFragment.findViewById(R.id.add_station_add_station_button);

        Bundle args = getArguments();
        AccessTokenDetails accessTokenDetails = args.getParcelable("access_token_details");

        Retrofit retrofit = RetrofitBuilder.createRetrofit(addStationFragment.getContext());
        ConsortiumClient consortiumClient = retrofit.create(ConsortiumClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<Collection<Consortium>> call = consortiumClient.getAllConsortiums(authHeader);

        call.enqueue(new Callback<Collection<Consortium>>() {
            @Override
            public void onResponse(Call<Collection<Consortium>> call, final Response<Collection<Consortium>> response) {
                if(response.isSuccessful()){
                    final Collection<Consortium> consortiums = response.body();
                    consortiums.forEach(consortium->consortiumsNames.add(consortium.getConsortiumName()));

                    Spinner consortiumsSpinner = addStationFragment.findViewById(R.id.add_station_consortium_name_spinner);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(addStationFragment.getContext(), R.layout.support_simple_spinner_dropdown_item, consortiumsNames);
                    dataAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    consortiumsSpinner.setAdapter(dataAdapter);
                    }else
                        Toast.makeText(addStationFragment.getContext(),"Problem with consortium name on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Collection<Consortium>> call, Throwable t) {
                Toast.makeText(addStationFragment.getContext(),"Connection error with server", Toast.LENGTH_SHORT).show();
            }
        });

        addStationButton.setOnClickListener(v -> createNewStation(v, addStationFragment, accessTokenDetails));

        return addStationFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setCheckedSecondItem();
    }

    private void createNewStation(View addStationButton, View addStationFragment,
                                  AccessTokenDetails accessTokenDetails){
        TextView tvStationName = addStationFragment.findViewById(R.id.add_station_name);
        TextView tvStreet = addStationFragment.findViewById(R.id.add_station_street);
        TextView tvApartmentNumber = addStationFragment.findViewById(R.id.add_station_apartment_number);
        TextView tvCity = addStationFragment.findViewById(R.id.add_station_city);
        TextView tvPostalCode = addStationFragment.findViewById(R.id.add_station_postal_code);
        Spinner spinnerConsortiumName = addStationFragment.findViewById(R.id.add_station_consortium_name_spinner);
        Switch switchHasFood = addStationFragment.findViewById(R.id.add_station_has_food_switch);
        TextView tvDescription = addStationFragment.findViewById(R.id.add_station_description);

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

        Call<PetrolStationResponse> call = petrolStationClient.createPetrolStation(authorizationHeader, fieldMap);

        call.enqueue(new Callback<PetrolStationResponse>() {
            @Override
            public void onResponse(Call<PetrolStationResponse> call, Response<PetrolStationResponse> response) {
                if(response.isSuccessful()){
                    PetrolStationResponse petrolStationResponse = response.body();
                    Toast.makeText(addStationFragment.getContext(), "Created station: "
                            + petrolStationResponse.getStationName(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<PetrolStationResponse> call, Throwable t) {
                Toast.makeText(addStationFragment.getContext(), "Add station error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}