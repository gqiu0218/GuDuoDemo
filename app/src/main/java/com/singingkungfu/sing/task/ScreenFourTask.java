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

import com.singingkungfu.sing.dialog.CheckVoiceDialog;
import com.singingkungfu.sing.listener.AgainTestListener;
import com.singingkungfu.sing.listener.BackListener;
import com.singingkungfu.sing.listener.ScreenFourForwardListener;
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
    private CheckVoiceDialog mDialog;
    private ScreenFourForwardListener forwardListener;
    private int mForwardTime;


    public ScreenFourTask(Context context, LinearLayout actionLayout, RelativeLayout progressLayout, View currentProgress, BackListener listener, AgainTestListener testListener, ScreenFourForwardListener listener3, Handler handler) {
        mContext = context;
        mActionLayout = actionLayout;
        mHandler = handler;
        mProgressLayout = progressLayout;
        mCurrentProgressLayout = currentProgress;
        mTotal = getScreenWidth(context) - dp2px(context, 88);
        mRecordUtils = new RecordUtils();

        mListener = listener;
        mTestListener = testListener;
        forwardListener = listener3;
        mListener.onBackState(false);
        mForwardTime = 0;

        FileUtils.deleteVoiceFile(context, 4, 1);
    }

    public void isStop(boolean stop) {
        if (!stop) {
            progress = 0;
        }
        mRecordUtils.stopRecord();
        mStopProgress = stop;
    }

    public void reset() {
        progress = 0;
        mCurrentProgress = 0;
        mStopProgress = false;
        mRecordUtils.stopRecord();
        mForwardTime = 0;
    }

    @Override
    public void run() {
        if (progress == 0) {
            mProgressLayout.setVisibility(View.GONE);
        }

        if (progress == 16000) {
            mListener.onBackState(true);
        }

        boolean interrupt = false;
        if (progress > 20000) {
            interrupt = mRecordUtils.updateMicStatus();
            if (interrupt) {
                mRecordUtils.stopRecord();
            }
        } else {
            mRecordUtils.updateMicStatus();
        }

        if (progress == 18000) {
            mProgressLayout.setVisibility(View.VISIBLE);
            mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 4, 1));
        }


        if (progress == 20000) {
            mRecordUtils.stopRecord();
            if (!FileUtils.isVoiceFileExist(mContext, 4, 1)) {
                //说明前两秒没有任何声音,显示静音dialog
                mTestListener.stopVideo();
                if (mDialog == null || !mDialog.isShowing()) {
                    mDialog = new CheckVoiceDialog(mContext, mTestListener);
                    mDialog.show();
                }
            } else {
                FileUtils.deleteVoiceFile(mContext, 4, 1);
                mRecordUtils.initRecord(FileUtils.getVoicePath(mContext, 4, 1));
            }
        }

        if (progress == 56000) {
            mProgressLayout.setVisibility(View.GONE);
            mRecordUtils.stopRecord();
        }


        if (progress == 57000 || interrupt) {
            boolean ok = isCheckVoice();
            if (ok) {
                if (progress < 57000) {
                    mForwardTime = progress;
                    forwardListener.onScreenFourForward();
                } else {
                    mActionLayout.setVisibility(View.VISIBLE);
                }


            } else {
                //说明其中某段没有录制,弹窗显示
                if (mDialog == null || !mDialog.isShowing()) {
                    mDialog = new CheckVoiceDialog(mContext, mTestListener);
                    mDialog.show();
                }
            }
        }

        Log.e("gqiu", "progress=" + progress);
        if (mForwardTime != 0 && progress - mForwardTime == 1000) {
            mActionLayout.setVisibility(View.VISIBLE);
        }

        ViewGroup.LayoutParams params = mCurrentProgressLayout.getLayoutParams();
        params.width = (int) ((float) mCurrentProgress / 38000 * mTotal);
        mCurrentProgressLayout.setLayoutParams(params);
        if (!mStopProgress) {
            progress += 50;

            if (progress >= 18000 && mForwardTime == 0) {
                mCurrentProgress += 50;
            }
        }
        mHandler.postDelayed(this, 50);
    }


    //静音检查
    public boolean isCheckVoice() {
        boolean isHasVoice = true;
        //如果某一段音频是静音的话，录制结束后直接删除，所以直接检查文件是否足够就行了
        if (!FileUtils.isVoiceFileExist(mContext, 4, 1)) {
            isHasVoice = false;
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
