package pl.tcps.tcps.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import pl.tcps.tcps.api_client.UserClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.fragment.AddStationFragment;
import pl.tcps.tcps.fragment.PetrolStationFragment;
import pl.tcps.tcps.R;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.responses.UserDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final double DEFAULT_DISTANCE = 20;

    private AccessTokenDetails accessTokenDetails;
    private NavigationView navigationView;
    private Double savedStationDistance;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        accessTokenDetails = getIntent().getParcelableExtra(getString(R.string.key_access_token_details));

        SharedPreferences preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        Long savedStationDistanceRawBits = preferences.getLong(getString(R.string.settings_saved_station_distance), Double.doubleToLongBits(DEFAULT_DISTANCE));
        savedStationDistance = Double.longBitsToDouble(savedStationDistanceRawBits);
        createPetrolStationFragment(savedStationDistance);

        navigationView.setCheckedItem(R.id.nav_first_option);
        View headerView = navigationView.getHeaderView(0);

        setNameAndEmailOnNavigationView(headerView);

//        UserDetails userDetails = getIntent().getParcelableExtra("user_details");
//        if (userDetails != null) {
//            String firstNameAndLastName = userDetails.getFirstName() + " " + userDetails.getLastName();
//            TextView logged = headerView.findViewById(R.id.logged_name);
//            logged.setText(firstNameAndLastName);
//            logged = headerView.findViewById(R.id.logged_email);
//            logged.setText(userDetails.getEmail());
//
//            String avatarURL = getString(R.string.avatar_url, userDetails.getId());
//            ImageView avatar = headerView.findViewById(R.id.logged_avatar);
//            Picasso.with(this).load(avatarURL).into(avatar);
//        }
    }

    private void setNameAndEmailOnNavigationView(View headerView) {
        Retrofit retrofit = RetrofitBuilder.createRetrofit(this);
        UserClient userClient = retrofit.create(UserClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<UserDetailsResponse> call = userClient.getLoggedUserDetails(authHeader);
        call.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                UserDetailsResponse userDetails = response.body();
                if (response.isSuccessful() && userDetails != null){
                    String nameAndLastName = userDetails.getFirstName() + " " + userDetails.getLastName();
                    TextView logged = headerView.findViewById(R.id.logged_name);
                    logged.setText(nameAndLastName);
                    logged = headerView.findViewById(R.id.logged_email);
                    logged.setText(userDetails.getEmail());
                }else
                    Toast.makeText(context, "Error on server", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("access_token_details", accessTokenDetails);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        accessTokenDetails = savedInstanceState.getParcelable("access_token_details");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_first_option:
                createPetrolStationFragment(savedStationDistance);
                break;
            case R.id.nav_second_option:
                createAddStationFragment();
                break;
            case R.id.nav_third_option:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_fourth_option:
                startMapActivity();
            case R.id.nav_logout:
                logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {

    }

    private void startMapActivity() {
    }

    private void createAddStationFragment() {
        AddStationFragment addStationFragment = new AddStationFragment();
        Bundle args = new Bundle();
        args.putParcelable("access_token_details", accessTokenDetails);
        addStationFragment.setArguments(args);
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMain, addStationFragment);
        fragmentTransaction.commit();
    }

    private void createPetrolStationFragment(Double distance) {
        PetrolStationFragment petrolStationFragment = new PetrolStationFragment();
        Bundle args = new Bundle();
        args.putParcelable("access_token_details", accessTokenDetails);
        args.putDouble(getString(R.string.key_distance), distance);
        petrolStationFragment.setArguments(args);
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMain, petrolStationFragment);
        fragmentTransaction.commit();
    }

    public void setCheckedFirstItem() {
        navigationView.setCheckedItem(R.id.nav_first_option);
    }

    public void setCheckedSecondItem() {
        navigationView.setCheckedItem(R.id.nav_second_option);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    //    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
}
