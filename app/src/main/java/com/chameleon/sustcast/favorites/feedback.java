package com.chameleon.sustcast.favorites;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chameleon.sustcast.home.Home;
import com.chameleon.streammusic.R;
import com.chameleon.sustcast.utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;


public class feedback extends AppCompatActivity {

    private static final String TAG = "Feedback";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = feedback.this;


    static final String serverName = "103.84.159.230"; //
    static final int port = 50001;
    static OutputStream outToServer;
    public static DataOutputStream out;
    static InputStream inFromServer;
    public static DataInputStream in;
    static Socket socket;
    final String token = "siojdioajs21839712987391872ahsdhkjshkjdh21983912doiasoidoias";
    final String userName = "puhi";
    public static ClientThread client;
    public static Thread T;
    static int msgFlg = 0;
    final Queue recieveMsg = new LinkedList();

    static boolean active = false;

    static String preSong;
    static String preArtist;
    static String preGenre;
    static String feedbackLog;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();

        System.out.println("FeedBack Created");

        coordinatorLayout = findViewById(R.id.feedbackLayout);
        final Button likeButton = findViewById(R.id.like);
        final Button dislikeButton = findViewById(R.id.dislike);
        final TextView songName = findViewById(R.id.song);
        final TextView artistName = findViewById(R.id.artist);
        final TextView titleView = findViewById(R.id.top);
        final TextView buttonTitle = findViewById(R.id.txt1);


        if(feedbackLog == null)
        {
            feedbackLog = "0";
            preArtist = Home.curArtist;
            preSong = Home.curSong;

            likeButton.setVisibility(View.VISIBLE);
            dislikeButton.setVisibility(View.VISIBLE);
        }


        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try{
                    if(feedbackLog.startsWith("0") && !(Home.curGenre.startsWith("RJ") && Home.curGenre.length() == 2))
                    {
                        titleView.setText("You are Listening to:");
                        buttonTitle.setText("Was this the right time to play this kind of music?");

                        songName.setText(Home.curSong);
                        artistName.setText("Artist: " + Home.curArtist);
                    } else if ( Home.curGenre.startsWith("RJ") && Home.curGenre.length() == 2 ){
                        titleView.setText("Now our RJ is Talking");
                        buttonTitle.setText("");

                        songName.setText("");
                        artistName.setText("");

                        likeButton.setVisibility(View.INVISIBLE);
                        dislikeButton.setVisibility(View.INVISIBLE);
                    }
                    else {
                        titleView.setText("Thanks for your feedback.");
                        buttonTitle.setText("");

                        songName.setText("");
                        artistName.setText("");

                        likeButton.setVisibility(View.INVISIBLE);
                        dislikeButton.setVisibility(View.INVISIBLE);
                    }

                    if(!(Home.curArtist.contains(preArtist) && Home.curArtist.length() == preArtist.length() &&  Home.curSong.contains(preSong) && Home.curSong.length() == preSong.length() ))
                    {
                        System.out.println("song changed");
                        feedbackLog = "0";

                        preArtist = Home.curArtist;
                        preSong = Home.curSong;


                        titleView.setText("You are Listening to:");
                        buttonTitle.setText("Was this the right time to play this kind of music?");

                        songName.setText(Home.curSong);
                        artistName.setText(Home.curArtist);

                        likeButton.setVisibility(View.VISIBLE);
                        dislikeButton.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e)
                {
                    System.out.println(e + " in feedback");
                }

                if(active)
                    handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 10);

        likeButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {

                 System.out.println("user liked it");

                 feedbackLog = "1";

                 titleView.setText("Thanks for your feedback.");
                 buttonTitle.setText("");

                 songName.setText("");
                 artistName.setText("");

                 likeButton.setVisibility(View.INVISIBLE);
                 dislikeButton.setVisibility(View.INVISIBLE);

                 if(T == null || !T.isAlive()) {
                     client = new ClientThread("<FEEDBACK>"+token+"__"+ Home.curSong);
                     T = new Thread(client);
                     T.start();
                 }
                 else
                 {
                     System.out.println("bipod");
                 }
             }
             }
        );

        dislikeButton.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {

                                              System.out.println("user disliked it");

                                              feedbackLog = "1";

                                              titleView.setText("Thanks for your feedback.");
                                              buttonTitle.setText("");

                                              songName.setText("");
                                              artistName.setText("");

                                              likeButton.setVisibility(View.INVISIBLE);
                                              dislikeButton.setVisibility(View.INVISIBLE);
                                          }
                                      }
        );

    }



    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    class ClientThread implements Runnable {

        Queue sendMsg = new LinkedList();
        String input, output;
        String query;
        ClientThread(String query)
        {
            this.query = query;
        }
        @Override
        public void run() {
            try {
                socket = new Socket(serverName, port);
                outToServer = socket.getOutputStream();
                out = new DataOutputStream(outToServer);

                out.writeUTF(token + " " + userName);

                inFromServer = socket.getInputStream();
                in = new DataInputStream(inFromServer);

                out.writeUTF(query);

                while (in.available() <= 0);

                input = in.readUTF();
                recieveMsg.offer(input);

                socket.close();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String recieve()
    {
        String str = "";
        if(!recieveMsg.isEmpty())
            str = (String) recieveMsg.remove();

        return str;
    }
}
