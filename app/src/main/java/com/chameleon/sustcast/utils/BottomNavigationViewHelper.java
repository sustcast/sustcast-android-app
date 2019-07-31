package com.chameleon.sustcast.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.authentication.ApiLogin;
import com.chameleon.sustcast.chatHandler.ChatActivity;
import com.chameleon.sustcast.data.model.logoutResponse;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.UserClient;
import com.chameleon.sustcast.home.Home;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HEMAYEET on 1/3/2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: SettingupBottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
    }

    public static void enableNavigation(final Context context, final BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.ic_house:
                        Intent intent = new Intent(context, Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);


                        break;
                    case R.id.ic_search:
                        Intent intent_ = new Intent(context, ChatActivity.class);
                        intent_.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent_);
                        break;
                    case R.id.ic_logout:
                        UserClient mAPIService;
                        mAPIService = ApiUtils.getAPIService();
                        String tokenHere = Home.getToken();
                        Log.d("FAG", "onCreate: TokenHere" + tokenHere);
                        mAPIService.signout("Bearer " + tokenHere).enqueue(new Callback<logoutResponse>() {
                            @Override
                            public void onResponse(Call<logoutResponse> call, Response<logoutResponse> response) {
                                System.out.println("Response code =>" + response.code());
                                Log.i("MY: ", "LOGOUT CLICKED");
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Response Successful!! ", Toast.LENGTH_SHORT).show();
                                    //spinner.setVisibility(View.GONE);
                                    String className = Home.getClassName();
                                    Intent intent = new Intent(context, ApiLogin.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                } else {
                                    //spinner.setVisibility(View.GONE);
                                    Toast.makeText(context, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<logoutResponse> call, Throwable t) {
                                // spinner.setVisibility(View.GONE);
                                Toast.makeText(context, "No Internet.", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                            }
                        });
                }


                return false;
            }
        });


    }
}
