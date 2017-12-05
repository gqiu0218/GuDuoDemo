package com.singingkungfu.sing.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.singingkungfu.sing.utils.FileUtils;
import com.singingkungfu.sing.utils.RecordUtils;

public class ScreenOneTask implements Runnable {
    private Context mContext;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;
    private LinearLayout mActionLayout;


    public ScreenOneTask(Context context, LinearLayout actionLayout, Handler handler) {
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
        if (progress == 20) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 1, 1));
        }

        if (progress == 24) {
            mRecordUtils.stopRecord();
        }

        if (progress == 27) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 1, 2));
        }


        if (progress == 31) {
            mRecordUtils.stopRecord();
        }


        if (progress == 35) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 1, 3));
        }


        if (progress == 39) {
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
