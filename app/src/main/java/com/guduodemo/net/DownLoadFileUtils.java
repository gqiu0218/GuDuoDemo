package com.guduodemo.net;

import android.app.Activity;
import android.widget.TextView;

import com.guduodemo.listener.DownLoadListener;
import com.guduodemo.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownLoadFileUtils {
    private List<String> mDownLoadUrls = new ArrayList<>();
    private int mDownloadIndex = 0;
    private Activity mContext;
    private TextView mProgressTv;
    private DownLoadedListener mListener;
    private List<String> mCaches = new ArrayList<>();

    public DownLoadFileUtils(Activity context) {
        mDownLoadUrls = getDownLoadUrlList();
        mContext = context;
    }

    public List<String> getDownLoadUrlList() {
        List<String> result = new ArrayList<>();
        result.add("http://picturesever2.b0.upaiyun.com/test/Android/agame2.mp4");
        result.add("http://picturesever2.b0.upaiyun.com/test/Android/agame3.mp4");
        result.add("http://picturesever2.b0.upaiyun.com/test/Android/agame4.mp4");
        result.add("http://picturesever2.b0.upaiyun.com/test/Android/agame5.mp4");
        result.add("http://picturesever2.b0.upaiyun.com/test/Android/agame6.mp4");
        result.add("http://picturesever2.b0.upaiyun.com/test/Android/agame7.mp4");
        result.add("http://picturesever2.b0.upaiyun.com/test/Android/agame8.mp4");

        return result;
    }


    //判断文件是否都存在
    public List<String> getCacheFiles() {
        List<String> result = new ArrayList<>();
        for (String path : mDownLoadUrls) {
            String fileName = FileUtils.getFileName(path);
            String filePath = FileUtils.getDownLoadPath(mContext) + File.separator + fileName;
            if (!new File(filePath).exists()) {
                return new ArrayList<>();
            }
            result.add(filePath);
        }
        return result;
    }


    public void download(TextView textView, DownLoadedListener listener) {
        mListener = listener;
        mProgressTv = textView;
        new Thread() {
            @Override
            public void run() {
                String url = mDownLoadUrls.get(mDownloadIndex);
                String fileName = FileUtils.getFileName(url);
                String downloadPath = FileUtils.getDownLoadPath(mContext) + File.separator + fileName;
                HttpEngine.downFile(downloadPath, url, new DownLoadListener() {
                    @Override
                    public void onProgress(int progress, int total) {
                        final float currentFilePercent = (float) progress / total;
                        if (mContext == null || mContext.isFinishing()) {
                            return;
                        }

                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                float addPerCent = (float) 100 / mDownLoadUrls.size();
                                int realPercent = (int) (addPerCent * mDownloadIndex + addPerCent * currentFilePercent);
                                mProgressTv.setText(realPercent + "%正在下载...");
                            }
                        });

                    }

                    @Override
                    public void onDownLoaded(String path) {
                        if (mContext == null || mContext.isFinishing()) {
                            return;
                        }

                        mCaches.add(path);
                        if (mDownloadIndex < mDownLoadUrls.size() - 1) {
                            mDownloadIndex++;
                            download(mProgressTv, mListener);
                        } else {
                            //下载完成
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mListener.downLoaded(mCaches);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure() {
                        mListener.downLoadFailure();
                    }
                });
            }
        }.start();
    }


    public interface DownLoadedListener {
        void downLoaded(List<String> result);

        void downLoadFailure();
    }
}
