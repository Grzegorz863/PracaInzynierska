package pl.tcps.tcps.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.tcps.tcps.R;
import pl.tcps.tcps.api_client.PetrolStationClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.layouts.PetrolStationRecycleViewAdapter;
import pl.tcps.tcps.other.LocationConfiguration;
import pl.tcps.tcps.pojo.PetrolStationRecycleViewItem;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.responses.AddressResponse;
import pl.tcps.tcps.pojo.responses.PetrolStationMapMarker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int REQUEST_CODE = 1000;
    private final float DEFAULT_ZOOM = 7.0f;
    private Boolean mLocationPermissionGranted;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean isFirstLocationResult = true;
    private Double distance;
    private AccessTokenDetails accessTokenDetails;
    private List<PetrolStationMapMarker> petrolStations;

    //private Map<Marker, Long> markersWithStationsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //markersWithStationsId = new HashMap<>();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Intent intent = getIntent();
        distance = intent.getDoubleExtra(getString(R.string.key_distance), 20);
        accessTokenDetails = intent.getParcelableExtra(getString(R.string.key_access_token_details));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationPermission();
        updateMapsUI();
        getDeviceLocation();
        createLocationRequest();
        createLocationCallback(distance, accessTokenDetails);
        createLocationClient();
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location location = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.15, 19.01), DEFAULT_ZOOM)); //Katowice
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(60000); //1min
        locationRequest.setFastestInterval(1000); //1s
        locationRequest.setSmallestDisplacement(100); //100m
    }

    private void createLocationCallback(Double distance, AccessTokenDetails accessTokenDetails) {
        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locations = locationResult.getLocations();
                Location location = locations.get(locations.size() - 1);
                if (isFirstLocationResult)
                    getAllPetrolStationFittingInRange(location.getLatitude(), location.getLongitude(), distance, accessTokenDetails);
                isFirstLocationResult = false;
            }
        };
    }

    private void createLocationClient() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    private void getAllPetrolStationFittingInRange(Double latitude, Double longitude, Double distance, AccessTokenDetails accessTokenDetails) {
        Retrofit retrofit = RetrofitBuilder.createRetrofit(this);
        PetrolStationClient petrolStationClient = retrofit.create(PetrolStationClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<List<PetrolStationMapMarker>> call = petrolStationClient.findPetrolStationByDistanceForMap(authHeader, latitude, longitude, distance);
        Context context = this;

        call.enqueue(new Callback<List<PetrolStationMapMarker>>() {
            @Override
            public void onResponse(Call<List<PetrolStationMapMarker>> call, Response<List<PetrolStationMapMarker>> response) {
                petrolStations = response.body();
                if (response.isSuccessful() && petrolStations != null) {
                    putMapMarkers(petrolStations);
                } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                    Toast.makeText(context, "No station was found!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<PetrolStationMapMarker>> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void putMapMarkers(List<PetrolStationMapMarker> petrolStations) {
        Map<Marker, PetrolStationMapMarker> markersWithStationsId = new HashMap<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (PetrolStationMapMarker station : petrolStations) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(station.getLatitude(), station.getLongitude()));
            markerOptions.title(station.getStationName());
            AddressResponse address = station.getAddress();
            markerOptions.snippet(String.format(Locale.ENGLISH, "%s %s, %s", address.getStreet(), address.getApartmentNumber(), address.getCity()));
            Marker marker = mMap.addMarker(markerOptions);
            builder.include(marker.getPosition());
            markersWithStationsId.put(marker, station);
        }
        LatLngBounds bounds = builder.build();
        int height = getResources().getDisplayMetrics().heightPixels;
        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.1);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cameraUpdate);
        setMarkerOnClickListener(markersWithStationsId);
    }

    private void setMarkerOnClickListener(Map<Marker, PetrolStationMapMarker> markersWithStationsId) {
        Context context = this;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                PetrolStationMapMarker station = markersWithStationsId.get(marker);

                Intent intent = new Intent(context, StationDetailsActivity.class);
                intent.putExtra(context.getString(R.string.key_access_token_details), accessTokenDetails);
                intent.putExtra(context.getString(R.string.key_station_id), station.getStationId());
                intent.putExtra(context.getString(R.string.key_distance), station.getDistance());

                context.startActivity(intent);
            }
        });
    }

    private void updateMapsUI() {
        if (mMap == null)
            return;

        try {

            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                checkLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mLocationPermissionGranted = true;
        else
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionc_menu_maps_activity, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_activity_refresh:
                refreshMap();
                return true;

            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("MissingPermission")
    private void refreshMap() {
        createLocationRequest();
        LocationConfiguration.displayLocationSettingsRequest(this, this, locationRequest);
        if (mFusedLocationProviderClient != null)
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        getAllPetrolStationFittingInRange(location.getLatitude(), location.getLongitude(), distance, accessTokenDetails);
                    } else {
                        isFirstLocationResult = true;
                        createLocationCallback(distance, accessTokenDetails);

                    }
                }
            });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    mLocationPermissionGranted = false;
                    LocationConfiguration.show(this);
                }
            }
        }
    }
}
