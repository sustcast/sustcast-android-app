package com.chameleon.streammusic.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chameleon.streammusic.ApiLogin;
import com.chameleon.streammusic.ChatActvity;
import com.chameleon.streammusic.R;
import com.chameleon.streammusic.Utils.BottomNavigationViewHelper;
import com.chameleon.streammusic.credit_page;
import com.chameleon.streammusic.data.model.logoutResponse;
import com.chameleon.streammusic.data.remote.ApiUtils;
import com.chameleon.streammusic.data.remote.UserClient;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;

    private Context mContext = HomeActivity.this;
    private Button mLogout;
    private Button mChat;
    private UserClient mAPIService;
    private String token;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLogout = findViewById(R.id.buttonLogout);
        mChat = findViewById(R.id.buttonChat);

        mAPIService = ApiUtils.getAPIService();
        token = getIntent().getStringExtra("token");
        Log.d(TAG, "onCreate  :starting. " );
        setupBottomNavigationView();
        setupViewPager();

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer= findViewById(R.id.drawer_layout);
        NavigationView navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_close ,R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    logout();
            }
        });
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChat();
            }
        });

    }

    private void startChat() {
        Intent intent = new Intent(HomeActivity.this, ChatActvity.class);
        startActivity(intent);
    }

    private void logout() {
        mAPIService.signout("Bearer "+token).enqueue(new Callback<logoutResponse>() {
            @Override
            public void onResponse(Call<logoutResponse> call, Response<logoutResponse> response) {
                System.out.println("Response code =>" + response.code());
                Log.i("MY: ","LOGOUT CLICKED");
                if (response.isSuccessful()){
                    Toast.makeText(HomeActivity.this, "Response Successful!! in general page", Toast.LENGTH_SHORT).show();
                    //spinner.setVisibility(View.GONE);
                    Intent intent = new Intent(HomeActivity.this, ApiLogin.class);
                    startActivity(intent);
                }
                else{
                    //spinner.setVisibility(View.GONE);
                    Toast.makeText(HomeActivity.this, "Response Unsuccessful", Toast.LENGTH_SHORT).show();
                }

                //System.out.println("JSON => " + new GsonBuilder().setPrettyPrinting().create().toJson(response));
            }

            @Override
            public void onFailure(Call<logoutResponse> call, Throwable t) {
               // spinner.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, "No Internet.", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(item.isChecked()){
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        if(id== R.id.nav_credits){
            startActivity(new Intent(getApplicationContext(), credit_page.class));
        }
        else if(id== R.id.nav_rateus){
            startActivity(new Intent(getApplicationContext(), ApiLogin.class));

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * responsible for adding the 3 tabs;
     */
    private void setupViewPager(){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LiveFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new TrendingFragment());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Live");
        tabLayout.getTabAt(1).setText("Home");
        tabLayout.getTabAt(2).setText("Trending");
    }

    /**
     * Bottom NavigationView setup
     */


    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}
