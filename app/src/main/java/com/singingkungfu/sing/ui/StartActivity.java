package com.singingkungfu.sing.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.singingkungfu.sing.R;
import com.singingkungfu.sing.widget.CustomVideoView;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {
    private CustomVideoView mVideoView;
    private List<String> mData;
    private boolean mStop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mVideoView = (CustomVideoView) findViewById(R.id.videoView);
        mVideoView.setOnCompletionListener(this);
        findViewById(R.id.close_iv).setOnClickListener(this);
        init();
    }

    private void init() {
        mData = getIntent().getStringArrayListExtra(DownLoadResourceActivity.DATA);
        mVideoView.setVideoURI(Uri.parse(mData.get(0)));
        mVideoView.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mStop) {
            mStop = false;
            mVideoView.seekTo(0);
            mVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        mStop = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                finish();
                break;
        }
    }


    private void jump() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putStringArrayListExtra(DownLoadResourceActivity.DATA, (ArrayList<String>) mData);
        startActivity(intent);
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        jump();
    }
}
