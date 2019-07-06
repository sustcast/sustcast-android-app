package com.chameleon.streammusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chameleon.streammusic.Homenew.HomeNew;
import com.chameleon.streammusic.data.model.outer;
import com.chameleon.streammusic.data.remote.ApiUtils;
import com.chameleon.streammusic.data.remote.UserClient;
import com.google.gson.GsonBuilder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "doppio_one.ttf");

        setContentView(R.layout.activity_main);

        mEmailField = (EditText) findViewById(R.id.entryEmailLog);
        mPasswordField = (EditText) findViewById(R.id.entryPasswordLog);

        mLoginButton = (Button) findViewById(R.id.buttonLogin);
        mRegisterButton = (Button) findViewById(R.id.buttonSignUp);
        mForgetPasswordButton = (Button) findViewById(R.id.buttonForget);

        mAPIService = ApiUtils.getAPIService();

        // setContentView(R.layout.activity_homenew);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailField.getText().toString().trim();
                String password = mPasswordField.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    //spinner.setVisibility(View.VISIBLE);
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
                    Log.i("MY :", "post submitted to API." + response.body().getOutput().getToken());
                    Toast.makeText(ApiLogin.this, "Response Successful!!", Toast.LENGTH_SHORT).show();
                    //spinner.setVisibility(View.GONE);
                    Intent intent = new Intent(ApiLogin.this, HomeNew.class);
                    intent.putExtra("token", response.body().getOutput().getToken());
                    startActivity(intent);
                } else {
                    //spinner.setVisibility(View.GONE);
                    Toast.makeText(ApiLogin.this, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                }

                System.out.println("JSON => " + new GsonBuilder().setPrettyPrinting().create().toJson(response));

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


    public void onFacebook(View view) {

        Snackbar.make(view, "Prototype Stage. Click on Login to proceed.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void onGoogle(View view) {

        Snackbar.make(view, "Prototype Stage. Click on Login to proceed.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
