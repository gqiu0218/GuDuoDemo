package com.singingkungfu.sing.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.singingkungfu.sing.R;
import com.singingkungfu.sing.net.DownLoadFileUtils;
import com.singingkungfu.sing.net.NetUtils;
import com.singingkungfu.sing.widget.CustomVideoView;

import java.util.ArrayList;
import java.util.List;

public class DownLoadResourceActivity extends AppCompatActivity implements DownLoadFileUtils.DownLoadedListener, View.OnClickListener, MediaPlayer.OnCompletionListener {
    private CustomVideoView mVideoView;
    private TextView mProgresTv;

    public static final String DATA = "data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        findViewById(R.id.close_iv).setOnClickListener(this);
        mVideoView = (CustomVideoView) findViewById(R.id.videoView);
        mVideoView.setOnCompletionListener(this);
        mProgresTv = (TextView) findViewById(R.id.download_progress_tv);
        init();
    }

    private void init() {
        //下载视频文件
        DownLoadFileUtils utils = new DownLoadFileUtils(this);
        List<String> cacheFiles = utils.getCacheFiles();
        if (cacheFiles.size() == 0) {
            setPlaySource("android.resource://" + getPackageName() + "/" + R.raw.start_video);
            mVideoView.start();

            //需要下载
            if (NetUtils.isConnect(this)) {
                //网络错误
                mProgresTv.setText(R.string.network_error);
                return;
            }
            utils.download(mProgresTv, this);

        } else {
            jump(cacheFiles);
        }
    }

    private void setPlaySource(String path) {
        //设置播放加载路径
        mVideoView.setVideoURI(Uri.parse(path));
    }

    private void jump(List<String> list) {
        Intent intent = new Intent(this, PrepareActivity.class);
        intent.putStringArrayListExtra(DATA, (ArrayList<String>) list);
        startActivity(intent);
        finish();
    }

    @Override
    public void downLoaded(List<String> result) {
        jump(result);
    }

    @Override
    public void downLoadFailure() {
        Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        exit();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("确定要退出吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mVideoView.start();
        mp.setLooping(true);
    }
}
