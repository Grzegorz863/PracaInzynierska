package pl.tcps.tcps.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.HttpURLConnection;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.SettingsActivity;
import pl.tcps.tcps.api_client.UserClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteUserDialog extends AppCompatDialogFragment {

    private SettingsActivity activity;
    private EditText etPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        activity = (SettingsActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_user_dialog_layout, null);
        bindViews(view);

        AccessTokenDetails accessTokenDetails = getArguments()
                .getParcelable(getString(R.string.key_access_token_details));

        Retrofit retrofit = RetrofitBuilder.createRetrofit(activity);
        UserClient userClient = retrofit.create(UserClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();

        builder.setView(view)
                .setTitle("Delete account")
                .setCancelable(true)
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(authHeader, userClient);
                    }
                });
        return builder.create();
    }

    private void deleteUser(String authHeader, UserClient userClient) {
        String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(password))
            return;

        Call<Void> call = userClient.deleteUser(authHeader, password);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    if(response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                        Toast.makeText(activity, "You deleted you account", Toast.LENGTH_LONG).show();
                        activity.logoutAfterDeleteAccount();
                        activity.finishAndRemoveTask();
                    }
                }else{
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                        Toast.makeText(activity, "You entered wrong password", Toast.LENGTH_LONG).show();
                    else
                    if(response.code() == HttpURLConnection.HTTP_NOT_FOUND)
                        Toast.makeText(activity, "User not found!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(activity, "Error on server", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindViews(View view) {
        etPassword = view.findViewById(R.id.delete_user_dialog_password);
    }
}
