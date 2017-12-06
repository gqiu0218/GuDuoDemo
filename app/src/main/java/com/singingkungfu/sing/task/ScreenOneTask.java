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

public class ScreenOneTask implements Runnable {
    private Context mContext;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;
    private LinearLayout mActionLayout;
    private BackListener mListener;
    private AgainTestListener mTestListener;

    public ScreenOneTask(Context context, LinearLayout actionLayout, Handler handler, BackListener listener, AgainTestListener testListener) {
        mContext = context;
        mHandler = handler;
        mActionLayout = actionLayout;
        mRecordUtils = new RecordUtils();
        mListener = listener;
        mListener.onBackState(false);
        mTestListener = testListener;
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
        if (progress == 19) {
            mListener.onBackState(true);
        }
        mRecordUtils.updateMicStatus();
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
            boolean ok = isCheckVoice();
            if (ok) {
                mActionLayout.setVisibility(View.VISIBLE);
            } else {
                //说明其中某段没有录制,弹窗显示
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
        for (int i = 1; i < 4; i++) {
            if (!FileUtils.isVoiceFileExist(mContext, 1, i)) {
                isHasVoice = false;
                break;
            }
        }
        return isHasVoice;
    }
}
