package com.singingkungfu.sing.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVideoView extends VideoView {
    private MediaPlayer.OnInfoListener onInfoListener;
    private MediaPlayer.OnPreparedListener onPreparedListener = null;
    private OnCorveHideListener onCorveHideListener;


    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    protected void init() {
        super.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            if (onCorveHideListener != null) {
                                onCorveHideListener.requestHide();
                            }
                        }
                        if (onInfoListener != null) {
                            onInfoListener.onInfo(mp, what, extra);
                        }
                        return false;
                    }
                });
            }
        });
    }


    @Override
    public void setOnInfoListener(MediaPlayer.OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
    }


    public void setOnCorveHideListener(OnCorveHideListener onCorveHideListener) {
        this.onCorveHideListener = onCorveHideListener;
    }


    @Override
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l) {
        this.onPreparedListener = l;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    @Override
    public boolean isPlaying() {
        return false;
    }


    public interface OnCorveHideListener {
        void requestHide();
    }

}