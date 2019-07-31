package com.chameleon.sustcast.authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.UserClient;

public class ApiLogout extends AppCompatActivity {

    private UserClient mAPIService;
    private String token;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_logout);
        mAPIService = ApiUtils.getAPIService();
        spinner = findViewById(R.id.pBarlogout);
    }
}
