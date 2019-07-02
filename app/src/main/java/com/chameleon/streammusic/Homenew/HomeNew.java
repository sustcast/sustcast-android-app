package com.chameleon.streammusic.Homenew;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chameleon.streammusic.FontsOverride;
import com.chameleon.streammusic.ApiLogin;
import com.chameleon.streammusic.R;
import com.chameleon.streammusic.Utils.BottomNavigationViewHelper;
import com.chameleon.streammusic.credit_page;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class HomeNew extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;

    static final String serverName = "103.84.159.230";
    static final int port = 50002;
    static OutputStream outToServer;
    public static DataOutputStream out;
    static InputStream inFromServer;
    public static DataInputStream in;
    static Socket socket;
    final String token = "siojdioajs21839712987391872ahsdhkjshkjdh21983912doiasoidoias";
    final String userName = "shuhan";

    private static final String TAG = "PlaylistActivity";
    private static final int ACTIVITY_NUM = 0;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    CoordinatorLayout coordinatorLayout;

    private Context mContext = HomeNew.this;

    //new code added from radiomeowv2
    static int buttonFlag;
    String url = "http://103.84.159.230:8000/mymount";
    static MediaPlayer mediaPlayer;

    static ClientThread client;
    static Thread T;

    static asyncInit initMedia;
    static Thread Tasync;

    static int mediaGenjam = 50;

    public static String curSong;
    public static String curArtist;
    public static String curGenre;
    public static String curLyric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homenew);
        FontsOverride.setDefaultFont(this,"MONOSPACE", "doppio_one.ttf");

        Log.d(TAG, "onCreate: started");
       setupBottomNavigationView();
        Log.d(TAG, "onCreate: started");

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_close ,R.string.navigation_drawer_open);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordiLayout);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        final Button button = (Button) findViewById(R.id.button1);
        final TextView songName = (TextView) findViewById(R.id.song_name);
        final TextView artistName = (TextView) findViewById(R.id.artist);
        final TextView lyricsField = (TextView) findViewById(R.id.lyrics);
        lyricsField.setMovementMethod(new ScrollingMovementMethod());
        final TextView genreName = (TextView) findViewById(R.id.genre);
        //added code here
        final CollapsingToolbarLayout collapsingToolbar =  (CollapsingToolbarLayout) findViewById(R.id.collapsing);


        try {
           if(mediaPlayer != null)
           {
               System.out.println("I am playing");

               if (buttonFlag == 0)
                   button.setBackgroundResource(R.drawable.logo_play_3);
               else
                   button.setBackgroundResource(R.drawable.logo_pause);
           }
           else {

               buttonFlag = 0;
           }
        }
       catch (Exception e) {
            System.out.println(e);
        }


        if(mediaPlayer == null)
            buttonFlag = 0;

        button.setOnClickListener(new View.OnClickListener() {

                                      public void onClick(View v) {
                                          if(mediaPlayer != null)
                                          {
                                              if (buttonFlag == 0) buttonFlag = 1;
                                              else buttonFlag = 0;

                                              if (buttonFlag == 0) //button ta ase play mode e
                                              {
                                                  if (mediaPlayer.isPlaying()) {
                                                      mediaPlayer.setVolume(0, 0);
                                                  }
                                                  button.setBackgroundResource(R.drawable.logo_play_3);
                                              } else //button ta ase pause mode e
                                              {
                                                  button.setBackgroundResource(R.drawable.logo_pause);
                                                  if (mediaPlayer.isPlaying()) {
                                                      //mediaPlayer.stop();
                                                      mediaPlayer.setVolume(1, 1);
                                                  }
                                              }
                                          }
                                          else
                                          {
                                              Snackbar snackbar = Snackbar.make(coordinatorLayout, "Connecting to internet. Please wait", Snackbar.LENGTH_LONG);
                                              snackbar.show();
                                          }
                                      }
                                  }
        );


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                if(mediaPlayer == null)
                {
                    if(initMedia == null)
                    {
                        initMedia = new asyncInit(1);
                        Tasync = new Thread(initMedia);
                        Tasync.start();
                    }
                }
                else if (mediaPlayer.isPlaying()) {
                    //System.out.println("I am playing");
                }
                else
                {
                    System.out.println("Not Playing");

                    mediaPlayer.stop();
                    initMedia = new asyncInit(2);
                    Tasync = new Thread(initMedia);
                    Tasync.start();
               }
                } catch (Exception e) {
                    System.out.println("aoisjdiojasod" + e);
                    mediaPlayer = null;
                    initMedia = new asyncInit(1);
                    Tasync = new Thread(initMedia);
                    Tasync.start();
                }
                // and here comes the "trick"
                handler.postDelayed(this, 100);
            }
        };
        handler.postDelayed(runnable, 10);


        client = new ClientThread();
        T = new Thread(client);
        T.start();

        final Handler handler2 = new Handler();
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {

                int flg = 5;

                if (T.isAlive()) {
                    handler2.postDelayed(this, 10);
                }
                else
                {
                    //while (T.isAlive()) ;

                    String str = "";
                    while (!str.startsWith("<finish>")) {
                        str = client.recieve();
                        //System.out.println(str);
                        if (str.startsWith("<song>")) {
                            songName.setText(str.substring(6));
                            curSong = str.substring(6);
                        }

                        else if (str.startsWith("<artist>"))
                        {
                            artistName.setText(str.substring(8));
                            curArtist = str.substring(8).replace("Artist: ","");
                        }
                        else if (str.startsWith("<genre>")) //added code here, previously just genreName.setText(str.substring(7).toUpperCase());
                        {
                            String sub = str.substring(7);
                            genreName.setText(sub.toUpperCase());
                            curGenre = sub;

                            if (sub.startsWith("semi-classical")) {
                                collapsingToolbar.setBackgroundResource(R.drawable.semi_classical);
                            }
                            else if (sub.contains("request")) {
                                collapsingToolbar.setBackgroundResource(R.drawable.bot4);
                            }
                            else if (sub.startsWith("RJ")) {
                                collapsingToolbar.setBackgroundResource(R.drawable.bot1);
                            }
                            else if (sub.startsWith("rock")) {
                                collapsingToolbar.setBackgroundResource(R.drawable.rock_back);
                            }
                                //collapsingToolbar.setBackgroundResource(R.drawable.classical);
                            else
                                collapsingToolbar.setBackgroundResource(getResources().getIdentifier(sub, "drawable", getPackageName()));

                        } else if (str.startsWith("<lyric>")) {
                            str = str.replace("$", "\n");
                            //System.out.println(str);
                            curLyric = str.substring(7);
                            lyricsField.setText(str.substring(7) + "\n\n\n\n");
                        }

                        if (str.length() == 0) flg--;
                        if (flg <= 0) {
                            System.out.println("genjam");
                            break;
                        }
                }


                client = new ClientThread();
                T = new Thread(client);
                T.start();
                if(flg <= 0 )
                {
                    handler2.postDelayed(this, 100);
                }

                else
                {
                    //System.out.println("got data");
                    handler2.postDelayed(this, 5000);
                }
            }
        // and here comes the "trick"

            }
        };
        handler2.postDelayed(runnable2, 10);

    }


    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Currently Streaming");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: clicked Menu item " + item);
                switch (item.getItemId()){
                    case R.id.profileMenu:
                        Log.d(TAG, "onMenuItemClick: Navigating to Profile Preferences");
                }
                return false;
            }
        });
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(item.isChecked()){
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        if(id== R.id.nav_credits){
            startActivity(new Intent(getApplicationContext(),credit_page.class));
        }
        else if(id== R.id.nav_rateus){
            startActivity(new Intent(getApplicationContext(), ApiLogin.class));

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class ClientThread implements Runnable {

        Queue sendMsg = new LinkedList();
        Queue recieveMsg = new LinkedList();
        String input, output;


        @Override
        public void run() {
            try {
                socket = new Socket(serverName, port);
                outToServer = socket.getOutputStream();
                out = new DataOutputStream(outToServer);

                out.writeUTF(token + " " + userName);

                inFromServer = socket.getInputStream();
                in = new DataInputStream(inFromServer);

                while (true) {
                    if (in.available() > 0) {
                        input = in.readUTF();
                        recieveMsg.offer(input);
                        if(input.startsWith("<finish>"))
                            break;
                    }
                    Thread.sleep(100);
                }

                socket.close();
            } catch (Exception e) {
                System.out.println(e);
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Connection Problem....Reconnecting", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        void send (String str)
        {
            if(str.length() > 0 )
                sendMsg.offer(str);
        }

        String recieve()
        {
            String str = "";
            if(!recieveMsg.isEmpty())
                str = (String) recieveMsg.remove();

            return str;
        }
    }

    class asyncInit implements Runnable {

        MediaPlayer mP;
        asyncInit(int type)
        {
            if (type == 1)
            {
                mP = new MediaPlayer();
            }
            else
            {
                mP = mediaPlayer;
                mediaPlayer = null;
            }
        }
        @Override
        public void run() {
            try {
                mP.setDataSource(url);
                mP.prepare();
                mP.start();
                mP.setVolume(buttonFlag,buttonFlag);
                mediaPlayer = mP;
            }
             catch (Exception e) {
                System.out.println("async" + e);
            }
        }
    }
}
