package pl.tcps.tcps.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.MainActivity;
import pl.tcps.tcps.api_client.ConsortiumClient;
import pl.tcps.tcps.api_client.PetrolStationClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.layouts.MyAdapter;
import pl.tcps.tcps.pojo.PetrolStationResponse;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import pl.tcps.tcps.pojo.PetrolStationToDelete;


/**
 * A simple {@link Fragment} subclass.
 */
public class PetrolStationFragment extends Fragment {

    private MainActivity mainActivity;
    private View petrolStationFragment;

    private final int REQUEST_CODE = 1000;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Double distance;
    private AccessTokenDetails accessTokenDetails;

    public PetrolStationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        petrolStationFragment = inflater.inflate(R.layout.fragment_petrol_station, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.setActionBarTitle("Petrol Stations");

        Bundle args = getArguments();
        accessTokenDetails = args.getParcelable(getString(R.string.key_access_token_details));
        distance = args.getDouble(getString(R.string.key_distance));

        RecyclerView recyclerView = petrolStationFragment.findViewById(R.id.petrol_stations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(petrolStationFragment.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<PetrolStationToDelete> petrolStations = new ArrayList<>();
        for (int i = 0; i < 20; ++i)
            petrolStations.add(new PetrolStationToDelete());

        recyclerView.setAdapter(new MyAdapter(petrolStations, recyclerView));

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            displayLocationSettingsRequest(petrolStationFragment.getContext());
            createLocationCallback(distance, accessTokenDetails);
            createLocationClient();
        }

        return petrolStationFragment;
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(600000); //10min
        locationRequest.setFastestInterval(300000); //5min
        locationRequest.setSmallestDisplacement(500); //500m
    }

    private void createLocationCallback(Double distance, AccessTokenDetails accessTokenDetails) {
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locations = locationResult.getLocations();
                Location location = locations.get(locations.size() - 1);
                getPetrolStationFitInRange(location.getLatitude(), location.getLongitude(), distance, accessTokenDetails);
            }
        };
    }

    private void getPetrolStationFitInRange(Double latitude, Double longitude, Double distance, AccessTokenDetails accessTokenDetails){
        Retrofit retrofit = RetrofitBuilder.createRetrofit(petrolStationFragment.getContext());
        PetrolStationClient petrolStationClient = retrofit.create(PetrolStationClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<Collection<PetrolStationResponse>> call = petrolStationClient.findPetrolStationByDistance(authHeader, latitude, longitude, distance);

        call.enqueue(new Callback<Collection<PetrolStationResponse>>() {
            @Override
            public void onResponse(Call<Collection<PetrolStationResponse>> call, Response<Collection<PetrolStationResponse>> response) {
                if (response.isSuccessful())
                    Toast.makeText(petrolStationFragment.getContext(),String.valueOf(response.body().size()) , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Collection<PetrolStationResponse>> call, Throwable t) {
                Toast.makeText(petrolStationFragment.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void createLocationClient(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void displayLocationSettingsRequest(Context context) {
        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> result = settingsClient.checkLocationSettings(builder.build());

        result.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception error) {
                Integer statusCode = ((ApiException) error).getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) error;
                            resolvable.startResolutionForResult(mainActivity, 1);
                        } catch (IntentSender.SendIntentException ignored) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    displayLocationSettingsRequest(petrolStationFragment.getContext());
                    createLocationCallback(distance, accessTokenDetails);
                    createLocationClient();
                }else if(grantResults[0] == PackageManager.PERMISSION_DENIED){

                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setCheckedFirstItem();
    }
}
