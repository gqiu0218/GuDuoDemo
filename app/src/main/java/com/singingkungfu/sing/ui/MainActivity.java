package com.singingkungfu.sing.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singingkungfu.sing.R;
import com.singingkungfu.sing.dialog.ShareDialog;
import com.singingkungfu.sing.listener.AgainTestListener;
import com.singingkungfu.sing.listener.AnalyzeVoiceListener;
import com.singingkungfu.sing.listener.BackListener;
import com.singingkungfu.sing.task.ScreenFiveTask;
import com.singingkungfu.sing.task.ScreenFourTask;
import com.singingkungfu.sing.task.ScreenOneTask;
import com.singingkungfu.sing.task.ScreenSixTask;
import com.singingkungfu.sing.task.ScreenThreeTask;
import com.singingkungfu.sing.task.ScreenTwoTask;
import com.singingkungfu.sing.utils.ScreenShotUtils;
import com.singingkungfu.sing.widget.CustomVideoView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CustomVideoView.OnCorveHideListener, MediaPlayer.OnCompletionListener, View.OnClickListener, AnalyzeVoiceListener, BackListener, AgainTestListener {
    private CustomVideoView mVideoView;
    private RelativeLayout mControlView;
    private ImageView backIv;
    private int mIndex = 1;

    private Handler mHandler = new Handler();
    private ScreenOneTask mScreenOneTask;
    private ScreenTwoTask mScreenTwoTask;
    private ScreenThreeTask mScreenThreeTask;
    private ScreenFourTask mScreenFourTask;
    private ScreenFiveTask mScreenFiveTask;
    private ScreenSixTask mScreenSixTask;

    private List<String> mCaches;

    //step screen
    private LinearLayout actionLayout;
    private Button nextBtn;

    //step four
    private RelativeLayout mProgressLayout;
    private View mCurrentProgressLayout;

    //step six
    private TextView analyzeVoiceTv;

    //step seven
    private ImageView mAvatarIv;
    private TextView mNameTv;
    private TextView mScoreTv;
    private ProgressBar mHighProgressBar, mLowProgressBar, mIntonationProgressBar, mBreathProgressBar, mRhythmProgressBar;
    private TextView mHighScoreTv, mLowScoreTv, mIntonationScoreTv, mBreathScoreTv, mRhythmScoreTv;
    private boolean mResetScreen;   //是否重置场景

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = (CustomVideoView) findViewById(R.id.videoView);
        mControlView = (RelativeLayout) findViewById(R.id.control);
        backIv = (ImageView) findViewById(R.id.back_iv);
        backIv.setOnClickListener(this);
        mVideoView.setOnCorveHideListener(this);
        mVideoView.setOnCompletionListener(this);
        initView();
    }

    private void initView() {
        mCaches = getIntent().getStringArrayListExtra(DownLoadResourceActivity.DATA);
        playStepOneView();
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
        if (mIndex != 7) {
            mVideoView.start();
        }
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
    public void requestHide() {
        //隐藏第一帧图片(需要切图)
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //播放完成,判断当前进度
        mVideoView.pause();
        switch (mIndex) {
            case 1: //播放第1段，监听耳机插入状态
                mHandler.removeCallbacks(mScreenOneTask);
                if (mScreenOneTask.isCheckVoice()) {
                    playStepTwoView();
                }
                break;
            case 2: //播放第2段
                mHandler.removeCallbacks(mScreenTwoTask);
                if (mScreenTwoTask.isCheckVoice()) {
                    playStepThreeView();
                }
                break;
            case 3: //播放第3段
                mHandler.removeCallbacks(mScreenThreeTask);
                if (mScreenThreeTask.isCheckVoice()) {
                    playStepFourView();
                }
                break;
            case 4: //播放第4段
                mHandler.removeCallbacks(mScreenFourTask);
                if (mScreenFourTask.isCheckVoice()) {
                    playStepFiveView();
                }
                break;
            case 5: //播放第5段
                mHandler.removeCallbacks(mScreenFiveTask);
                if (mScreenFiveTask.isCheckVoice()) {
                    playStepSixView();
                }
                break;
            case 6: //播放第6段
                mHandler.removeCallbacks(mScreenSixTask);
                playStepSevenView();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                onBack();
                break;
            case R.id.reset_test_btn:   //重新测试
                playStepTwoView();
                break;
            case R.id.share_iv:         //分享
                String path = ScreenShotUtils.getActivityShot(this);
                ShareDialog dialog = new ShareDialog(this);
                dialog.setPicPath(path);
                dialog.show();
                break;
            case R.id.finish_btn:      //完成测试
                Toast.makeText(this, "功能待加入", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reset_btn:       //重新开始
                reset();
                break;
            case R.id.next_btn:          //下一关
                switch (mIndex) {
                    case 1:
                        playStepTwoView();
                        break;
                    case 2:
                        playStepThreeView();
                        break;
                    case 3:
                        playStepFourView();
                        break;
                    case 4:
                        playStepFiveView();
                        break;
                    case 5:
                        playStepSixView();
                        break;
                }
                break;
        }
    }

    private void addEndView() {
        removeCurrentStep();
        View stepView = LayoutInflater.from(this).inflate(R.layout.item_end_view, mControlView, false);
        actionLayout = (LinearLayout) stepView.findViewById(R.id.action_layout);
        stepView.findViewById(R.id.reset_btn).setOnClickListener(this);
        nextBtn = (Button) stepView.findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(this);
        mControlView.addView(stepView);
    }

    private void addStepFourView() {
        removeCurrentStep();
        View stepView = LayoutInflater.from(this).inflate(R.layout.item_step_four, mControlView, false);
        mProgressLayout = (RelativeLayout) stepView.findViewById(R.id.progress_layout);
        mCurrentProgressLayout = stepView.findViewById(R.id.current_progress);
        actionLayout = (LinearLayout) stepView.findViewById(R.id.action_layout);
        nextBtn = (Button) stepView.findViewById(R.id.next_btn);
        stepView.findViewById(R.id.reset_btn).setOnClickListener(this);
        nextBtn.setOnClickListener(this);
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


    //播放片段1
    private void playStepOneView() {
        mIndex = 1;
        setPlaySource(mCaches.get(1));
        mVideoView.start();
        removeCurrentStep();
        addEndView();
        actionLayout.setVisibility(View.GONE);
        nextBtn.setTextColor(ContextCompat.getColor(this, R.color.color_8159f3));
        if (mScreenOneTask != null) {
            mHandler.removeCallbacks(mScreenOneTask);
            mScreenOneTask = null;
        }
        mScreenOneTask = new ScreenOneTask(this, actionLayout, mHandler, this, this);
        mHandler.post(mScreenOneTask);
        mVideoView.start();
    }

    //播放片段2
    private void playStepTwoView() {
        mIndex = 2;
        setPlaySource(mCaches.get(2));
        mVideoView.start();
        removeCurrentStep();
        addEndView();
        actionLayout.setVisibility(View.GONE);
        nextBtn.setTextColor(ContextCompat.getColor(this, R.color.color_ff9437));

        if (mScreenTwoTask != null) {
            mHandler.removeCallbacks(mScreenTwoTask);
            mScreenTwoTask = null;
        }
        mScreenTwoTask = new ScreenTwoTask(this, actionLayout, mHandler, this, this);
        mHandler.post(mScreenTwoTask);
        mVideoView.start();
    }

    //播放片段3
    private void playStepThreeView() {
        mIndex = 3;
        setPlaySource(mCaches.get(3));
        mVideoView.start();
        removeCurrentStep();
        addEndView();
        actionLayout.setVisibility(View.GONE);
        nextBtn.setTextColor(ContextCompat.getColor(this, R.color.color_f67353));

        if (mScreenThreeTask != null) {
            mHandler.removeCallbacks(mScreenThreeTask);
            mScreenThreeTask = null;
        }

        mScreenThreeTask = new ScreenThreeTask(this, actionLayout, mHandler, this, this);
        mHandler.post(mScreenThreeTask);
        mVideoView.start();
    }

    //播放片段4
    private void playStepFourView() {
        mIndex = 4;
        setPlaySource(mCaches.get(4));
        mVideoView.start();
        addStepFourView();
        actionLayout.setVisibility(View.GONE);

        nextBtn.setTextColor(ContextCompat.getColor(this, R.color.color_4fc8ec));

        if (mScreenFourTask != null) {
            mHandler.removeCallbacks(mScreenFourTask);
            mScreenFourTask = null;
        }

        mScreenFourTask = new ScreenFourTask(this, actionLayout, mProgressLayout, mCurrentProgressLayout, this, this, mHandler);
        mHandler.post(mScreenFourTask);
        mVideoView.start();
    }

    //播放片段5
    private void playStepFiveView() {
        mIndex = 5;
        setPlaySource(mCaches.get(5));
        mVideoView.start();
        removeCurrentStep();
        addEndView();
        actionLayout.setVisibility(View.GONE);

        nextBtn.setTextColor(ContextCompat.getColor(this, R.color.color_1ebe80));
        nextBtn.setText(R.string.create_result);


        if (mScreenFiveTask != null) {
            mHandler.removeCallbacks(mScreenFiveTask);
            mScreenFiveTask = null;
        }

        mScreenFiveTask = new ScreenFiveTask(this, actionLayout, this, this, mHandler);
        mHandler.post(mScreenFiveTask);
        mVideoView.start();
    }

    //播放片段6
    private void playStepSixView() {
        backIv.setVisibility(View.GONE);
        mIndex = 6;
        setPlaySource(mCaches.get(6));
        mVideoView.start();
        addAnalyzeVoiceView();

        if (mScreenSixTask != null) {
            mHandler.removeCallbacks(mScreenSixTask);
            mScreenSixTask = null;
        }
        mScreenSixTask = new ScreenSixTask(mHandler, analyzeVoiceTv, this);
        mHandler.post(mScreenSixTask);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void analyzeComplete() {
        //分析完成
        mHandler.removeCallbacks(mScreenSixTask);
        playStepSevenView();
        mVideoView.pause();
    }


    private void onBack() {
        if (actionLayout != null) {
            actionLayout.setVisibility(View.GONE);
        }
        switch (mIndex) {
            case 1:
                if (mResetScreen) {
                    mResetScreen = false;
                    mVideoView.seekTo(0);
                    mVideoView.start();
                } else {
                    finish();
                }
                break;
            case 2:
                if (mResetScreen) {
                    mResetScreen = false;
                    mVideoView.seekTo(0);
                    mVideoView.start();
                } else {
                    playStepOneView();
                }
                break;
            case 3:
                if (mResetScreen) {
                    mResetScreen = false;
                    mVideoView.seekTo(0);
                    mVideoView.start();
                } else {
                    playStepTwoView();
                }
                break;
            case 4:
                if (mResetScreen) {
                    mResetScreen = false;
                    mVideoView.seekTo(0);
                    mVideoView.start();
                } else {
                    playStepThreeView();
                }
                break;
            case 5:
                if (mResetScreen) {
                    mResetScreen = false;
                    mVideoView.seekTo(0);
                    mVideoView.start();
                } else {
                    playStepFourView();
                }
                break;
        }
    }


    private void reset() {
        switch (mIndex) {
            case 1:
                playStepOneView();
                break;
            case 2:
                playStepTwoView();
                break;
            case 3:
                playStepThreeView();
                break;
            case 4:
                playStepFourView();
                break;
            case 5:
                playStepFiveView();
                break;
            case 6:
                playStepSixView();
                break;
        }
    }

    @Override
    public void onBackState(boolean resetScreen) {
        mResetScreen = resetScreen;
    }

    @Override
    public void onAgainTest() {
        reset();
    }

    @Override
    public void stopVideo() {
        mVideoView.pause();
    }
}
