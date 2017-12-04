package com.guduodemo.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.guduodemo.utils.FileUtils;
import com.guduodemo.utils.RecordUtils;

public class ScreenTwoTask implements Runnable {
    private Context mContext;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;
    private LinearLayout mActionLayout;


    public ScreenTwoTask(Context context, LinearLayout actionLayout, Handler handler) {
        mContext = context;
        mHandler = handler;
        mActionLayout = actionLayout;
        mRecordUtils = new RecordUtils();
    }

    public void isStop(boolean stop) {
        if (!stop) {
            progress = 0;
        }
        mRecordUtils.stopRecord();
        mStopProgress = stop;
    }

    @Override
    public void run() {
        if (progress == 21) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 2, 1));
        }

        if (progress == 24) {
            mRecordUtils.stopRecord();
        }

        if (progress == 28) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 2, 2));
        }


        if (progress == 31) {
            mRecordUtils.stopRecord();
        }


        if (progress == 36) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 2, 3));
        }


        if (progress == 40) {
            mRecordUtils.stopRecord();
        }

        if (progress == 42) {
            mActionLayout.setVisibility(View.VISIBLE);
        }

        if (!mStopProgress) {
            progress++;
        }
        mHandler.postDelayed(this, 1000);
    }
}
