package com.guduodemo.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.guduodemo.utils.FileUtils;
import com.guduodemo.utils.RecordUtils;
import com.guduodemo.widget.MainRippleView;

public class ScreenOneTask implements Runnable {
    private Context mContext;
    private MainRippleView mRippleView;
    private ImageView mRecordView;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;


    public ScreenOneTask(Context context, MainRippleView mRippleView, ImageView mRecordView, Handler handler) {
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
        if (progress == 20) {
            mRippleView.setVisibility(View.VISIBLE);
            mRecordView.setVisibility(View.VISIBLE);
            mRippleView.start();
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 1, 1));
        }

        if (progress == 24) {
            mRippleView.setVisibility(View.GONE);
            mRecordView.setVisibility(View.GONE);
            mRippleView.stop();
            mRecordUtils.stopRecord();
        }

        if (progress == 27) {
            mRippleView.setVisibility(View.VISIBLE);
            mRecordView.setVisibility(View.VISIBLE);
            mRippleView.start();
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 1, 2));
        }


        if (progress == 31) {
            mRippleView.setVisibility(View.GONE);
            mRecordView.setVisibility(View.GONE);
            mRippleView.stop();
            mRecordUtils.stopRecord();
        }


        if (progress == 35) {
            mRippleView.setVisibility(View.VISIBLE);
            mRecordView.setVisibility(View.VISIBLE);
            mRippleView.start();
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 1, 3));
        }


        if (progress == 39) {
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
