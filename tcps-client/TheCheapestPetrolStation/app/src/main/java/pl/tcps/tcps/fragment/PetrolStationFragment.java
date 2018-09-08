package pl.tcps.tcps.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class PetrolStationFragment extends Fragment {


    public PetrolStationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).setActionBarTitle("Petrol Stations");
        return inflater.inflate(R.layout.fragment_petrol_station, container, false);
    }

}
