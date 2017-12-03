package com.guduodemo.task;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

public class ScreenSixTask implements Runnable {
    private Handler mHandler;
    private Context mContext;
    private TextView mTextView;

    public ScreenSixTask(Context context, TextView textView, Handler handler) {
        mHandler = handler;
        mContext = context;
        mTextView = textView;
    }

    @Override
    public void run() {
        mHandler.postDelayed(this, 1000);
    }
}
