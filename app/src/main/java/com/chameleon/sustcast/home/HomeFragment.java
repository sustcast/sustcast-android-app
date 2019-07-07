package com.chameleon.sustcast.home;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chameleon.streammusic.R;

import java.io.IOException;

public class HomeFragment extends Fragment {
    private static final String TAG = "Home Fragment";
    Button b_play;

    MediaPlayer mediaPlayer;

    boolean prepared = false;
    boolean started = false;
    String stream = "http://103.84.159.230:8000/sustcast";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new HomeFragment.PlayerTask().execute(stream);

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
            mediaPlayer.start();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(started) {
            mediaPlayer.pause();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(started) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(prepared){
            mediaPlayer.release();
        }
    }

}
