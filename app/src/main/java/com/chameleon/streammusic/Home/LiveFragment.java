package com.chameleon.streammusic.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chameleon.streammusic.R;

/**
 * Created by HEMAYEET on 1/3/2018.
 */

public class LiveFragment extends Fragment {
    private static final String TAG = "Live Fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);

        return view;
    }
}
