package com.singingkungfu.sing.utils;

import android.content.Context;

import com.singingkungfu.sing.R;
import com.singingkungfu.sing.onekeyshare.OnekeyShare;

import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by gqiu On 2017/12/7.
 */

public class ShareUtils {
    public static void toShare(Context context, String platform, String title, String url, String content, String imagePath) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        if (platform.equals(QZone.NAME)){
            oks.setTitleUrl(url);
        }
        oks.setUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imagePath);//确保SDcard下面存在此张图片

        // url仅在微信（包括好友和朋友圈）中使用
        if(platform.equals(Wechat.NAME)||platform.equals(WechatMoments.NAME)){
            oks.setUrl(url);
        }
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        oks.setPlatform(platform);
        // 启动分享GUI
        oks.show(context);
    }
}
