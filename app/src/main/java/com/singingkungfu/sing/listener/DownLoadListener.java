package com.singingkungfu.sing.listener;

public interface DownLoadListener {

    void onProgress(int progress, int total);

    void onDownLoaded(String path);

    void onFailure();
}
