package com.chameleon.sustcast.authentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.data.model.logoutResponse;
import com.chameleon.sustcast.data.model.outer;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.UserClient;
import com.chameleon.sustcast.fontOverride.FontsOverride;
import com.chameleon.sustcast.home.Home;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiLogin extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private Button mRegisterButton;
    private Button mForgetPasswordButton;
    private UserClient mAPIService;
    private  static String token;
    private static final String TAG = "ApiLogin";
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "doppio_one.ttf");

        setContentView(R.layout.activity_main);

        mEmailField = findViewById(R.id.entryEmailLog);
        mPasswordField = findViewById(R.id.entryPasswordLog);

        mLoginButton = findViewById(R.id.buttonLogin);
        mRegisterButton = findViewById(R.id.buttonSignUp);
        mForgetPasswordButton = findViewById(R.id.buttonForget);

        mAPIService = ApiUtils.getAPIService();
        session = new SessionManager(getApplicationContext());
        Log.i(TAG,"LoginStatus: "+ session.isLoggedIn());

        if(session.isLoggedIn() == true){
            Intent intent = new Intent(ApiLogin.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    startSignIn(email, password);
                } else {
                    Toast.makeText(ApiLogin.this, "Field empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUp(view);
            }
        });

        mForgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ApiLogin.this, resetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startSignIn(String email, String password) {
        mAPIService.signin(email, password).enqueue(new Callback<outer>() {
            @Override
            public void onResponse(Call<outer> call, Response<outer> response) {
                System.out.println("Response code =>" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "post submitted to API." + response.body().getOutput().getToken());
                    Toast.makeText(ApiLogin.this, "Response Successful!!", Toast.LENGTH_SHORT).show();

                    token = response.body().getOutput().getToken();
                    session.createLoginSession(token);

                    Log.i(TAG,"SIGNIN token: "+ token);

                    Intent intent = new Intent(ApiLogin.this, Home.class);
                    intent.putExtra("token", response.body().getOutput().getToken());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ApiLogin.this, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<outer> call, Throwable t) {
                // spinner.setVisibility(View.GONE);
                Toast.makeText(ApiLogin.this, "Failure!!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();

            }
        });

    }

    public void onSignUp(View view) {
        Intent i = new Intent(ApiLogin.this, ApiRegistration.class);
        startActivity(i);

    }



}
