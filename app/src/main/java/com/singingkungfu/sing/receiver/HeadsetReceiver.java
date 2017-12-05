package com.singingkungfu.sing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 耳机插拔状态监听
 * Created by gqiu on 2017/11/26.
 */

public class HeadsetReceiver extends BroadcastReceiver {
    private HeadSetConnectListener mListener;


    public HeadsetReceiver(HeadSetConnectListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_HEADSET_PLUG)) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    mListener.headsetState(false);
                } else if (intent.getIntExtra("state", 0) == 1) {
                    mListener.headsetState(true);
                }
            }
        }
    }


    public interface HeadSetConnectListener {
        void headsetState(boolean connectState);
    }

}
