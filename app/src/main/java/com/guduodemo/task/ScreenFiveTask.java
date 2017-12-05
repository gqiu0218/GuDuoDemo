package com.guduodemo.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.guduodemo.utils.FileUtils;
import com.guduodemo.utils.RecordUtils;

public class ScreenFiveTask implements Runnable {
    private Context mContext;
    private LinearLayout mActionLayout;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;

    public ScreenFiveTask(Context context, LinearLayout actionLayout, Handler handler) {
        mActionLayout = actionLayout;
        mHandler = handler;
        mContext = context;
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
        if (progress == 29) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 5, 1));
        }

        if (progress == 36) {
            mRecordUtils.stopRecord();
        }
        if (progress == 40) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 5, 2));
        }

        if (progress == 47) {
            mRecordUtils.stopRecord();
        }

        if (progress == 49) {
            mActionLayout.setVisibility(View.VISIBLE);
        }

        if (!mStopProgress) {
            progress++;
        }
        mHandler.postDelayed(this, 1000);
    }
}
