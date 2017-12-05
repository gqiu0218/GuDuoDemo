package com.singingkungfu.sing.wxapi;

/**
 * 微信回调页
 * Created by gqiu On 2017/12/5.
 */

import android.os.Bundle;

import com.singingkungfu.sing.utils.Contants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.weixin.callback.WXCallbackActivity;

/**
 * Created by gqiu on 2017-12-05.
 * 微信分享回调
 */
public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        //注册API
        IWXAPI api = WXAPIFactory.createWXAPI(this, Contants.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //发送成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }
        finish();
    }
}