package com.chameleon.sustcast.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chameleon.streammusic.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by HEMAYEET on 1/3/2018.
 */

public class LiveFragment extends Fragment {
    private static final String TAG = "Live Fragment";
    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        CurrentSong();
        return view;
    }
    public void CurrentSong() {

        try
        {
            URL url = new URL("http://103.84.159.230:8000/sustcast/currentsong?sid=#");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);

            in.close();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
}
