package com.singingkungfu.sing.share;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.BottomSheetDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * 分享dialog
 * Created by gqiu on 2017/10/6.
 */

public class ShareDialog implements View.OnClickListener {
    private Context mContext;
    private BottomSheetDialog dialog;
    private ShareUtils mShareUtils;
    private String mPhotoUrl, mContent, mTitle, mShareUrl;

    public ShareDialog(Activity activity, ShareListener listener) {
        mContext = activity;
        init(activity);
        mShareUtils = new ShareUtils(activity, listener);
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
        boolean result = false;
        if (view.getId() == R.id.qq_layout) {
            //qq分享
            result = mShareUtils.shareH5(SHARE_MEDIA.QQ, mContent, mPhotoUrl, mTitle, mShareUrl);
        } else if (view.getId() == R.id.qzone_layout) {
            //QQ空间分享
            result = mShareUtils.shareH5(SHARE_MEDIA.QZONE, mContent, mPhotoUrl, mTitle, mShareUrl);
        } else if (view.getId() == R.id.wechat_circle_layout) {
            //微信朋友圈分享
            result = mShareUtils.shareH5(SHARE_MEDIA.WEIXIN_CIRCLE, mContent, mPhotoUrl, mTitle, mShareUrl);
        } else if (view.getId() == R.id.wechat_layout) {
            //微信分享
            result = mShareUtils.shareH5(SHARE_MEDIA.WEIXIN, mContent, mPhotoUrl, mTitle, mShareUrl);
        } else if (view.getId() == R.id.weibo_layout) {
            //微博分享
            result = mShareUtils.shareH5(SHARE_MEDIA.SINA, mContent, mPhotoUrl, mTitle, mShareUrl);
        } else if (view.getId() == R.id.cancel_tv) {
            //取消分享
            dismiss();
        }

        if (result) {
            dismiss();
        }
    }
}
