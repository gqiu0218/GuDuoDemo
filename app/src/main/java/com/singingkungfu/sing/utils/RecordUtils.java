package com.singingkungfu.sing.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

/**
 * 录音权限检查
 * Created by gqiu on 2017/11/26.
 */

public class RecordUtils {
    // 音频获取源
    private static final int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    private static final int sampleRateInHz = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    private static final int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    private static int bufferSizeInBytes;

    private MediaRecorder mRecorder;
    private boolean mRecording;

    public static boolean isHasPermission(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                return false;
            }
        }


        bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        AudioRecord audioRecord = new AudioRecord(audioSource, sampleRateInHz,
                channelConfig, audioFormat, bufferSizeInBytes);
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        // 根据开始录音判断是否有录音权限
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage("检测到没有录音权限，请设置");
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getAppDetailSettingIntent(context);
                }
            });
            builder.show();
            return false;
        }
        audioRecord.stop();
        audioRecord.release();

        return true;
    }


    private static void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }


    public void initRecord(String filePath) {
        mRecorder = new MediaRecorder();// new出MediaRecorder对象
        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        // 设置MediaRecorder的音频源为麦克风
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        // 设置MediaRecorder录制的音频格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置MediaRecorder录制音频的编码为amr.
        mRecorder.setOutputFile(filePath);
        // 设置录制好的音频文件保存路径
        try {
            mRecorder.prepare();// 准备录制
            mRecorder.start();// 开始录制
            mRecording = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopRecord() {
        if (mRecording) {
            mRecorder.stop();// 停止刻录
            mRecorder.release(); // 刻录完成一定要释放资源
            mRecording = false;
        }
    }
}
