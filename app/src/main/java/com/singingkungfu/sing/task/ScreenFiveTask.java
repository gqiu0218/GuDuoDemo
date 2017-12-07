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

public class ScreenFiveTask implements Runnable {
    private Context mContext;
    private LinearLayout mActionLayout;
    private int progress;
    private boolean mStopProgress;
    private Handler mHandler;
    private RecordUtils mRecordUtils;
    private BackListener mListener;
    private AgainTestListener mTestListener;


    public ScreenFiveTask(Context context, LinearLayout actionLayout, BackListener listener, AgainTestListener testListener, Handler handler) {
        mActionLayout = actionLayout;
        mHandler = handler;
        mContext = context;
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
        if (progress == 27) {
            mListener.onBackState(true);
        }
        mRecordUtils.updateMicStatus();

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
            if (!FileUtils.isVoiceFileExist(mContext, 5, i)) {
                isHasVoice = false;
                break;
            }
        }
        return isHasVoice;
    }


}
