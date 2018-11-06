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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import pl.tcps.tcps.R;
import pl.tcps.tcps.activity.SettingsActivity;
import pl.tcps.tcps.api_client.UserClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordDialog extends AppCompatDialogFragment {

    private SettingsActivity activity;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etRepeatedNewPassword;

    final Integer PASSWORD_LENGTH_MIN = 4;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        activity = (SettingsActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.change_pw_dialog_layout, null);
        bindViews(view);

        AccessTokenDetails accessTokenDetails = getArguments()
                .getParcelable(getString(R.string.key_access_token_details));

        Retrofit retrofit = RetrofitBuilder.createRetrofit(activity);
        UserClient userClient = retrofit.create(UserClient.class);
        String authHeader = accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken();

        builder.setView(view)
                .setTitle("Password change")
                .setCancelable(true)
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeUserPassword(authHeader, userClient);
                    }
                });
        return builder.create();
    }

    private void changeUserPassword(String authHeader, UserClient userClient) {
        Map<String, String> headerMap = createHeader();
        if(headerMap == null)
            return;

        Call<Void> call = userClient.changeUserPassword(authHeader, headerMap);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    if(response.code() == HttpURLConnection.HTTP_NO_CONTENT)
                        Toast.makeText(activity, "You changed your password!", Toast.LENGTH_LONG).show();
                }else{
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                        Toast.makeText(activity, "You entered wrong old password", Toast.LENGTH_LONG).show();
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

    private Map<String,String> createHeader() {

        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String repeatedNewPassword = etRepeatedNewPassword.getText().toString();

        if(TextUtils.isEmpty(oldPassword))
            return null;

        if(newPassword.length() < PASSWORD_LENGTH_MIN) {
            Toast.makeText(activity, "New password is too short", Toast.LENGTH_LONG).show();
            return null;
        }

        if(newPassword.equals(oldPassword)){
            Toast.makeText(activity, "New passwords must be different!", Toast.LENGTH_LONG).show();
            return null;
        }

        if(!repeatedNewPassword.equals(newPassword)){
            Toast.makeText(activity, "New passwords are different!", Toast.LENGTH_LONG).show();
            return null;
        }

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("old_password", oldPassword);
        headerMap.put("new_password", newPassword);

        return headerMap;
    }

    private void bindViews(View view) {
        etOldPassword = view.findViewById(R.id.change_pw_dialog_old_password_et);
        etNewPassword = view.findViewById(R.id.change_pw_dialog_new_password_et);
        etRepeatedNewPassword = view.findViewById(R.id.change_pw_dialog_repeat_new_password_et);
    }
}
