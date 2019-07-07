package com.chameleon.streammusic.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.chameleon.streammusic.ApiLogout;
import com.chameleon.streammusic.Favourites.feedback;
import com.chameleon.streammusic.Homenew.HomeNew;
import com.chameleon.streammusic.R;
import com.chameleon.streammusic.Search.RJ;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by HEMAYEET on 1/3/2018.
 */

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: SettingupBottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);
    }

    public static void enableNavigation(final Context context, final BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.ic_house:
                        Intent intent1 = new Intent(context, HomeNew.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_search:
                       Intent intent2 = new Intent(context, RJ.class);
                       context.startActivity(intent2);

                        break;
                    case R.id.ic_heart:
                        /*Intent intent3 = new Intent(context, feedback.class);
                        context.startActivity(intent3);*/
                        //Snackbar.make(view, "Feature Under Construction", Snackbar.LENGTH_LONG)
                          //      .setAction("Action", null).show();
                        break;
                    case R.id.ic_playlist:
                        final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.facebook.com/robotbakers/"));
                        context.startActivity(intent);
                        /*Intent intent4 = new Intent(context, HomeNew.class);
                        context.startActivity(intent4);*/
                        break;


                }


                return false;
            }
        });


    }
}
