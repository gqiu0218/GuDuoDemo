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
