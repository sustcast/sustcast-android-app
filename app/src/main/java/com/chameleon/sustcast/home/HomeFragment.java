package com.chameleon.sustcast.home;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.data.model.Current;
import com.chameleon.sustcast.data.model.OuterCurrent;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.CurrentClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "Home Fragment";
    Button b_play;
    private CurrentClient mAPIService;


    MediaPlayer mediaPlayer;

    boolean prepared = false;
    boolean started = false;
    String stream = "http://103.84.159.230:8000/sustcast";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAPIService = ApiUtils.getMetadataService();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new HomeFragment.PlayerTask().execute(stream);
       catchMetadata();
        return view;
    }


    public void catchMetadata(){
        mAPIService.fetch().enqueue(new Callback<OuterCurrent>() {
            @Override
            public void onResponse(Call<OuterCurrent> call, Response<OuterCurrent> response) {
                System.out.println("Response code =>" + response.code());
                //System.out.println("JSON => " + new GsonBuilder().setPrettyPrinting().create().toJson(response));
                if (response.isSuccessful()) {
                    Log.i("MY", "Response Metadata successful");
                    List<Current> currentList = response.body().getCurrent();
                    String artist = currentList.get(0).getArtist();
                    String songtitle = currentList.get(0).getSong();
                    String genre = currentList.get(0).getGenre();
                    String lyric = currentList.get(0).getLyric();
                    Log.i("MY", "ARTIST here => "+ currentList.get(0).getLyric());

                } else {
                    Log.i("MY", "Response Metadata NOT successful");
                    System.out.println("JSON => " +response.body());
                    System.out.println("RESPONSE: " + response.toString());

                }


            }

            @Override
            public void onFailure(Call<OuterCurrent> call, Throwable t) {
                Log.i("MY", "Response Metadata FAILED");

            }
        });

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
