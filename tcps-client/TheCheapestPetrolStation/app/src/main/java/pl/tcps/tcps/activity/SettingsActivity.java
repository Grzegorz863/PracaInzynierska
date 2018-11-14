package pl.tcps.tcps.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import pl.tcps.tcps.R;
import pl.tcps.tcps.fragment.ChangePasswordDialog;
import pl.tcps.tcps.fragment.DeleteUserDialog;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;

import static java.security.AccessController.getContext;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private AccessTokenDetails accessTokenDetails;
    private MainActivity parentActivity;

    private Button buttonChangePassword;
    private Button buttonDeleteUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        bindViews();
        Intent intent = getIntent();
        parentActivity = (MainActivity) getParent();
        accessTokenDetails = intent.getParcelableExtra(getString(R.string.key_access_token_details));

        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        Long savedStationDistanceRawBits = preferences.getLong(getString(R.string.settings_saved_station_distance), Double.doubleToRawLongBits(20));
        Double savedStationDistance = Double.longBitsToDouble(savedStationDistanceRawBits);

        Spinner spinnerStationDistance = findViewById(R.id.settings_station_distance_spinner);
        List<Double> allDistances = new ArrayList<>();
        allDistances.add(1d);
        allDistances.add(2d);
        allDistances.add(5d);
        allDistances.add(10d);
        allDistances.add(20d);
        allDistances.add(50d);
        allDistances.add(100d);
        allDistances.add(200d);

        ArrayAdapter<Double> dataAdapter = new ArrayAdapter<>(this, R.layout.distance_spinner_item, allDistances);
        dataAdapter.setDropDownViewResource(R.layout.distance_spinner_item);
        spinnerStationDistance.setAdapter(dataAdapter);

        if (savedStationDistance != 0)
            spinnerStationDistance.setSelection(allDistances.indexOf(savedStationDistance));

        spinnerStationDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(getString(R.string.settings_saved_station_distance), Double.doubleToRawLongBits((Double) parent.getItemAtPosition(position)));
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        setOnClickListenerChangePasswordButton();
        setOnClickListenerDeleteUserButton();
    }

    private void setOnClickListenerChangePasswordButton() {
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
                Bundle args = new Bundle();
                args.putParcelable(getString(R.string.key_access_token_details), accessTokenDetails);
                changePasswordDialog.setArguments(args);
                changePasswordDialog.show(getSupportFragmentManager(), "change_password_dialog");
            }
        });
    }

    private void setOnClickListenerDeleteUserButton() {
        buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteUserDialog deleteUserDialog = new DeleteUserDialog();
                Bundle args = new Bundle();
                args.putParcelable(getString(R.string.key_access_token_details), accessTokenDetails);
                deleteUserDialog.setArguments(args);
                deleteUserDialog.show(getSupportFragmentManager(), "delete_user_dialog");
            }
        });
    }

    public void logoutAfterDeleteAccount() {
        parentActivity.logoutUser();
    }

    private void bindViews() {
        buttonChangePassword = findViewById(R.id.settings_change_password_button);
        buttonDeleteUser = findViewById(R.id.settings_delete_user_button);
    }
}
