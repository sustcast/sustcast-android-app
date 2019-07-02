package com.chameleon.streammusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.chameleon.streammusic.data.remote.ApiUtils;
import com.chameleon.streammusic.data.remote.UserClient;

public class ApiLogout extends AppCompatActivity {

    private UserClient mAPIService;
    private String token;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_logout);

        mAPIService = ApiUtils.getAPIService();
        spinner = (ProgressBar) findViewById(R.id.pBarlogout);


    }
}
