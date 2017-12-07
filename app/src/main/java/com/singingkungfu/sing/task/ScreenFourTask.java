package com.singingkungfu.sing.task;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.singingkungfu.sing.dialog.CheckVoiceDialog;
import com.singingkungfu.sing.listener.AgainTestListener;
import com.singingkungfu.sing.listener.BackListener;
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
    private BackListener mListener;
    private AgainTestListener mTestListener;
    private int mTotal;

    public ScreenFourTask(Context context, LinearLayout actionLayout, RelativeLayout progressLayout, View currentProgress, BackListener listener, AgainTestListener testListener, Handler handler) {
        mContext = context;
        mActionLayout = actionLayout;
        mHandler = handler;
        mProgressLayout = progressLayout;
        mCurrentProgressLayout = currentProgress;
        mTotal = getScreenWidth(context) - dp2px(context, 88);
        mRecordUtils = new RecordUtils();

        mListener = listener;
        mTestListener = testListener;
        mListener.onBackState(false);

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
        if (progress == 16000) {
            mListener.onBackState(true);
        }
        mRecordUtils.updateMicStatus();
        if (progress == 18000) {
            mProgressLayout.setVisibility(View.VISIBLE);
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 4, 1));
        }

        if (progress == 56000) {
            mProgressLayout.setVisibility(View.GONE);
            mRecordUtils.stopRecord();
        }


        if (progress == 57000) {
            boolean ok = isCheckVoice();
            if (ok) {
                mActionLayout.setVisibility(View.VISIBLE);
            } else {
                //说明其中某段没有录制,弹窗显示
                mTestListener.stopVideo();
                CheckVoiceDialog dialog = new CheckVoiceDialog(mContext, mTestListener);
                dialog.show();
            }
        }

        ViewGroup.LayoutParams params = mCurrentProgressLayout.getLayoutParams();
        params.width = (int) ((float) mCurrentProgress / 38000 * mTotal);
        mCurrentProgressLayout.setLayoutParams(params);
        if (!mStopProgress) {
            progress += 50;

            if (progress >= 18000) {
                mCurrentProgress += 50;
            }
        }
        mHandler.postDelayed(this, 50);
    }


    //静音检查
    public boolean isCheckVoice() {
        boolean isHasVoice = true;
        //如果某一段音频是静音的话，录制结束后直接删除，所以直接检查文件是否足够就行了
        for (int i = 1; i < 4; i++) {
            if (!FileUtils.isVoiceFileExist(mContext, 1, i)) {
                isHasVoice = false;
                break;
            }
        }
        return isHasVoice;
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
