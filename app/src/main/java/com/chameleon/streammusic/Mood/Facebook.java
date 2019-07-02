package com.chameleon.streammusic.Mood;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.chameleon.streammusic.R;
import com.chameleon.streammusic.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class Facebook extends AppCompatActivity {

    private static final String TAG = "Facebook";
    private static final int ACTIVITY_NUM = 4;
    private Context mContext = Facebook.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_facebook);//previous
        setContentView(R.layout.activity_feedback);
        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();

        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        //startActivity(browserIntent);

        /*
        Uri webpage = Uri.parse("http://www.facebook.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);

        }
        */
        //personal toolbar added
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarplaymood);
        //toolbar.setTitle("Mood");
        //toolbar.setTitleTextColor(Color.WHITE);
        //setSupportActionBar(toolbar);
        //setupBottomNavigationView();
    }

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);



    }


    /*

    public void onHappy(View view){
        Snackbar.make(view, "Music List Will Be Retrieved From Database.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    public void onSad(View view){
        Snackbar.make(view, "Music List Will Be Retrieved From Database.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    public void onInspired(View view){
        Snackbar.make(view, "Music List Will Be Retrieved From Database.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    public void onFunky(View view){
        Snackbar.make(view, "Music List Will Be Retrieved From Database.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    public void onClassy(View view){
        Snackbar.make(view, "Music List Will Be Retrieved From Database.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    public void onVintage(View view){
        Snackbar.make(view, "Music List Will Be Retrieved From Database.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    */
}
