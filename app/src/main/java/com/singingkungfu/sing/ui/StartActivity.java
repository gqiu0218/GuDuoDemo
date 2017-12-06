package com.singingkungfu.sing.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.singingkungfu.sing.R;
import com.singingkungfu.sing.receiver.HeadsetReceiver;
import com.singingkungfu.sing.utils.RecordUtils;
import com.singingkungfu.sing.widget.CustomVideoView;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity implements View.OnClickListener, HeadsetReceiver.HeadSetConnectListener {
    private CustomVideoView mVideoView;
    private List<String> mData;
    private boolean mStop;
    private HeadsetReceiver mReceiver;
    private boolean mConnectedHeadset;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mVideoView = (CustomVideoView) findViewById(R.id.videoView);
        findViewById(R.id.start_btn).setOnClickListener(this);
        findViewById(R.id.close_iv).setOnClickListener(this);
        init();
    }

    private void init() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        mReceiver = new HeadsetReceiver(this);
        registerReceiver(mReceiver, filter);

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
            case R.id.start_btn:
                if (!check()) {
                    return;
                }
                jump();
                break;
            case R.id.close_iv:
                exit();
                break;
        }
    }


    private void jump() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putStringArrayListExtra(DownLoadResourceActivity.DATA, (ArrayList<String>) mData);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定要退出吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void headsetState(boolean connectState) {
        mConnectedHeadset = connectState;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }


    //检查耳机,录音权限
    private boolean check() {
        mConnectedHeadset = mAudioManager.isWiredHeadsetOn();
        mConnectedHeadset = true;
        if (!mConnectedHeadset) {
            //不存在耳机
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("检测到没有耳机，请插入耳机");
            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return false;
        } else {
            return RecordUtils.isHasPermission(this);
        }
    }

}
