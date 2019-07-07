package com.chameleon.streammusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chameleon.streammusic.Home.Home;

public class MainScreen extends AppCompatActivity {
    private TextView tvsplash;
    private ImageView iv1;
    private ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this,"MONOSPACE", "doppio_one.ttf");
        setContentView(R.layout.activity_main_screen);

        tvsplash = (TextView) findViewById(R.id.tvsplash);
        iv1 = (ImageView) findViewById(R.id.iv1);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        tvsplash.startAnimation(myanim);
        iv1.startAnimation(myanim);
//        iv2.startAnimation(myanim);
        //screen switching below
       // final Intent i = new Intent(this, ApiLogin.class);
        final Intent i = new Intent(this, Home.class);


        Thread Timer  = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();

                }
                finally{
                    startActivity(i);
                    finish();

                }
            }




        };
        Timer.start();
    }
}
