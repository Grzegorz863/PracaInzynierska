package pl.tcps.tcps.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import pl.tcps.tcps.R;
import pl.tcps.tcps.api_client.RegistrationClient;
import pl.tcps.tcps.api_client.retrofit.RetrofitBuilder;
import pl.tcps.tcps.pojo.login.AccessTokenDetails;
import pl.tcps.tcps.pojo.registration.RegisteredUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistrationActivity extends AppCompatActivity {

    Button registrationButton;
    TextView etUsername;
    TextView etPassword;
    TextView etFirstName;
    TextView etLastName;
    TextView etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registrationButton = findViewById(R.id.registration_activity_button_registration);
        etUsername = findViewById(R.id.registration_activity_et_username);
        etPassword = findViewById(R.id.registration_activity_et_password);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);

        setListenersForRegistrationButton();
    }

    private void setListenersForRegistrationButton() {
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = etFirstName.getText().toString();
                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(RegistrationActivity.this, "Enter first name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String lastName = etLastName.getText().toString();
                if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(RegistrationActivity.this, "Enter last name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userName = etUsername.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegistrationActivity.this, "Enter username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = etEmail.getText().toString();
                Boolean val = Patterns.EMAIL_ADDRESS.matcher(email).matches();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegistrationActivity.this, "Enter e-mail!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(RegistrationActivity.this, "Wrong e-mail!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                generateAccessTokenAndRegisterUser(firstName, lastName, userName, password, email);
            }
        });
    }

    private void generateAccessTokenAndRegisterUser(final String firstName, final String lastName, final String userName, final String password, final String email) {
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("grant_type", "password");
        fieldMap.put("username", "registration");
        fieldMap.put("password", "reg_password");
        fieldMap.put("scope", "read write trust");


        Retrofit retrofit = RetrofitBuilder.createRetrofit(RegistrationActivity.this);

        RegistrationClient registrationClient = retrofit.create(RegistrationClient.class);
        Call<AccessTokenDetails> call = registrationClient.accessToken(fieldMap);

        call.enqueue(new Callback<AccessTokenDetails>() {
            @Override
            public void onResponse(Call<AccessTokenDetails> call, Response<AccessTokenDetails> response) {
                if (response.isSuccessful()) {
                    AccessTokenDetails accessTokenDetails = response.body();

                    Map<String, Object> hearedMap = new HashMap<>();
                    hearedMap.put("Authorization", accessTokenDetails.getTokenType() + " " + accessTokenDetails.getAccessToken());
                    hearedMap.put("user_name", userName);
                    hearedMap.put("password", password);
                    hearedMap.put("role", "android_user");
                    hearedMap.put("enable", true);
                    hearedMap.put("first_name", firstName);
                    hearedMap.put("last_name", lastName);
                    hearedMap.put("email", email);

                    Retrofit retrofit = RetrofitBuilder.createRetrofit(RegistrationActivity.this);
                    RegistrationClient registrationClient = retrofit.create(RegistrationClient.class);
                    Call<RegisteredUser> call2 = registrationClient.registerUser(hearedMap);

                    call2.enqueue(new Callback<RegisteredUser>() {
                        @Override
                        public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                            if (response.code() == HttpURLConnection.HTTP_CREATED) {
                                RegisteredUser registeredUser = response.body();
                                Toast.makeText(RegistrationActivity.this,
                                        "Created: " + registeredUser.getFirstName() + " " + registeredUser.getLastName(),
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                if(response.code() == HttpURLConnection.HTTP_SEE_OTHER)
                                    Toast.makeText(RegistrationActivity.this, "User already exist!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(RegistrationActivity.this, "Registration error!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisteredUser> call, Throwable t) {
                            Toast.makeText(RegistrationActivity.this, "Registration error!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(RegistrationActivity.this, "Registration error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccessTokenDetails> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Registration error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}