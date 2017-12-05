package com.singingkungfu.sing.share;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 分享工具类
 * Created by gqiu on 2017/10/6.
 */

public class ShareUtils implements UMShareListener {
    private Activity mActivity;
    private UMShareAPI uMShareAPI;
    private ShareListener mListener;


    public ShareUtils(Activity activity, ShareListener listener) {
        mActivity = activity;
        uMShareAPI = UMShareAPI.get(mActivity);
        mListener = listener;
    }

    public boolean shareH5(SHARE_MEDIA platform, String content, String photoPath, String title, String shareUrl) {
        if (TextUtils.equals(platform.name(), SHARE_MEDIA.WEIXIN.name()) || TextUtils.equals(platform.name(), SHARE_MEDIA.WEIXIN_CIRCLE.name())) {
            if (!uMShareAPI.isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {
                Toast.makeText(mActivity, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (TextUtils.equals(platform.name(), SHARE_MEDIA.QQ.name()) || TextUtils.equals(platform.name(), SHARE_MEDIA.QZONE.name())) {
            if (!uMShareAPI.isInstall(mActivity, SHARE_MEDIA.QQ)) {
                Toast.makeText(mActivity, "请安装QQ客户端", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(photoPath) || TextUtils.isEmpty(title) || TextUtils.isEmpty(shareUrl)) {
            Toast.makeText(mActivity, "无法分享", Toast.LENGTH_SHORT).show();
            return false;
        }

        UMImage image = new UMImage(mActivity, photoPath);//网络图片
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(content);//描述


        ShareAction shareAction = new ShareAction(mActivity);
        shareAction.setPlatform(platform);      //传入平台
        shareAction.withMedia(web);
        shareAction.setCallback(this); //回调监听器
        shareAction.share();

        return true;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        if (mListener != null) {
            mListener.onStartShare();
        }
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        if (mListener != null) {
            mListener.onFinishShare();
        }

        if (mActivity != null && !mActivity.isFinishing()) {
            Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        if (mActivity != null && !mActivity.isFinishing()) {
            Toast.makeText(mActivity, "分享异常", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        if (mListener != null) {
            mListener.onCancelShare();
        }

        if (mActivity != null && !mActivity.isFinishing()) {
            Toast.makeText(mActivity, "取消分享", Toast.LENGTH_SHORT).show();
        }
    }
}
