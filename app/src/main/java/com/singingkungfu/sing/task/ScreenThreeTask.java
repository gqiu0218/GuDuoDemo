package com.singingkungfu.sing.task;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.singingkungfu.sing.dialog.CheckVoiceDialog;
import com.singingkungfu.sing.listener.AgainTestListener;
import com.singingkungfu.sing.listener.BackListener;
import com.singingkungfu.sing.utils.FileUtils;
import com.singingkungfu.sing.utils.RecordUtils;

public class ScreenThreeTask implements Runnable {
    private Context mContext;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;
    private LinearLayout mActionLayout;
    private BackListener mListener;
    private AgainTestListener mTestListener;

    public ScreenThreeTask(Context context, LinearLayout actionLayout, Handler handler, BackListener listener, AgainTestListener testListener) {
        mContext = context;
        mActionLayout = actionLayout;
        mHandler = handler;
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

    public void reset() {
        progress = 0;
        mRecordUtils.stopRecord();
    }


    @Override
    public void run() {
        if (progress == 22) {
            mListener.onBackState(true);
        }
        mRecordUtils.updateMicStatus();

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

        if (!mStopProgress) {
            progress++;
        }
        mHandler.postDelayed(this, 1000);
    }


    //静音检查
    public boolean isCheckVoice() {
        boolean isHasVoice = true;
        //如果某一段音频是静音的话，录制结束后直接删除，所以直接检查文件是否足够就行了
        for (int i = 1; i < 3; i++) {
            if (!FileUtils.isVoiceFileExist(mContext, 3, i)) {
                isHasVoice = false;
                break;
            }
        }
        return isHasVoice;
    }


}
