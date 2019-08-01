package com.chameleon.sustcast.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chameleon.streammusic.R;
import com.chameleon.sustcast.chatHandler.RequestTask;

import java.util.UUID;

import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;


public class ChatFragment extends Fragment {
    private static final String TAG = ChatFragment.class.getSimpleName();
    private static final int USER = 10001;
    private static final int BOT = 10002;
    private static final int ACTIVITY_NUM = 1;
    AIRequest aiRequest;
    AIDataService aiDataService;
    private EditText queryEditText;
    private String uuid = UUID.randomUUID().toString();
    private AIServiceContext customAIServiceContext;
    private LinearLayout chatLayout;
    private Context mContext = getContext();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

}