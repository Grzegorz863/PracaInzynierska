package pl.tcps.tcps.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.net.HttpURLConnection;
import java.util.List;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.MainActivity;
import pl.tcps.tcps.api_client.PetrolStationClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.layouts.PetrolStationRecycleViewAdapter;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.PetrolStationRecycleViewItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


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
    private RecyclerView recyclerView;

    public PetrolStationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        petrolStationFragment = inflater.inflate(R.layout.fragment_petrol_station, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivity.setActionBarTitle("Petrol Stations");
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        accessTokenDetails = args.getParcelable(getString(R.string.key_access_token_details));
        distance = args.getDouble(getString(R.string.key_distance));

        recyclerView = petrolStationFragment.findViewById(R.id.petrol_stations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(petrolStationFragment.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        locationRequest.setInterval(60000); //1min
        locationRequest.setFastestInterval(1000); //1s
        //locationRequest.setSmallestDisplacement(500); //500m
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

    private void getPetrolStationFitInRange(Double latitude, Double longitude, Double distance, AccessTokenDetails accessTokenDetails) {
        Retrofit retrofit = RetrofitBuilder.createRetrofit(petrolStationFragment.getContext());
        PetrolStationClient petrolStationClient = retrofit.create(PetrolStationClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<List<PetrolStationRecycleViewItem>> call = petrolStationClient.findPetrolStationByDistance(authHeader, latitude, longitude, distance);

        call.enqueue(new Callback<List<PetrolStationRecycleViewItem>>() {
            @Override
            public void onResponse(Call<List<PetrolStationRecycleViewItem>> call, Response<List<PetrolStationRecycleViewItem>> response) {
                if (response.isSuccessful()) {
                    List<PetrolStationRecycleViewItem> petrolStations = response.body();

                    recyclerView.setAdapter(new PetrolStationRecycleViewAdapter(petrolStations, accessTokenDetails, recyclerView));
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                    Toast.makeText(petrolStationFragment.getContext(), "No station was found!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<PetrolStationRecycleViewItem>> call, Throwable t) {
                Toast.makeText(petrolStationFragment.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createLocationClient() {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
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

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayLocationSettingsRequest(petrolStationFragment.getContext());
                    createLocationCallback(distance, accessTokenDetails);
                    createLocationClient();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    showLocationPermissionDenialInformation();
                }
            }
        }
    }

    private void showLocationPermissionDenialInformation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, R.style.Theme_AppCompat_Dialog_Alert);
        builder
                .setTitle("App does not have locate permission")
                .setMessage(R.string.permission_denied_message)
                .setIcon(R.drawable.ic_location_permission_denied_24dp)
                .setPositiveButton(R.string.permission_denied_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton(R.string.permission_denied_close_app, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainActivity.finishAndRemoveTask();
                    }
                })
                .show();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences preferences = mainActivity.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Long savedStationDistanceRawBits = preferences.getLong(getString(R.string.settings_saved_station_distance), Double.doubleToLongBits(mainActivity.DEFAULT_DISTANCE));
        Double changedDistanceFromSettings = Double.longBitsToDouble(savedStationDistanceRawBits);

        if (!changedDistanceFromSettings.equals(distance))
            createLocationCallback(changedDistanceFromSettings, accessTokenDetails);


        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        mainActivity.setCheckedFirstItem();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_refresh:
                refreshRecycleView();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @SuppressLint("MissingPermission")
    private void refreshRecycleView() {

        displayLocationSettingsRequest(petrolStationFragment.getContext());
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(mainActivity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        getPetrolStationFitInRange(location.getLatitude(), location.getLongitude(), distance, accessTokenDetails);
                    }
                }
            });
    }

}
