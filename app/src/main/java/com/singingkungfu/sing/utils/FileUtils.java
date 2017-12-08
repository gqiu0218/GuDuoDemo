package com.singingkungfu.sing.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件工具类
 * Created by gqiu on 2017/11/29.
 */

public class FileUtils {
    private static final String DOWNLOAD = "download";
    private static final String VOICE = "voice";
    private static final String SCREEN_SHOT = "screenShot.jpg";


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
        if (!downloadFile.exists()) {
            downloadFile.mkdirs();
        }
        return path;
    }


    private static String getVoicePath(Context context) {
        String rootPath = getRootFilePath(context);
        String path = rootPath + File.separator + VOICE;
        File downloadFile = new File(path);
        if (!downloadFile.exists()) {
            downloadFile.mkdirs();
        }
        return path;
    }


    public static String getVoicePath(Context context, int screen, int index) {
        String rootPath = getVoicePath(context);
        String filePath = rootPath + File.separator + "voice" + screen + "_" + index + ".amr";

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }


    public static boolean isVoiceFileExist(Context context, int screen, int index) {
        String rootPath = getVoicePath(context);
        String filePath = rootPath + File.separator + "voice" + screen + "_" + index + ".amr";
        File file = new File(filePath);
        return file.exists();
    }


    public static String getScreenShotPath(Context context) {
       return getRootFilePath(context) + File.separator + SCREEN_SHOT;
    }


    public static String getFileName(String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl)) {
            return "";
        }

        int index = downloadUrl.lastIndexOf("/");

        return downloadUrl.substring(index + 1, downloadUrl.length());
    }
}



