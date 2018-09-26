package pl.tcps.tcps.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import okhttp3.ResponseBody;
import pl.tcps.tcps.R;
import pl.tcps.tcps.api_client.LoginClient;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.UserDetails;
import pl.tcps.tcps.pojo.login.LoginUser;
import pl.tcps.tcps.ssl.SelfSigningClientBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ProgressBar progressBar;
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);

    Button loginButton;
    Button registrationButton;
    TextView loginTextView;
    TextView passwordTextView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.buttonLogin);
        registrationButton = findViewById(R.id.buttonRegistration);
        loginTextView = findViewById(R.id.etLogin);
        passwordTextView = findViewById(R.id.etPassword);

        setListenersForButtons();

        callbackManager = CallbackManager.Factory.create();

        RelativeLayout relativeLayout = new RelativeLayout(this);
        progressBar = new ProgressBar(LoginActivity.this,null,android.R.attr.progressBarStyleLarge);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        sendAndGoToMainActivity(object);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,first_name,last_name");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Singing in was canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Singing in was terminated", Toast.LENGTH_LONG).show();
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            UserDetails userDetails = new UserDetails(accessToken.getUserId());
            intent.putExtra("user_details", userDetails);
            startActivity(intent);
            //finish();
        }
    }

    private void setListenersForButtons(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = loginTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                if(userName.matches(""))
                    Toast.makeText(LoginActivity.this, "Enter username!", Toast.LENGTH_SHORT);
                if(password.matches(""))
                    Toast.makeText(LoginActivity.this, "Enter password!", Toast.LENGTH_SHORT);

                Map<String, Object> headerMap = new HashMap<>();
                headerMap.put("grant_type", "password");
                headerMap.put("username", userName);
                headerMap.put("password", password);
                headerMap.put("scope", "read write trust");

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(getString(R.string.base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(SelfSigningClientBuilder.createClient(LoginActivity.this));

                Retrofit retrofit = builder.build();

                LoginClient loginClient = retrofit.create(LoginClient.class);
                Call<AccessTokenDetails> call = loginClient.accessToken(headerMap);

                call.enqueue(new Callback<AccessTokenDetails>() {
                    @Override
                    public void onResponse(Call<AccessTokenDetails> call, Response<AccessTokenDetails> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(LoginActivity.this, response.body().getAccessToken(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Wrong credentials", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessTokenDetails> call, Throwable t) {
                        Log.d("myError", t.toString());
                        Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void sendAndGoToMainActivity(JSONObject object) {
        try{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            UserDetails userDetails = new UserDetails(object.getString("id"),
                    object.getString("first_name"),
                    object.getString("last_name"),
                    object.getString("email"));

            intent.putExtra("user_details", userDetails);
            startActivity(intent);
            //finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}