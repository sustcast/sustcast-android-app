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
import android.widget.Button;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.authentication.ApiLogin;
import com.chameleon.sustcast.chatHandler.ChatActivity;
import com.chameleon.sustcast.credit.credit_page;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.UserClient;
import com.chameleon.sustcast.fontOverride.FontsOverride;
import com.chameleon.sustcast.utils.BottomNavigationViewHelper;
import com.google.android.gms.ads.AdView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "Home";
    private static final int ACTIVITY_NUM = 0;
    public static String token2;
    public static String className;
    DrawerLayout drawer;
    int buttonFlag;
    String url = "http://103.84.159.230:8000/sustcast";
    private AdView mAdView;
    private Context mContext = Home.this;
    private Button mChat;
    private UserClient mAPIService;

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
        token2 = getIntent().getStringExtra("token");
        Log.d("FAG", "onCreate: Token2" + token2);
        buttonFlag = 0;
        setupBottomNavigationView();
        setupViewPager();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        className = this.getClass().getName();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FontsOverride.setDefaultFont(this, "MONOSPACE", "doppio_one.ttf");
        setupBottomNavigationView();
        drawer = findViewById(R.id.drawer_layout);

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


}
