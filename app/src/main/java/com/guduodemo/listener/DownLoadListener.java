package com.guduodemo.listener;

public interface DownLoadListener {

    void onProgress(int progress, int total);

    void onDownLoaded(String path);

    void onFailure();
}
