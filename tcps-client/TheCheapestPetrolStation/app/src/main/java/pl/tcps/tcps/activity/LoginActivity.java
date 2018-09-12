package pl.tcps.tcps.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import pl.tcps.tcps.R;
import pl.tcps.tcps.model.UserDetails;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ProgressBar progressBar;
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        }
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}