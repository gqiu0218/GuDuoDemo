package com.singingkungfu.sing.share;

/**
 * 分享监听
 * Created by gqiu on 2017/10/6.
 */

public interface ShareListener {

    void onStartShare();

    void onCancelShare();

    void onFinishShare();
}
