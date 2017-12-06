package com.singingkungfu.sing.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.singingkungfu.sing.R;
import com.singingkungfu.sing.listener.AgainTestListener;


public class CheckVoiceDialog extends Dialog implements View.OnClickListener {
    private AgainTestListener mListener;
    private Context mContext;

    public CheckVoiceDialog(@NonNull Context context, AgainTestListener listener) {
        super(context, R.style.dialog);
        mListener = listener;
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check_voice);
        findViewById(R.id.reset_btn).setOnClickListener(this);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        if (window != null) {
            WindowManager wm = ((Activity) mContext).getWindowManager();
            int width = wm.getDefaultDisplay().getWidth();

            WindowManager.LayoutParams params = window.getAttributes();
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = (int) (width * 0.75f);
            params.gravity = Gravity.CENTER;
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        mListener.onAgainTest();
    }


}
