package com.singingkungfu.sing.task;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.singingkungfu.sing.utils.FileUtils;
import com.singingkungfu.sing.utils.RecordUtils;

public class ScreenFourTask implements Runnable {
    private Context mContext;
    private LinearLayout mActionLayout;
    private int progress, mCurrentProgress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;
    private RelativeLayout mProgressLayout;
    private View mCurrentProgressLayout;
    private int mTotal;

    public ScreenFourTask(Context context, LinearLayout actionLayout, RelativeLayout progressLayout, View currentProgress, Handler handler) {
        mContext = context;
        mActionLayout = actionLayout;
        mHandler = handler;
        mProgressLayout = progressLayout;
        mCurrentProgressLayout = currentProgress;
        mTotal = getScreenWidth(context) - dp2px(context, 88);
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
        if (progress == 18000) {
            mProgressLayout.setVisibility(View.VISIBLE);
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 4, 1));
        }

        if (progress == 48000) {
            mProgressLayout.setVisibility(View.GONE);
            mRecordUtils.stopRecord();
        }


        if (progress == 50000) {
            mActionLayout.setVisibility(View.VISIBLE);
        }

        ViewGroup.LayoutParams params = mCurrentProgressLayout.getLayoutParams();
        params.width = (int) ((float) mCurrentProgress / 30000 * mTotal);
        mCurrentProgressLayout.setLayoutParams(params);
        if (!mStopProgress) {
            progress += 50;

            if (progress >= 18000) {
                mCurrentProgress += 50;
            }
        }
        mHandler.postDelayed(this, 50);
    }

    private int getScreenWidth(Context context) {
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    public int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
