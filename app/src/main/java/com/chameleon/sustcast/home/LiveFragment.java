package com.chameleon.sustcast.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.data.model.OuterXSL;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.UserClient;
import com.chibde.visualizer.CircleBarVisualizer;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HEMAYEET on 1/3/2018.
 */

public class LiveFragment extends Fragment {
    private static final String TAG = "Live Fragment";
    ImageButton b_play;
    final static String stream = "http://103.84.159.230:8000/sustcast";
    private UserClient mAPIService;
    MediaPlayer mediaPlayer;
    private Timer autoUpdate;
    TextView songName;
    TextView artistName;
    CircleBarVisualizer circleBarVisualizer;
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 102;
    public static final String[] WRITE_EXTERNAL_STORAGE_PERMS = {
            Manifest.permission.RECORD_AUDIO
    };

    boolean prepared = false;
    boolean started = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        circleBarVisualizer = view.findViewById(R.id.visualizer);
        b_play = (ImageButton) view.findViewById(R.id.playButton);
        AdView mAdView = view.findViewById(R.id.adView);
        songName = view.findViewById(R.id.tv_song);
        artistName = view.findViewById(R.id.tv_artist);

        mAPIService = ApiUtils.getAPIService();

        mediaPlayer = new MediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED) {

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                circleBarVisualizer.setColor(ContextCompat.getColor(getActivity(), R.color.colorPink));
                new LiveFragment.PlayerTask().execute(stream);
                circleBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
                catchMetadata();
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    Toast.makeText(getActivity(), "App required access to audio", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(WRITE_EXTERNAL_STORAGE_PERMS, AUDIO_PERMISSION_REQUEST_CODE);
            }

        } else {
            Log.e(TAG, "onCreateView: Dont");
        }
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }

    class PlayerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
            }  catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aboolean) {
            super.onPostExecute(aboolean);
            b_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mediaPlayer.isPlaying()) {
                        b_play.setImageResource(R.drawable.ic_play);
                        mediaPlayer.pause();
                    }
                    else{
                        b_play.setImageResource(R.mipmap.ic_pause);
                        mediaPlayer.start();
                    }
                }
            });

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        "Application will not have audio on record", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void catchMetadata(){
        mAPIService.fetch().enqueue(new Callback<OuterXSL>() {
            @Override
            public void onResponse(Call<OuterXSL> call, Response<OuterXSL> response) {
                System.out.println("Response Code=>"+ response.code());
                if(response.isSuccessful()){
                    Log.i("Tag", "Response Metadata Received");
                    Log.i("Tag","POST submitted to API"+ response.body().getIcestats().getSource().getTitle());
                    String gotcha= response.body().getIcestats().getSource().getTitle();
                    String[] separate = gotcha.split("-");
                    String artist = separate[0];
                    String song = separate[1];
                    songName.setText(song);
                    artistName.setText(artist);
                }
                else {
                    Log.i("Tag","Failed to Receive Data");
                }
            }

            @Override
            public void onFailure(Call<OuterXSL> call, Throwable t) {
                Log.i("Tag","Failed to catch data");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        updateHTML();
                    }
                });
            }
        }, 0, 2000); // updates each 40 secs
    }

    private void updateHTML(){
        catchMetadata();
    }


}