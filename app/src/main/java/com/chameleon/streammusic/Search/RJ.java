package com.chameleon.streammusic.Search;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chameleon.streammusic.R;
import com.chameleon.streammusic.Utils.BottomNavigationViewHelper;
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

public class RJ extends AppCompatActivity {

    private static final String TAG = "RJ";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = RJ.this;

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
    LinearLayout linearLayout;

    static String msgALLcontent = "RJ: Hey It's RJ Meow. Want to listen to the song you want to hear now?" +
            "Just send me a message and I will play it for you :D. " +
            "Song request format\nREQ_<your name>_<artist name>_<song name>" +
            "Example: REQ_Shuhan Mirza_Linkin Park_Numb"+"\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rj);
       // linearLayout = (LinearLayout) findViewById(R.id.chatlayout);
        //linearLayout.setOrientation(LinearLayout.VERTICAL);
        //setContentView(linearLayout);
        //linearLayout.setOrientation(LinearLayout.VERTICAL);
        Log.d(TAG, "onCreate: started");
        setupBottomNavigationView();


        final TextView msgAll = (TextView) findViewById(R.id.msgAll);
        final Button button = (Button) findViewById(R.id.button1);
        final EditText msg = (EditText) findViewById(R.id.msg);

        msgAll.setText(msgALLcontent);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


//                TextView newTextView = new TextView(RJ.this);
//                newTextView.setText("my name");
//                linearLayout.addView(newTextView);

                String str = msg.getText().toString();
                msgAll.append("\nYou: "+ str);
                msg.setText("");

                msgFlg = 1;

                if(T == null || !T.isAlive()) {
                    client = new ClientThread(str);
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

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /* do what you need to do */
                try{
                    if(msgFlg == 1)
                    {
                        if(!T.isAlive())
                        {
                            String str = recieve();

                            msgAll.append("\nRJ: " + str);

                            if (str.length() > 0) msgFlg = 0;
                        }
                    }
                }catch (Exception ex)
                {
                    msgAll.append( ex.toString());
                }
               // if(msgFlg == 1) System.out.println("waiting for msg");

                /* and here comes the "trick" */
                handler.postDelayed(this, 100);
            }
        };

        handler.postDelayed(runnable, 100);
        //http://www.mopri.de/2010/timertask-bad-do-it-the-android-way-use-a-handler/
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

    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
