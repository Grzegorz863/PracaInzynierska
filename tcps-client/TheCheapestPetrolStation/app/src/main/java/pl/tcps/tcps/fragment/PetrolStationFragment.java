package pl.tcps.tcps.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.MainActivity;
import pl.tcps.tcps.api_client.ConsortiumClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.layouts.MyAdapter;
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

    public PetrolStationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View petrolStationFragment = inflater.inflate(R.layout.fragment_petrol_station, container, false);
        mainActivity = (MainActivity)getActivity();
        mainActivity.setActionBarTitle("Petrol Stations");

        Bundle args = getArguments();
        AccessTokenDetails accessTokenDetails = args.getParcelable("access_token_details");

//        if(savedInstanceState !=null)
//            accessTokenDetails = savedInstanceState.getParcelable("access_token_details");
        Retrofit retrofit = RetrofitBuilder.createRetrofit(petrolStationFragment.getContext());
        ConsortiumClient consortiumClient = retrofit.create(ConsortiumClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<String> call = consortiumClient.getConsortium(authHeader,1);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.isSuccessful())
//                    //Toast.makeText(petrolStationFragment.getContext(), response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(petrolStationFragment.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView recyclerView = petrolStationFragment.findViewById(R.id.petrol_stations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(petrolStationFragment.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<PetrolStationToDelete> petrolStations = new ArrayList<>();
        for (int i = 0; i < 20; ++i)
            petrolStations.add(new PetrolStationToDelete());

        recyclerView.setAdapter(new MyAdapter(petrolStations, recyclerView));

        return petrolStationFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setCheckedFirstItem();
    }
}
