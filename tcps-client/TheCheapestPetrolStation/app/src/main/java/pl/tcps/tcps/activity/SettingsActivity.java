package pl.tcps.tcps.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import pl.tcps.tcps.R;

import static java.security.AccessController.getContext;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        preferences = getSharedPreferences("settings" ,Context.MODE_PRIVATE);
        Long savedStationDistanceRawBits = preferences.getLong(getString(R.string.settings_saved_station_distance),0);
        Double savedStationDistance = Double.longBitsToDouble(savedStationDistanceRawBits);

        Spinner spinnerStationDistance = findViewById(R.id.settings_station_distance_spinner);
        List<Double> allDistances = new ArrayList<>();
        allDistances.add(1d);allDistances.add(2d);allDistances.add(5d);allDistances.add(10d);
        allDistances.add(20d);allDistances.add(50d);allDistances.add(100d);allDistances.add(200d);

        ArrayAdapter<Double> dataAdapter = new ArrayAdapter<>(this, R.layout.distance_spinner_item, allDistances);
        dataAdapter.setDropDownViewResource(R.layout.distance_spinner_item);
        spinnerStationDistance.setAdapter(dataAdapter);

        if(savedStationDistance!=0)
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

    }
}
