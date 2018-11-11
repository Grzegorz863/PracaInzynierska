package pl.tcps.tcps.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.Comparator;
import java.util.List;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.MainActivity;
import pl.tcps.tcps.api_client.PetrolStationClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.layouts.PetrolStationRecycleViewAdapter;
import pl.tcps.tcps.layouts.SORTING_WAYS;
import pl.tcps.tcps.other.LocationConfiguration;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.PetrolStationRecycleViewItem;
import pl.tcps.tcps.pojo.responses.PetrolPriceRecycleViewItem;
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
    private Boolean isFirstLocationResult = true;
    private List<PetrolStationRecycleViewItem> petrolStations;
    private SORTING_WAYS sortingWay;

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

        sortingWay = setSortingWay();

        recyclerView = petrolStationFragment.findViewById(R.id.petrol_stations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(petrolStationFragment.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            createLocationRequest();
            LocationConfiguration.displayLocationSettingsRequest(petrolStationFragment.getContext(), mainActivity, locationRequest);
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
                if (isFirstLocationResult)
                    getAllPetrolStationFittingInRange(location.getLatitude(), location.getLongitude(), distance, accessTokenDetails);
                isFirstLocationResult = false;
            }
        };
    }

    private void getAllPetrolStationFittingInRange(Double latitude, Double longitude, Double distance, AccessTokenDetails accessTokenDetails) {
        Retrofit retrofit = RetrofitBuilder.createRetrofit(petrolStationFragment.getContext());
        PetrolStationClient petrolStationClient = retrofit.create(PetrolStationClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<List<PetrolStationRecycleViewItem>> call = petrolStationClient.findPetrolStationByDistance(authHeader, latitude, longitude, distance);

        call.enqueue(new Callback<List<PetrolStationRecycleViewItem>>() {
            @Override
            public void onResponse(Call<List<PetrolStationRecycleViewItem>> call, Response<List<PetrolStationRecycleViewItem>> response) {
                if (response.isSuccessful()) {
                    petrolStations = response.body();
                    sortPetrolStations();

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.key_recycle_view_state), recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(getString(R.string.key_recycle_view_state));
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest();
                    LocationConfiguration.displayLocationSettingsRequest(petrolStationFragment.getContext(), mainActivity, locationRequest);
                    createLocationCallback(distance, accessTokenDetails);
                    createLocationClient();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    LocationConfiguration.show(mainActivity);
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        //isFirstLocationResult = true;
        SharedPreferences preferences = mainActivity.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Long savedStationDistanceRawBits = preferences.getLong(getString(R.string.settings_saved_station_distance), Double.doubleToLongBits(mainActivity.DEFAULT_DISTANCE));
        Double changedDistanceFromSettings = Double.longBitsToDouble(savedStationDistanceRawBits);

        if (!changedDistanceFromSettings.equals(distance))
            createLocationCallback(changedDistanceFromSettings, accessTokenDetails);


        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        mainActivity.setCheckedFirstItem();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//    }

    @Override
    public void onPause() {
        super.onPause();

        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        saveSortingWayInSharedPreferences(sortingWay);
    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_menu_petrol_station_fragment, menu);
        MenuItem item;

        switch (sortingWay) {
            case SORT_BY_DISTANCE:
                item = menu.findItem(R.id.popup_menu_sort_by_distance);
                item.setChecked(true);
                break;

            case SORT_BY_PB_95_PRICE:
                item = menu.findItem(R.id.popup_menu_sort_by_pb95_price);
                item.setChecked(true);
                break;

            case SORT_BY_PB_98_PRICE:
                item = menu.findItem(R.id.popup_menu_sort_by_pb98_price);
                item.setChecked(true);
                break;

            case SORT_BY_ON_PRICE:
                item = menu.findItem(R.id.popup_menu_sort_by_on_price);
                item.setChecked(true);
                break;

            case SORT_BY_LPG_PRICE:
                item = menu.findItem(R.id.popup_menu_sort_by_lpg_price);
                item.setChecked(true);
                break;

            default:
                item = menu.findItem(R.id.popup_menu_sort_by_distance);
                item.setChecked(true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.petrol_station_activity_refresh:
                refreshRecycleView();
                return true;

            case R.id.popup_menu_sort_by_distance:
                sortPetrolStationsByDistances();
                sortingWay = SORTING_WAYS.SORT_BY_DISTANCE;
                item.setChecked(true);
                return true;

            case R.id.popup_menu_sort_by_pb95_price:
                sortPetrolStationsByPb95Price();
                sortingWay = SORTING_WAYS.SORT_BY_PB_95_PRICE;
                item.setChecked(true);
                return true;

            case R.id.popup_menu_sort_by_pb98_price:
                sortPetrolStationsByPb98Price();
                sortingWay = SORTING_WAYS.SORT_BY_PB_98_PRICE;
                item.setChecked(true);
                return true;

            case R.id.popup_menu_sort_by_on_price:
                sortPetrolStationsByOnPrice();
                sortingWay = SORTING_WAYS.SORT_BY_ON_PRICE;
                item.setChecked(true);
                return true;

            case R.id.popup_menu_sort_by_lpg_price:
                sortPetrolStationsByLpgPrice();
                sortingWay = SORTING_WAYS.SORT_BY_LPG_PRICE;
                item.setChecked(true);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void saveSortingWayInSharedPreferences(SORTING_WAYS sortingWay) {
        SharedPreferences preferences = mainActivity.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("sorting_way", sortingWay.getValue());
        editor.apply();
    }

    private SORTING_WAYS setSortingWay() {
        SharedPreferences preferences = mainActivity.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Integer way = preferences.getInt("sorting_way", 0);
        MenuItem item;

        switch (way) {
            case 0:
                return SORTING_WAYS.SORT_BY_DISTANCE;
            case 1:
                return SORTING_WAYS.SORT_BY_PB_95_PRICE;
            case 2:
                return SORTING_WAYS.SORT_BY_PB_98_PRICE;
            case 3:
                return SORTING_WAYS.SORT_BY_ON_PRICE;
            case 4:
                return SORTING_WAYS.SORT_BY_LPG_PRICE;
        }

        item = mainActivity.findViewById(R.id.popup_menu_sort_by_distance);
        item.setChecked(true);
        return SORTING_WAYS.SORT_BY_DISTANCE;
    }

    private void sortPetrolStations() {

        switch (sortingWay) {
            case SORT_BY_DISTANCE:
                sortPetrolStationsByDistances();
                break;

            case SORT_BY_PB_95_PRICE:
                sortPetrolStationsByPb95Price();
                break;

            case SORT_BY_PB_98_PRICE:
                sortPetrolStationsByPb98Price();
                break;

            case SORT_BY_ON_PRICE:
                sortPetrolStationsByOnPrice();
                break;

            case SORT_BY_LPG_PRICE:
                sortPetrolStationsByLpgPrice();
                break;

            default:
                sortPetrolStationsByLpgPrice();

        }
    }

    private void sortPetrolStationsByDistances() {
        if (petrolStations != null && petrolStations.size() != 0) {
            petrolStations.sort(Comparator.comparing(PetrolStationRecycleViewItem::getDistance));
            PetrolStationRecycleViewAdapter adapter = new PetrolStationRecycleViewAdapter(petrolStations,
                    accessTokenDetails, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    private void sortPetrolStationsByPb95Price() {
        if (petrolStations != null && petrolStations.size() != 0) {
            petrolStations.sort((o1, o2) -> {
                PetrolPriceRecycleViewItem prices1 = o1.getPriceRecycleViewItem();
                PetrolPriceRecycleViewItem prices2 = o2.getPriceRecycleViewItem();
                if (prices1.getPb95Price() == 0f)
                    return 1;
                if (prices2.getPb95Price() == 0f)
                    return -1;
                return prices1.getPb95Price() > prices2.getPb95Price() ? 1 : (prices1.getPb95Price() < prices2.getPb95Price()) ? -1 : 0;
            });
            PetrolStationRecycleViewAdapter adapter = new PetrolStationRecycleViewAdapter(petrolStations,
                    accessTokenDetails, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    private void sortPetrolStationsByPb98Price() {
        if (petrolStations != null && petrolStations.size() != 0) {
            petrolStations.sort((o1, o2) -> {
                PetrolPriceRecycleViewItem prices1 = o1.getPriceRecycleViewItem();
                PetrolPriceRecycleViewItem prices2 = o2.getPriceRecycleViewItem();
                if (prices1.getPb98Price() == 0f)
                    return 1;
                if (prices2.getPb98Price() == 0f)
                    return -1;
                return prices1.getPb98Price() > prices2.getPb98Price() ? 1 : (prices1.getPb98Price() < prices2.getPb98Price()) ? -1 : 0;
            });
            PetrolStationRecycleViewAdapter adapter = new PetrolStationRecycleViewAdapter(petrolStations,
                    accessTokenDetails, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    private void sortPetrolStationsByOnPrice() {
        if (petrolStations != null && petrolStations.size() != 0) {
            petrolStations.sort((o1, o2) -> {
                PetrolPriceRecycleViewItem prices1 = o1.getPriceRecycleViewItem();
                PetrolPriceRecycleViewItem prices2 = o2.getPriceRecycleViewItem();
                if (prices1.getOnPrice() == 0f)
                    return 1;
                if (prices2.getOnPrice() == 0f)
                    return -1;
                return prices1.getOnPrice() > prices2.getOnPrice() ? 1 : (prices1.getOnPrice() < prices2.getOnPrice()) ? -1 : 0;
            });
            PetrolStationRecycleViewAdapter adapter = new PetrolStationRecycleViewAdapter(petrolStations,
                    accessTokenDetails, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    private void sortPetrolStationsByLpgPrice() {
        if (petrolStations != null && petrolStations.size() != 0) {
            petrolStations.sort((o1, o2) -> {
                PetrolPriceRecycleViewItem prices1 = o1.getPriceRecycleViewItem();
                PetrolPriceRecycleViewItem prices2 = o2.getPriceRecycleViewItem();
                if (prices1.getLpgPrice() == 0f)
                    return 1;
                if (prices2.getLpgPrice() == 0f)
                    return -1;
                return prices1.getLpgPrice() > prices2.getLpgPrice() ? 1 : (prices1.getLpgPrice() < prices2.getLpgPrice()) ? -1 : 0;
            });
            PetrolStationRecycleViewAdapter adapter = new PetrolStationRecycleViewAdapter(petrolStations,
                    accessTokenDetails, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    @SuppressLint("MissingPermission")
    private void refreshRecycleView() {
        createLocationRequest();
        LocationConfiguration.displayLocationSettingsRequest(petrolStationFragment.getContext(), mainActivity, locationRequest);
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(mainActivity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        getAllPetrolStationFittingInRange(location.getLatitude(), location.getLongitude(), distance, accessTokenDetails);
                    }else {
                        isFirstLocationResult = true;
                        createLocationCallback(distance, accessTokenDetails);

                    }
                }
            });
    }

}
