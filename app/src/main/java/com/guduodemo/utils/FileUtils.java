package com.guduodemo.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

/**
 * 文件工具类
 * Created by gqiu on 2017/11/29.
 */

public class FileUtils {
    private static final String DOWNLOAD = "download";


    private static String getRootFilePath(Context context) {
        File file = context.getExternalCacheDir();
        if (file == null) {
            return "";
        }

        return file.getAbsolutePath();
    }


    public static String getDownLoadPath(Context context) {
        String rootPath = getRootFilePath(context);
        String path = rootPath + File.separator + DOWNLOAD;
        File downloadFile = new File(path);
        if (downloadFile.exists()) {
            downloadFile.mkdirs();
        }
        return path;
    }


    public static String getFileName(String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl)) {
            return "";
        }

        int index = downloadUrl.lastIndexOf("/");

        return downloadUrl.substring(index + 1, downloadUrl.length());
    }
}



