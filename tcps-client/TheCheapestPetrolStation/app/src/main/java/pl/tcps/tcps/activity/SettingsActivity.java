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
        Integer savedStationDistance = preferences.getInt(getString(R.string.settings_saved_station_distance),0);

        Spinner spinnerStationDistance = findViewById(R.id.settings_station_distance_spinner);
        List<Integer> allDistances = new ArrayList<>();
        allDistances.add(1);allDistances.add(2);allDistances.add(5);allDistances.add(10);
        allDistances.add(20);allDistances.add(50);allDistances.add(100);allDistances.add(200);

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(this, R.layout.distance_spinner_item, allDistances);
        dataAdapter.setDropDownViewResource(R.layout.distance_spinner_item);
        spinnerStationDistance.setAdapter(dataAdapter);

        if(savedStationDistance!=0)
            spinnerStationDistance.setSelection(allDistances.indexOf(savedStationDistance));

        spinnerStationDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.settings_saved_station_distance), (int)parent.getItemAtPosition(position));
                editor.apply();
        }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
