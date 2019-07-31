package com.chameleon.sustcast.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.UserClient;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class resetPasswordActivity extends AppCompatActivity {
    private EditText mEmailText;
    private Button mResetButton;
    private ProgressBar spinner;

    private UserClient mAPIService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mEmailText = findViewById(R.id.resetEmail);
        mResetButton = findViewById(R.id.resetButton);
        spinner = findViewById(R.id.pBarreset);

        mAPIService = ApiUtils.getAPIService();


        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailText.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    spinner.setVisibility(View.VISIBLE);
                    startReset(email);
                } else {
                    Toast.makeText(resetPasswordActivity.this, "Field empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void startReset(String email) {
        mAPIService.requestReset(email).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("Response code => " + response.code());
                if (response.isSuccessful()) {
                    if (response.code() == 404) {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(resetPasswordActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 200) {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(resetPasswordActivity.this, "Password reset link sent.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(resetPasswordActivity.this, newResetPassword.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                } else {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(resetPasswordActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                }
                System.out.println("JSON => " + new GsonBuilder().setPrettyPrinting().create().toJson(response));


            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(resetPasswordActivity.this, "Failed to Request Reset!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
