package com.guduodemo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guduodemo.listener.DownLoadListener;
import com.guduodemo.net.DownLoadFileUtils;
import com.guduodemo.net.HttpEngine;
import com.guduodemo.utils.FileUtils;
import com.guduodemo.R;
import com.guduodemo.utils.RecordUtils;
import com.guduodemo.receiver.HeadsetReceiver;
import com.guduodemo.widget.CustomVideoView;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomVideoView.OnCorveHideListener, MediaPlayer.OnCompletionListener, HeadsetReceiver.HeadSetConnectListener, View.OnClickListener {
    private CustomVideoView mVideoView;
    private RelativeLayout mControlView;
    private int mIndex = 0;
    private HeadsetReceiver mReceiver;
    private boolean mConnectedHeadset;
    private AudioManager mAudioManager;
    private Handler mHandler = new Handler();
    private int mIndexTwoProgress;
    private boolean mStopProgress;
    private SecondVideoRunnable mRunnable;


    //step one
    private TextView mDownloadProgressTv;
    private int mDownloadIndex;  //下载文件位置
    private List<String> mDownLoadUrls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = (CustomVideoView) findViewById(R.id.videoView);
        mControlView = (RelativeLayout) findViewById(R.id.control);
        mVideoView.setOnCorveHideListener(this);
        mVideoView.setOnCompletionListener(this);
        initView();
    }

    private void initView() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setPlaySource(R.raw.video1);
        mVideoView.start();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        mReceiver = new HeadsetReceiver(this);
        registerReceiver(mReceiver, filter);
        stepOneView();
    }


    private void setPlaySource(int id) {
        //设置播放加载路径
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + id));
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (mIndex) {
            case 0:
                mVideoView.start();
                break;
            case 1:
                boolean audioPermission = RecordUtils.isHasPermission(this);
                if (audioPermission && mConnectedHeadset) {
                    if (mRunnable == null) {
                        mRunnable = new SecondVideoRunnable();
                        mHandler.post(mRunnable);
                    }
                    mVideoView.start();
                }
                break;
        }

    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        mVideoView.pause();
        mStopProgress = true;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void requestHide() {
        //隐藏第一帧图片(需要切图)

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //播放完成,判断当前进度

        switch (mIndex) {
            case 0: //播放第二段，监听耳机插入状态
                mIndex++;
                setPlaySource(R.raw.video2);
                check();
                break;
            case 1: //播放第三段
                mIndex++;
//                mRippleView.stop();
//                mRecordLayout.setVisibility(View.GONE);
                break;
        }
    }


    //检查耳机
    private void check() {
        mConnectedHeadset = mAudioManager.isWiredHeadsetOn();
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
        } else {
            boolean recordPermission = RecordUtils.isHasPermission(this);
            if (recordPermission) {
                mRunnable = new SecondVideoRunnable();
                mHandler.post(mRunnable);
                mVideoView.start();
            }
        }
    }

    @Override
    public void headsetState(boolean connectState) {
        mConnectedHeadset = connectState;
        switch (mIndex) {
            case 1: //检测到耳机，播放第二段
                if (connectState) {
                    boolean audioPermission = RecordUtils.isHasPermission(this);
                    if (audioPermission) {
                        mRunnable = new SecondVideoRunnable();
                        mHandler.post(mRunnable);
                        mVideoView.start();
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.record_layout:  //录音
//                mRippleView.start();
//                break;
        }
    }


    private class SecondVideoRunnable implements Runnable {

        @Override
        public void run() {
            if (mIndex != 1) {
                return;
            }

            if (mIndexTwoProgress == 16) {
//                mRecordLayout.setVisibility(View.VISIBLE);
                return;
            }

            if (!mStopProgress) {
                mIndexTwoProgress++;
            }
            mHandler.postDelayed(this, 1000);
        }
    }


    public void downLoad() {
        new Thread() {
            @Override
            public void run() {
                String url = mDownLoadUrls.get(mDownloadIndex);
                String fileName = FileUtils.getFileName(url);
                String downloadPath = FileUtils.getDownLoadPath(MainActivity.this) + File.separator + fileName;
                HttpEngine.downFile(downloadPath, url, new DownLoadListener() {
                    @Override
                    public void onProgress(int progress, int total) {
                        Log.e("gqiu", "进度：progress=" + (float) (progress / total) * 100);

                      final  int percent = (int) ((float) progress / total * 100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mDownloadProgressTv.setText(percent + "%正在下载...");
                            }
                        });

                    }

                    @Override
                    public void onDownLoaded(String path) {
                        if (mDownloadIndex < mDownLoadUrls.size() - 1) {
                            mDownloadIndex++;
                            downLoad();
                        } else {
                            //下载完成

                        }
                        Log.e("gqiu", "下载完成");
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        }.start();
    }


    //步骤1-view
    private void stepOneView() {
        View stepView = LayoutInflater.from(this).inflate(R.layout.item_guide_step_one, mControlView, false);
        mDownloadProgressTv = (TextView) stepView.findViewById(R.id.download_progress_tv);
        mDownLoadUrls = DownLoadFileUtils.getDownLoadUrlList();
        mControlView.addView(stepView);
        downLoad();
    }
}
