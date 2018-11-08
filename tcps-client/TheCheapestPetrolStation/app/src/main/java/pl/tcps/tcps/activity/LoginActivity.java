package pl.tcps.tcps.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import pl.tcps.tcps.R;
import pl.tcps.tcps.api_client.LoginClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.UserDetails;
import pl.tcps.tcps.pojo.login.CheckAccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    //CallbackManager callbackManager;
    //ProgressBar progressBar;
    //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);

    private CardView loginButton;
    private CardView registrationButton;
    private TextView loginTextView;
    private TextView passwordTextView;

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();

        SharedPreferences sharedPreferences = getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(getString(R.string.key_token), null);
        String tokenType = sharedPreferences.getString(getString(R.string.key_token_type), null);
        String refreshToken = sharedPreferences.getString(getString(R.string.key_refresh_token), null);

        if(accessToken != null && tokenType != null && refreshToken != null){
            Retrofit retrofit = RetrofitBuilder.createRetrofit(this);
            LoginClient loginClient = retrofit.create(LoginClient.class);
            Call<CheckAccessToken> call = loginClient.checkAccessToken(accessToken);
            call.enqueue(new Callback<CheckAccessToken>() {
                @Override
                public void onResponse(Call<CheckAccessToken> call, Response<CheckAccessToken> response) {
                    CheckAccessToken checkAccessToken = response.body();
                    if (response.isSuccessful() && checkAccessToken != null){
                        if (checkAccessToken.getActive()) {
                            AccessTokenDetails accessTokenDetails = new AccessTokenDetails(accessToken, tokenType, refreshToken,
                                    checkAccessToken.getExp(), checkAccessToken.getScope().toString());
                            sendAndGoToMainActivityFromNativeLogin(accessTokenDetails, sharedPreferences);
                        }
                    }

                }

                @Override
                public void onFailure(Call<CheckAccessToken> call, Throwable t) {

                }
            });
        }

        setListenersForLoginButton(sharedPreferences);
        setListenersForRegistrationButton();

        //KOMENTARZE SA OD FB LOGIN
//        callbackManager = CallbackManager.Factory.create();

//        RelativeLayout relativeLayout = new RelativeLayout(this);
//        progressBar = new ProgressBar(LoginActivity.this,null,android.R.attr.progressBarStyleLarge);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        relativeLayout.addView(progressBar,params);

//        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
//        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
//
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                progressBar.setVisibility(View.VISIBLE);
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        progressBar.setVisibility(View.GONE);
//                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                        sendAndGoToMainActivityFromFBLogin(object);
//                    }
//                });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,email,first_name,last_name");
//                graphRequest.setParameters(parameters);
//                graphRequest.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//                Toast.makeText(getApplicationContext(), "Singing in was canceled", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Toast.makeText(getApplicationContext(), "Singing in was terminated", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        if(accessToken != null){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            UserDetails userDetails = new UserDetails(accessToken.getUserId());
//            intent.putExtra("user_details", userDetails);
//            startActivity(intent);
//            //finish();
//        }
    }

    private void bindViews() {
        loginButton = findViewById(R.id.login_activity_button_login);
        registrationButton = findViewById(R.id.login_activity_button_registration);
        loginTextView = findViewById(R.id.login_activity_et_login);
        passwordTextView = findViewById(R.id.login_activity_et_password);
    }

    private void setListenersForRegistrationButton() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setListenersForLoginButton(SharedPreferences sharedPreferences){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = loginTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                if(TextUtils.isEmpty(userName)) {
                    Toast.makeText(LoginActivity.this, "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("grant_type", "password");
                fieldMap.put("username", userName); // <== DO ZMIANY NA USERNAME
                fieldMap.put("password", password); // <== DO ZMIANY NA PASSWORD
                fieldMap.put("scope", "read write trust");

//                Map<String, Object> fieldMap = new HashMap<>();
//                fieldMap.put("grant_type", "refresh_token");
//                fieldMap.put("client_id", "android_app");
//                fieldMap.put("refresh_token", "65244e60-a2c9-4a02-8bb7-dc8f6a6b7bd7"); // <== DO ZMIANY NA USERNAME
//                //fieldMap.put("scope", "read write trust");

                Retrofit retrofit = RetrofitBuilder.createRetrofit(LoginActivity.this);

                LoginClient loginClient = retrofit.create(LoginClient.class);
                Call<AccessTokenDetails> call = loginClient.accessToken(fieldMap);

                call.enqueue(new Callback<AccessTokenDetails>() {
                    @Override
                    public void onResponse(Call<AccessTokenDetails> call, Response<AccessTokenDetails> response) {
                        AccessTokenDetails accessTokenDetails = response.body();
                        if(response.isSuccessful() && accessTokenDetails != null){
                            sendAndGoToMainActivityFromNativeLogin(accessTokenDetails, sharedPreferences);
                        }else{
                            Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessTokenDetails> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void sendAndGoToMainActivityFromNativeLogin(AccessTokenDetails accessTokenDetails, SharedPreferences sharedPreferences){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.key_token), accessTokenDetails.getAccessToken());
        editor.putString(getString(R.string.key_token_type), accessTokenDetails.getTokenType());
        editor.putString(getString(R.string.key_refresh_token), accessTokenDetails.getRefreshToken());
        editor.apply();
        intent.putExtra("access_token_details", accessTokenDetails);
        startActivity(intent);
        finish();
    }

//    private void sendAndGoToMainActivityFromFBLogin(JSONObject object) {
//        try{
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            UserDetails userDetails = new UserDetails(object.getString("id"),
//                    object.getString("first_name"),
//                    object.getString("last_name"),
//                    object.getString("email"));
//
//            intent.putExtra("user_details", userDetails);
//            startActivity(intent);
//            //finish();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}