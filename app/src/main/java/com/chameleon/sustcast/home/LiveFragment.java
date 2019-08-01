package com.chameleon.sustcast.home;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.data.model.Current;
import com.chameleon.sustcast.data.model.OuterCurrent;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.CurrentClient;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LiveFragment extends Fragment {


    //Stream Link

    final static String stream = "http://103.84.159.230:8000/sustcast?type=.mpeg";

    //Layout Resources

    ImageButton b_play;
    TextView songName;
    TextView artistName;
    TextView lyricsView;
    //Instance Identifier

    private CurrentClient mAPIService;
    private ProgressBar progressBar;
    private ProgressBar LoadingMusic;
    private Context context;
    private Handler mHandler;
    private int mInterval;
    private View view;

    //EXO

    private SimpleExoPlayer mPlayer;
    private Uri uri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_live, container, false);

            context = getContext();

            //Resource Initializer
            b_play = view.findViewById(R.id.playButton);
            songName = view.findViewById(R.id.tv_song);
            artistName = view.findViewById(R.id.tv_artist);
            progressBar = view.findViewById(R.id.progressBar);
            AdView mAdView = view.findViewById(R.id.adView);
            lyricsView =view.findViewById(R.id.lyricsView);
            mAPIService = ApiUtils.getMetadataService();
            uri = Uri.parse(stream);
            mInterval = 1000;
            lyricsView.setMovementMethod(new ScrollingMovementMethod());
            setRetainInstance(true);
            //Creates ExoPlayer instance
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            new LiveFragment.PlayerTask().execute();
            mHandler = new Handler();
            catchMetadata();
            startRepeatingTask();
        return view;
    }
class PlayerTask extends AsyncTask<String,Void,Boolean> {

    @Override
    protected Boolean doInBackground(String... strings) {
        mediaPlayerLaunch();
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        b_play.setOnClickListener(view -> {
            if(isPlaying()) {
                b_play.setImageResource(R.mipmap.ic_play_b);
                pause();
            }
            else{
                b_play.setImageResource(R.mipmap.ic_pause_b);
                play();
            }
        });
    }
}

    public void catchMetadata() {
        mAPIService.fetch().enqueue(new Callback<OuterCurrent>() {
            @Override
            public void onResponse(@NotNull Call<OuterCurrent> call, @EverythingIsNonNull Response<OuterCurrent> response) {
                System.out.println("Response code =>" + response.code());
                if (response.isSuccessful()) {
                    Log.i("MY", "Response Metadata successful");
                    List<Current> currentList = response.body().getCurrent();
                    String artist = currentList.get(0).getArtist();
                    String songtitle = currentList.get(0).getSong();
                    Double progress = currentList.get(0).getProgress();
                    String lyric = currentList.get(0).getLyric();
                    String some = Double.toString(progress);
                    Log.i("Progress", some);
                    songName.setText(songtitle);
                    artistName.setText(artist);
                    lyricsView.setText(lyric);
                    progressBar.setProgress((int) Math.round(progress));
                } else {
                    Log.i("MY", "Response Metadata NOT successful");
                    System.out.println("JSON => " + response.body());
                    System.out.println("RESPONSE: " + response.toString());
                }
            }
            @Override
            public void onFailure(@EverythingIsNonNull Call<OuterCurrent> call, Throwable t) {
                Log.i("MY", "Response Metadata FAILED");
            }
        });
    }

    private void startRepeatingTask(){
        mStatusChecker.run();
    }
    private void stopRepeatingTask(){
        mHandler.removeCallbacks(mStatusChecker);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try{
                updateHTML();
            }
            finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    private void mediaPlayerLaunch(){
        mPlayer = ExoPlayerFactory.newSimpleInstance(context);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "SUSTCast"));
        MediaSource audioStream = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        mPlayer.prepare(audioStream);
        mPlayer.setPlayWhenReady(true);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mPlayer.seekTo(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        stopRepeatingTask();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private boolean isPlaying() {
        return mPlayer != null
                && mPlayer.getPlaybackState() != Player.STATE_ENDED
                && mPlayer.getPlaybackState() != Player.STATE_IDLE
                && mPlayer.getPlayWhenReady();
    }
    public void pause(){
        mPlayer.setPlayWhenReady(false);
        mPlayer.getPlaybackState();
    }
    public void play(){
        mPlayer.seekTo(0);
        mPlayer.setPlayWhenReady(true);
        mPlayer.getPlaybackState();
    }
    private void updateHTML() {
        catchMetadata();
    }
}