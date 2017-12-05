package com.guduodemo.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.guduodemo.utils.FileUtils;
import com.guduodemo.utils.RecordUtils;

public class ScreenThreeTask implements Runnable {
    private Context mContext;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;
    private LinearLayout mActionLayout;

    public ScreenThreeTask(Context context, LinearLayout actionLayout, Handler handler) {
        mContext = context;
        mActionLayout = actionLayout;
        mHandler = handler;
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
        if (progress == 24) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 3, 1));
        }

        if (progress == 30) {
            mRecordUtils.stopRecord();
        }

        if (progress == 36) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 3, 2));
        }


        if (progress == 42) {
            mRecordUtils.stopRecord();
        }

        if (progress == 44) {
            mActionLayout.setVisibility(View.VISIBLE);
        }

        if (!mStopProgress) {
            progress++;
        }
        mHandler.postDelayed(this, 1000);
    }
}
