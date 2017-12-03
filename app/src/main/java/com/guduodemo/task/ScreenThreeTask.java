package com.guduodemo.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.guduodemo.utils.FileUtils;
import com.guduodemo.utils.RecordUtils;
import com.guduodemo.widget.MainRippleView;

public class ScreenThreeTask implements Runnable {
    private Context mContext;
    private MainRippleView mRippleView;
    private ImageView mRecordView;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;

    public ScreenThreeTask(Context context, MainRippleView mRippleView, ImageView mRecordView, Handler handler) {
        mContext = context;
        this.mRippleView = mRippleView;
        this.mRecordView = mRecordView;
        mHandler = handler;
        mRecordUtils = new RecordUtils();
    }

    public void isStop(boolean stop) {
        if (!stop) {
            progress = 0;
        }
        mRecordUtils.stopRecord();
        mStopProgress = stop;
        mRippleView.setVisibility(View.GONE);
        mRecordView.setVisibility(View.GONE);
        mRippleView.stop();
    }

    @Override
    public void run() {
        if (progress == 24) {
            mRippleView.setVisibility(View.VISIBLE);
            mRecordView.setVisibility(View.VISIBLE);
            mRippleView.start();
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 3, 1));
        }

        if (progress == 30) {
            mRippleView.setVisibility(View.GONE);
            mRecordView.setVisibility(View.GONE);
            mRippleView.stop();
            mRecordUtils.stopRecord();
        }

        if (progress == 36) {
            mRippleView.setVisibility(View.VISIBLE);
            mRecordView.setVisibility(View.VISIBLE);
            mRippleView.start();
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 3, 2));
        }


        if (progress == 42) {
            mRippleView.setVisibility(View.GONE);
            mRecordView.setVisibility(View.GONE);
            mRippleView.stop();
            mRecordUtils.stopRecord();
        }

        if (!mStopProgress) {
            progress++;
        }
        mHandler.postDelayed(this, 1000);
    }
}
