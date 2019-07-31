package com.chameleon.sustcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.authentication.ApiLogin;
import com.chameleon.sustcast.fontOverride.FontsOverride;
import com.chameleon.sustcast.home.Home;

public class MainScreen extends AppCompatActivity {
    private TextView tvsplash;
    private ImageView iv1;
    private ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "doppio_one.ttf");
        setContentView(R.layout.activity_main_screen);

        tvsplash = findViewById(R.id.tvsplash);
        iv1 = findViewById(R.id.iv1);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        tvsplash.startAnimation(myanim);
        iv1.startAnimation(myanim);

        final Intent i = new Intent(this, Home.class);


        Thread Timer = new Thread() {
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                } finally {
                    startActivity(i);
                    finish();

                }
            }


        };
        Timer.start();
    }
}
