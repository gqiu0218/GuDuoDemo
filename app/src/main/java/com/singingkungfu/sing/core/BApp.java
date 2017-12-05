package com.singingkungfu.sing.core;

import android.app.Application;

import com.singingkungfu.sing.utils.Contants;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by gqiu On 2017/12/5.
 */

public class BApp extends Application {
    /**
     *  友盟分享、登录初始化配置
     */ {
        PlatformConfig.setWeixin(Contants.WECHAT_APP_ID, Contants.WECHAT_APP_SECRET);
        PlatformConfig.setQQZone(Contants.QQ_APP_ID, Contants.QQ_APP_SECRET);
        PlatformConfig.setSinaWeibo(Contants.WEIBO_APP_ID, Contants.WEIBO_APP_SECRET, "http://www.baidu.com");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Config.shareType = "u3d";
        UMShareAPI.get(this);
    }
}
