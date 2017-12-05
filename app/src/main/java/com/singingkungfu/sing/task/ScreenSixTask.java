package com.singingkungfu.sing.task;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.singingkungfu.sing.listener.AnalyzeVoiceListener;

public class ScreenSixTask implements Runnable {
    private Handler mHandler;
    private TextView mTxtView;
    private int mProgress;
    private static final int TOTAL = 1000 * 12;
    private AnalyzeVoiceListener mListener;

    public ScreenSixTask(Handler handler, TextView textView, AnalyzeVoiceListener listener) {
        mHandler = handler;
        mTxtView = textView;
        mListener = listener;
    }

    @Override
    public void run() {
        String content = (int) ((float) mProgress / TOTAL * 100) + "%";
        mTxtView.setText(content);

        mProgress += 100;

        if (mProgress == TOTAL) {
            mListener.analyzeComplete();
            return;
        }

        mHandler.postDelayed(this, 100);
    }
}
