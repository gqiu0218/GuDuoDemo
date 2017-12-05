package com.guduodemo.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.guduodemo.utils.FileUtils;
import com.guduodemo.utils.RecordUtils;

public class ScreenFourTask implements Runnable {
    private Context mContext;
    private LinearLayout mActionLayout;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;

    public ScreenFourTask(Context context, LinearLayout actionLayout, Handler handler) {
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
        if (progress == 18) {
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 4, 1));
        }

        if (progress == 60) {
            mRecordUtils.stopRecord();
        }


        if (progress == 62) {
            mActionLayout.setVisibility(View.VISIBLE);
        }

        if (!mStopProgress) {
            progress++;
        }
        mHandler.postDelayed(this, 1000);
    }
}
