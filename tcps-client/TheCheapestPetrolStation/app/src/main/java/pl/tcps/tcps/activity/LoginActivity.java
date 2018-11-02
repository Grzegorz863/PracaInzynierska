package pl.tcps.tcps.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import java.util.Map;

import pl.tcps.tcps.R;
import pl.tcps.tcps.api_client.LoginClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.UserDetails;
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

        loginButton = findViewById(R.id.login_activity_button_login);
        registrationButton = findViewById(R.id.login_activity_button_registration);
        loginTextView = findViewById(R.id.login_activity_et_login);
        passwordTextView = findViewById(R.id.login_activity_et_password);

        setListenersForLoginButton();
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

    private void setListenersForRegistrationButton() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setListenersForLoginButton(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = loginTextView.getText().toString();
                String password = passwordTextView.getText().toString();
//                if(TextUtils.isEmpty(userName)) {
//                    Toast.makeText(LoginActivity.this, "Enter username!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(TextUtils.isEmpty(password)) {
//                    Toast.makeText(LoginActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("grant_type", "password");
                fieldMap.put("username", "user1"); // <== DO ZMIANY NA USERNAME
                fieldMap.put("password", "user1"); // <== DO ZMIANY NA PASSWORD
                fieldMap.put("scope", "read write trust");

                Retrofit retrofit = RetrofitBuilder.createRetrofit(LoginActivity.this);

                LoginClient loginClient = retrofit.create(LoginClient.class);
                Call<AccessTokenDetails> call = loginClient.accessToken(fieldMap);

                call.enqueue(new Callback<AccessTokenDetails>() {
                    @Override
                    public void onResponse(Call<AccessTokenDetails> call, Response<AccessTokenDetails> response) {
                        if(response.isSuccessful()){
                            sendAndGoToMainActivityFromNativeLogin(response.body());
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

    private void sendAndGoToMainActivityFromNativeLogin(AccessTokenDetails accessTokenDetails){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("access_token_details", accessTokenDetails);
        startActivity(intent);
        //finish();
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