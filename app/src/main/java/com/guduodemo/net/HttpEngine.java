package com.guduodemo.net;

import com.guduodemo.listener.DownLoadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpEngine {


    /**
     * 下载文件
     */
    public static void downFile(String path, String downUrl, DownLoadListener listener) {
        try {
            URL url = new URL(downUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("GET");

            int total = conn.getContentLength();
            int code = conn.getResponseCode();
            if (code >= 200 && code < 300) {
                InputStream is = conn.getInputStream();
                File file = new File(path);

                if (file.exists()) {
                    if (file.length() == total) {
                        //已经下载成功，直接返回
                        listener.onDownLoaded(path);
                        return;
                    } else {
                        //存在，但文件长度不一致，可能是上次没有下载完，删除重新下载
                        file.delete();
                        file.createNewFile();
                    }
                } else {
                    file.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int len;
                int currentTotal = 0;  //当前下载大小

                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    currentTotal += len;
                    listener.onProgress(total, currentTotal);
                }
                fos.flush();
                fos.close();
                is.close();
                listener.onDownLoaded(path);
            } else {
                listener.onFailure();
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFailure();
        }
    }

}
