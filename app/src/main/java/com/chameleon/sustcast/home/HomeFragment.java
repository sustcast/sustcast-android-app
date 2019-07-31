package com.chameleon.sustcast.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.data.model.Current;
import com.chameleon.sustcast.data.model.OuterCurrent;
import com.chameleon.sustcast.data.remote.ApiUtils;
import com.chameleon.sustcast.data.remote.CurrentClient;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HEMAYEET on 1/3/2018.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "Home Fragment";
    TextView lyrics_text;
    private CurrentClient mAPIService;
    private Timer autoUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAPIService = ApiUtils.getMetadataService();
        lyrics_text = view.findViewById(R.id.lyrics);
        lyrics_text.setMovementMethod(new ScrollingMovementMethod());
        setAutoUpdate();
        return view;
    }


    public void catchMetadata() {
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
                    Double progress = currentList.get(0).getProgress();
                    String some = Double.toString(progress);
                    //Log.i("MY", "ARTIST here => "+ currentList.get(0).getLyric());
                    Log.i("Progess", some);
                    String[] separate = songtitle.split("-");
                    lyrics_text.setText(lyric);
                } else {
                    Log.i("MY", "Response Metadata NOT successful");
                    System.out.println("JSON => " + response.body());
                    System.out.println("RESPONSE: " + response.toString());

                }

            }

            @Override
            public void onFailure(Call<OuterCurrent> call, Throwable t) {
                Log.i("MY", "Response Metadata FAILED");

            }
        });

    }


    public void setAutoUpdate() {
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    getActivity().runOnUiThread(() -> updateHTML());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000); // updates each 40 secs
    }

    private void updateHTML() {
        catchMetadata();
    }


}