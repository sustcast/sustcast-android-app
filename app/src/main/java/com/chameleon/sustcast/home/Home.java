package com.chameleon.sustcast.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.authentication.ApiLogin;
import com.chameleon.sustcast.authentication.SessionManager;
import com.chameleon.sustcast.chatHandler.ChatActivity;
import com.chameleon.sustcast.credit.credit_page;
import com.chameleon.sustcast.data.model.OuterUser;
import com.chameleon.sustcast.data.model.User;
import com.chameleon.sustcast.data.model.logoutResponse;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.UserClient;
import com.chameleon.sustcast.fontOverride.FontsOverride;
import com.chameleon.sustcast.utils.BottomNavigationViewHelper;
import com.google.android.gms.ads.AdView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Home";
    private static final int ACTIVITY_NUM = 0;
    public static String token2;
    public static String className;
    static OutputStream outToServer;
    static InputStream inFromServer;
    static Socket socket;
    static int mediaGenjam = 50;
    private  String token;
    DrawerLayout drawer;
    int buttonFlag;
    String url = "http://103.84.159.230:8000/sustcast";
    private AdView mAdView;
    private Context mContext = Home.this;
    private Button mChat;
    private UserClient mAPIService;
    private static String userName;
    SessionManager session;
    private Button mLogout;

    public static String getClassName() {
        return className;
    }

    public static String getToken() {
        return token2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAPIService = ApiUtils.getAPIService();
        /*token2 = getIntent().getStringExtra("token");
        Log.d(TAG, "onCreate: Token" + token2);*/
        mLogout = findViewById(R.id.buttonLogout);
        buttonFlag = 0;
        setupBottomNavigationView();
        setupViewPager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        className = this.getClass().getName();
        session = new SessionManager(getApplicationContext());
        Log.i(TAG,"LoginStatus: "+ session.isLoggedIn());

        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        token2 = user.get(SessionManager.KEY_TOKEN);
        Log.d(TAG, "HASHMAP Token" + token2);

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FontsOverride.setDefaultFont(this, "MONOSPACE", "doppio_one.ttf");
        setupBottomNavigationView();
        drawer = findViewById(R.id.drawer_layout);
        fetchUsername();

    }

    private void fetchUsername() {
        mAPIService.getName("Bearer " + token2).enqueue(new Callback<OuterUser>() {
            @Override
            public void onResponse(Call<OuterUser> call, Response<OuterUser> response) {
                System.out.println("Response code =>" + response.code());
                if(response.isSuccessful()){
                    User user = response.body().getUser();
                    Log.i(TAG, "getname successful" +user.getName().toString());
                    userName = user.getName().toString();
                }

                else {
                    Log.i(TAG, "response getname unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<OuterUser> call, Throwable t) {
                    Log.i(TAG,"GETNAME FAILED");
            }
        });
    }


    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LiveFragment());
        adapter.addFragment(new ChatFragment());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Live");
        tabLayout.getTabAt(1).setText("Lyrics");
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, getClassName(), bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (item.isChecked()) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        if (id == R.id.nav_credits) {
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        } else if (id == R.id.nav_rateus) {
            startActivity(new Intent(getApplicationContext(), ApiLogin.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static String getUserName(){
        return  userName;
    }

    public void logout() {
        String tokenHere = Home.getToken();
        Log.d(TAG, " Token Here" + tokenHere);
        mAPIService.signout("Bearer " + tokenHere).enqueue(new Callback<logoutResponse>() {
            @Override
            public void onResponse(Call<logoutResponse> call, Response<logoutResponse> response) {
                System.out.println("Response code =>" + response.code());
                Log.i("MY: ", "LOGOUT CLICKED");
                if (response.isSuccessful()) {
                    Toast.makeText(Home.this, "Response Successful!! ", Toast.LENGTH_SHORT).show();
                    session.cleanPref();
                    Intent intent = new Intent(Home.this, ApiLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    //spinner.setVisibility(View.GONE);
                    Toast.makeText(Home.this, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<logoutResponse> call, Throwable t) {
                // spinner.setVisibility(View.GONE);
                Toast.makeText(Home.this, "No Internet.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


}
