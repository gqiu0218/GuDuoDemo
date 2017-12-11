package com.singingkungfu.sing.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.singingkungfu.sing.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 * Created by gqiu on 2017/11/29.
 */

public class FileUtils {
    private static final String DOWNLOAD = "download";
    private static final String VOICE = "voice";
    private static final String SHARE_PIC = "sharePic.jpg";


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

    public static void deleteVoiceFile(Context context, int screen, int index) {
        String rootPath = getVoicePath(context);
        String filePath = rootPath + File.separator + "voice" + screen + "_" + index + ".amr";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }


    public static String getFileName(String downloadUrl) {
        if (TextUtils.isEmpty(downloadUrl)) {
            return "";
        }

        int index = downloadUrl.lastIndexOf("/");

        return downloadUrl.substring(index + 1, downloadUrl.length());
    }

    public static String getSharePic(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share_pic);
        String path = getRootFilePath(context) + File.separator + SHARE_PIC;
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}



