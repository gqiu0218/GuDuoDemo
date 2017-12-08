package com.singingkungfu.sing.dialog;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.singingkungfu.sing.R;
import com.singingkungfu.sing.utils.ShareUtils;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享dialog
 * Created by gqiu on 2017/10/6.
 */

public class ShareDialog implements View.OnClickListener {
    private Context mContext;
    private BottomSheetDialog dialog;
    private String mPhotoUrl, mContent, mTitle, mShareUrl;

    public ShareDialog(Activity activity) {
        mContext = activity;
        init(activity);
    }

    public void setPicPath(String path) {
        mPhotoUrl = path;
    }

    public void setShareData(String title, String content, String photoUrl, String shareUrl) {
        mTitle = title;
        mContent = content;
        mPhotoUrl = photoUrl;
        mShareUrl = shareUrl;
    }

    private void init(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }


        dialog = new BottomSheetDialog(activity);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        dialogView.findViewById(R.id.qq_layout).setOnClickListener(this);
        dialogView.findViewById(R.id.wechat_circle_layout).setOnClickListener(this);
        dialogView.findViewById(R.id.wechat_layout).setOnClickListener(this);
        dialogView.findViewById(R.id.qzone_layout).setOnClickListener(this);
        dialogView.findViewById(R.id.weibo_layout).setOnClickListener(this);
        dialogView.findViewById(R.id.link_layout).setOnClickListener(this);
        dialogView.findViewById(R.id.cancel_tv).setOnClickListener(this);
        dialog.setContentView(dialogView);

        int screenHeight = getScreenHeight(mContext);
        int statusBarHeight = getStatusBarHeight(mContext);
        int dialogHeight = screenHeight - statusBarHeight;
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }


    private int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    public void show() {
        if (dialog == null) {
            return;
        }
        dialog.show();
    }

    public void dismiss() {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    private int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.heightPixels;
    }


    @Override
    public void onClick(View view) {
        test();
        if (view.getId() == R.id.qq_layout) {
            //qq分享
            ShareUtils.toShare(mContext, QQ.NAME, mTitle, mShareUrl, mContent, mPhotoUrl);
        } else if (view.getId() == R.id.qzone_layout) {
            //QQ空间分享
            ShareUtils.toShare(mContext, QZone.NAME, mTitle, mShareUrl, mContent, mPhotoUrl);
        } else if (view.getId() == R.id.wechat_circle_layout) {
            //微信朋友圈分享
            ShareUtils.toShare(mContext, WechatMoments.NAME, mTitle, mShareUrl, mContent, mPhotoUrl);
        } else if (view.getId() == R.id.wechat_layout) {
            //微信分享
            ShareUtils.toShare(mContext, Wechat.NAME, mTitle, mShareUrl, mContent, mPhotoUrl);
        } else if (view.getId() == R.id.weibo_layout) {
            //微博分享
            ShareUtils.toShare(mContext, SinaWeibo.NAME, mTitle, mShareUrl, mContent, mPhotoUrl);
        } else if (view.getId() == R.id.link_layout) {
            ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(mContent);
            Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
        }

        dismiss();
    }


    private void test() {
        mTitle = "[唱功]声音测试，了解你的声音特质！";
        mContent = "[唱功]声音测试，了解你的声音特质！邀你一起加入[唱功]!让声音成为你的武器！";
        mShareUrl = "http://www.baidu.com";
    }
}
