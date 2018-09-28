package pl.tcps.tcps.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.MainActivity;
import pl.tcps.tcps.api_client.ConsortiumsClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.pojo.UserDetails;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class PetrolStationFragment extends Fragment {

    private MainActivity mainActivity;
    private TextView consortiumName;

    public PetrolStationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View petrolStationFragment = inflater.inflate(R.layout.fragment_petrol_station, null);
        mainActivity = (MainActivity)getActivity();
        consortiumName = petrolStationFragment.findViewById(R.id.consortium_text_view);


        mainActivity.setActionBarTitle("Petrol Stations");
        AccessTokenDetails accessTokenDetails = mainActivity.getIntent().getParcelableExtra("access_token_details");
        //Toast.makeText(mainActivity, accessTokenDetails.getAccessToken(), Toast.LENGTH_SHORT).show();

        Retrofit retrofit = RetrofitBuilder.createRetrofit(mainActivity);

        ConsortiumsClient consortiumsClient = retrofit.create(ConsortiumsClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();
        Call<String> call = consortiumsClient.getConsortium(authHeader,1);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful())
                    consortiumName.setText(response.body());
                Toast.makeText(mainActivity, response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(mainActivity, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        return inflater.inflate(R.layout.fragment_petrol_station, container, false);
    }

}
