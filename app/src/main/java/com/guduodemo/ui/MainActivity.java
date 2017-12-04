package com.guduodemo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guduodemo.R;
import com.guduodemo.net.DownLoadFileUtils;
import com.guduodemo.net.NetUtils;
import com.guduodemo.receiver.HeadsetReceiver;
import com.guduodemo.utils.RecordUtils;
import com.guduodemo.task.ScreenFiveTask;
import com.guduodemo.task.ScreenFourTask;
import com.guduodemo.task.ScreenOneTask;
import com.guduodemo.task.ScreenSixTask;
import com.guduodemo.task.ScreenThreeTask;
import com.guduodemo.task.ScreenTwoTask;
import com.guduodemo.widget.CustomVideoView;
import com.guduodemo.widget.MainRippleView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomVideoView.OnCorveHideListener, MediaPlayer.OnCompletionListener, HeadsetReceiver.HeadSetConnectListener, View.OnClickListener, DownLoadFileUtils.DownLoadedListener {
    private CustomVideoView mVideoView;
    private RelativeLayout mControlView;
    private int mIndex = -1;
    private HeadsetReceiver mReceiver;
    private boolean mConnectedHeadset;
    private AudioManager mAudioManager;
    private Handler mHandler = new Handler();
    private ScreenOneTask mScreenOneTask;
    private ScreenTwoTask mScreenTwoTask;
    private ScreenThreeTask mScreenThreeTask;
    private ScreenFourTask mScreenFourTask;
    private ScreenFiveTask mScreenFiveTask;
    private ScreenSixTask mScreenSixTask;

    //step zero
    private TextView mDownloadProgressTv;
    private List<String> mCaches;

    //录音
    private MainRippleView mRippleView;
    private ImageView mRecordView;

    //step six
    private TextView analyzeVoiceTv;

    //step seven
    private ImageView mAvatarIv;
    private TextView mNameTv;
    private TextView mScoreTv;
    private ProgressBar mHighProgressBar, mLowProgressBar, mIntonationProgressBar, mBreathProgressBar, mRhythmProgressBar;
    private TextView mHighScoreTv, mLowScoreTv, mIntonationScoreTv, mBreathScoreTv, mRhythmScoreTv;


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
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        mReceiver = new HeadsetReceiver(this);
        registerReceiver(mReceiver, filter);

        //下载视频文件
        DownLoadFileUtils utils = new DownLoadFileUtils(this);
        List<String> cacheFiles = utils.getCacheFiles();
        if (cacheFiles.size() == 0) {
            downloadView();

            //需要下载
            if (NetUtils.isConnect(this)) {
                //网络错误
                mDownloadProgressTv.setText(R.string.network_error);
                return;
            }
            utils.download(mDownloadProgressTv, this);

        } else {
            //直接开始播放第0个片段
            mCaches = cacheFiles;
            playStepZeroView();
        }
    }


    private void setPlaySource(String path) {
        //设置播放加载路径
        mVideoView.setVideoURI(Uri.parse(path));
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        mVideoView.pause();
        switch (mIndex) {
            case 1:
                mScreenOneTask.isStop(true);
                break;
            case 2:
                mScreenTwoTask.isStop(true);
                break;
            case 3:
                mScreenThreeTask.isStop(true);
                break;
            case 4:
                mScreenFourTask.isStop(true);
                break;
            case 5:
                mScreenFiveTask.isStop(true);
                break;
        }
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
        switch (mIndex) {
            case 1:
                mScreenOneTask.isStop(false);
                break;
            case 2:
                mScreenTwoTask.isStop(false);
                break;
            case 3:
                mScreenThreeTask.isStop(false);
                break;
            case 4:
                mScreenFourTask.isStop(false);
                break;
            case 5:
                mScreenFiveTask.isStop(false);
                break;
        }
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
        if (mIndex == -1) {
            mVideoView.start();
            return;
        }
        //播放完成,判断当前进度
        mVideoView.pause();
        switch (mIndex) {
            case 1: //播放第1段，监听耳机插入状态
                mHandler.removeCallbacks(mScreenOneTask);
                addEndView();
                break;
            case 2: //播放第2段
                mHandler.removeCallbacks(mScreenTwoTask);
                addEndView();
                break;
            case 3: //播放第3段
                mHandler.removeCallbacks(mScreenThreeTask);
                addEndView();
                break;
            case 4: //播放第4段
                mHandler.removeCallbacks(mScreenFourTask);
                addEndView();
                break;
            case 5: //播放第5段
                mHandler.removeCallbacks(mScreenFiveTask);
                addEndView();
                break;
            case 6: //播放第6段
                mHandler.removeCallbacks(mScreenSixTask);
                playStepSevenView();
                break;
        }
    }


    //检查耳机,录音权限
    private boolean check() {
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
            return false;
        } else {
            return RecordUtils.isHasPermission(this);
        }
    }

    @Override
    public void headsetState(boolean connectState) {
        mConnectedHeadset = connectState;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_test_btn: //开始测试
                if (!check()) {
                    return;
                }
                mIndex = 1;
                playStepOneView();
                break;
            case R.id.reset_test_btn:   //重新测试
                mIndex = playStepOneView();
                break;
            case R.id.share_iv:         //分享

                break;
            case R.id.finish_btn:      //完成测试

                break;
            case R.id.reset_btn:       //重新开始
                switch (mIndex) {
                    case 1:
                        mIndex = playStepOneView();
                        break;
                    case 2:
                        mIndex = playStepTwoView();
                        break;
                    case 3:
                        mIndex = playStepThreeView();
                        break;
                    case 4:
                        mIndex = playStepFourView();
                        break;
                    case 5:
                        mIndex = playStepFiveView();
                        break;
                }
                break;
            case R.id.next_btn:          //下一关
                switch (mIndex) {
                    case 1:
                        mIndex = playStepTwoView();
                        break;
                    case 2:
                        mIndex = playStepThreeView();
                        break;
                    case 3:
                        mIndex = playStepFourView();
                        break;
                    case 4:
                        mIndex = playStepFiveView();
                        break;
                    case 5:
                        mIndex = playStepSixView();
                        break;
                }
                break;
        }
    }

    @Override
    public void downLoaded(List<String> result) {
        mCaches = result;
        playStepZeroView();
    }

    @Override
    public void downLoadFailure() {
        Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
    }


    //下载view-下载进度
    private void downloadView() {
        View stepView = LayoutInflater.from(this).inflate(R.layout.item_download, mControlView, false);
        mDownloadProgressTv = (TextView) stepView.findViewById(R.id.download_progress_tv);
        mControlView.addView(stepView);
        setPlaySource("android.resource://" + getPackageName() + "/" + R.raw.start_video);
        mVideoView.start();
    }


    //步骤0-view-开始测试
    private void stepZeroView() {
        View stepView = LayoutInflater.from(this).inflate(R.layout.item_guide_step_zero, mControlView, false);
        stepView.findViewById(R.id.start_test_btn).setOnClickListener(this);
        mControlView.addView(stepView);
    }


    private void stepRecordView() {
        View stepView = LayoutInflater.from(this).inflate(R.layout.item_record, mControlView, false);
        mRippleView = (MainRippleView) stepView.findViewById(R.id.rippleView);
        mRecordView = (ImageView) stepView.findViewById(R.id.start_record_btn);
        mRecordView.setOnClickListener(this);
        mControlView.addView(stepView);
    }


    private void addEndView() {
        removeCurrentStep();
        View stepView = LayoutInflater.from(this).inflate(R.layout.item_end_view, mControlView, false);
        stepView.findViewById(R.id.reset_btn).setOnClickListener(this);
        stepView.findViewById(R.id.next_btn).setOnClickListener(this);
        if (mIndex == 5) {
            ((Button) stepView.findViewById(R.id.next_btn)).setText(R.string.test_finsih);
        }
        mControlView.addView(stepView);
    }

    private void addAnalyzeVoiceView() {
        removeCurrentStep();
        View stepView = LayoutInflater.from(this).inflate(R.layout.item_analyze_voice, mControlView, false);
        analyzeVoiceTv = (TextView) stepView.findViewById(R.id.analyze_voice_tv);
        mControlView.addView(stepView);
    }

    private void removeCurrentStep() {
        mControlView.removeAllViews();
    }


    //播放片段0
    private void playStepZeroView() {
        mIndex = 0;
        setPlaySource(mCaches.get(0));
        mVideoView.start();
        removeCurrentStep();
        stepZeroView();
    }


    //播放片段1
    private int playStepOneView() {
        boolean isCheck = check();
        if (!isCheck) {
            return mIndex;
        }

        setPlaySource(mCaches.get(1));
        mVideoView.start();
        removeCurrentStep();
        stepRecordView();

        mScreenOneTask = new ScreenOneTask(this, mRippleView, mRecordView, mHandler);
        mHandler.post(mScreenOneTask);
        mVideoView.start();
        return 1;
    }

    //播放片段2
    private int playStepTwoView() {
        boolean isCheck = check();
        if (!isCheck) {
            return mIndex;
        }

        setPlaySource(mCaches.get(2));
        mVideoView.start();
        removeCurrentStep();
        stepRecordView();

        mScreenTwoTask = new ScreenTwoTask(this, mRippleView, mRecordView, mHandler);
        mHandler.post(mScreenTwoTask);
        mVideoView.start();
        return 2;
    }

    //播放片段3
    private int playStepThreeView() {
        boolean isCheck = check();
        if (!isCheck) {
            return mIndex;
        }

        setPlaySource(mCaches.get(3));
        mVideoView.start();
        removeCurrentStep();
        stepRecordView();

        mScreenThreeTask = new ScreenThreeTask(this, mRippleView, mRecordView, mHandler);
        mHandler.post(mScreenThreeTask);
        mVideoView.start();

        return 3;
    }

    //播放片段4
    private int playStepFourView() {
        boolean isCheck = check();
        if (!isCheck) {
            return mIndex;
        }

        setPlaySource(mCaches.get(4));
        mVideoView.start();
        removeCurrentStep();
        stepRecordView();

        mScreenFourTask = new ScreenFourTask(this, mRippleView, mRecordView, mHandler);
        mHandler.post(mScreenFourTask);
        mVideoView.start();

        return 4;
    }

    //播放片段5
    private int playStepFiveView() {
        boolean isCheck = check();
        if (!isCheck) {
            return mIndex;
        }

        setPlaySource(mCaches.get(5));
        mVideoView.start();
        removeCurrentStep();
        stepRecordView();

        mScreenFiveTask = new ScreenFiveTask(this, mRippleView, mRecordView, mHandler);
        mHandler.post(mScreenFiveTask);
        mVideoView.start();
        return 5;
    }

    //播放片段6
    private int playStepSixView() {
        boolean isCheck = check();
        if (!isCheck) {
            return mIndex;
        }

        setPlaySource(mCaches.get(6));
        mVideoView.start();
        addAnalyzeVoiceView();

        mScreenSixTask = new ScreenSixTask(this, analyzeVoiceTv, mHandler);
        mHandler.post(mScreenSixTask);

        return 6;
    }


    //播放片段7

    private void playStepSevenView() {
        removeCurrentStep();
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_result, mControlView, false);
        mAvatarIv = (ImageView) itemView.findViewById(R.id.avatar_iv);
        itemView.findViewById(R.id.share_iv).setOnClickListener(this);
        mNameTv = (TextView) itemView.findViewById(R.id.name_tv);
        mScoreTv = (TextView) itemView.findViewById(R.id.score_tv);
        mHighProgressBar = (ProgressBar) itemView.findViewById(R.id.high_progressBar);
        mLowProgressBar = (ProgressBar) itemView.findViewById(R.id.low_progressBar);
        mIntonationProgressBar = (ProgressBar) itemView.findViewById(R.id.intonation_progressBar);
        mBreathProgressBar = (ProgressBar) itemView.findViewById(R.id.breath_progressBar);
        mRhythmProgressBar = (ProgressBar) itemView.findViewById(R.id.rhythm_progressBar);
        mHighScoreTv = (TextView) itemView.findViewById(R.id.high_score_tv);
        mLowScoreTv = (TextView) itemView.findViewById(R.id.low_score_tv);
        mIntonationScoreTv = (TextView) itemView.findViewById(R.id.intonation_score_tv);
        mRhythmScoreTv = (TextView) itemView.findViewById(R.id.rhythm_score_tv);
        mBreathScoreTv = (TextView) itemView.findViewById(R.id.breath_score_tv);

        itemView.findViewById(R.id.reset_test_btn).setOnClickListener(this);
        itemView.findViewById(R.id.finish_btn).setOnClickListener(this);

        mControlView.addView(itemView);
    }

}
