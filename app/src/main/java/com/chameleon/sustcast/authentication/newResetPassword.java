package com.chameleon.sustcast.authentication;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newResetPassword extends AppCompatActivity {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mCpassword;
    private EditText mToken;
    private Button mSend;
    private ProgressBar spinner;

    private UserClient mAPIService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reset_password);

        mEmail = findViewById(R.id.sendEmail);
        mPassword = findViewById(R.id.sendPassword);
        mCpassword = findViewById(R.id.sendCpassword);
        mToken = findViewById(R.id.sendToken);
        mSend = findViewById(R.id.sendInfo);
        spinner = findViewById(R.id.pBarresetnew);
        mAPIService = ApiUtils.getAPIService();

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String cpassword = mCpassword.getText().toString().trim();
                String token = mToken.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(cpassword) && !TextUtils.isEmpty(token)) {
                    spinner.setVisibility(View.VISIBLE);
                    sendResetInfo(email, password, cpassword, token);
                }
            }
        });


    }

    private void sendResetInfo(String email, String password, String cpassword, String token) {
        mAPIService.resetPassword(email, password, cpassword, token).enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                System.out.println("Response code =>" + response.code());
                if (response.isSuccessful()) {
                    Toast.makeText(newResetPassword.this, "Response Successful!! You can now login with new credentials!", Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                    Intent intent = new Intent(newResetPassword.this, ApiLogin.class);
                    startActivity(intent);
                } else {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(newResetPassword.this, "Response Unsuccessful!!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(newResetPassword.this, "Response Failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}
