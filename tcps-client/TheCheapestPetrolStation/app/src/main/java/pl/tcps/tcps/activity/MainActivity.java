package pl.tcps.tcps.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import pl.tcps.tcps.fragment.AddStationFragment;
import pl.tcps.tcps.fragment.PetrolStationFragment;
import pl.tcps.tcps.R;
import pl.tcps.tcps.pojo.UserDetails;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final double DEFAULT_DISTANCE = 20;

    private AccessTokenDetails accessTokenDetails;
    private NavigationView navigationView;
    private Double savedStationDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        UserDetails userDetails = getIntent().getParcelableExtra("user_details");
        if (userDetails != null) {
            String firstNameAndLastName = userDetails.getFirstName() + " " + userDetails.getLastName();
            TextView logged = headerView.findViewById(R.id.logged_name);
            logged.setText(firstNameAndLastName);
            logged = headerView.findViewById(R.id.logged_email);
            logged.setText(userDetails.getEmail());

            String avatarURL = getString(R.string.avatar_url, userDetails.getId());
            ImageView avatar = headerView.findViewById(R.id.logged_avatar);
            Picasso.with(this).load(avatarURL).into(avatar);
        }
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_first_option) {
            createPetrolStationFragment(savedStationDistance);
        } else if (id == R.id.nav_second_option) {
            createAddStationFragment();
        } else if (id == R.id.nav_third_option) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void setCheckedFirstItem(){
        navigationView.setCheckedItem(R.id.nav_first_option);
    }

    public void setCheckedSecondItem(){
        navigationView.setCheckedItem(R.id.nav_second_option);
    }

    public void setActionBarTitle(String title){
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
