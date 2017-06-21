package com.lxh.userlibrary.utils;

import android.content.Context;
import android.util.Log;

import com.lxh.userlibrary.entity.WechatKeyValueInfo;
import com.lxh.userlibrary.tools.jm.Security;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by lxh on 2017/4/13.
 * QQ-632671653
 */

public class WechatPayUtil {

    private static IWXAPI api;

    private static String WX_APPID = "";
    private static String WX_KEY = "";
    public static void wechatPay(Context mContext,String wxappid,String wxkey,String sign,String nonce_str, String mch_id, String prepayId, String timeStamp) {
        WX_APPID = wxappid;
        WX_KEY = wxkey;
        api = WXAPIFactory.createWXAPI(mContext,wxappid,false);
        api.registerApp(wxappid);
        // 将该app注册到微信
        PayReq request = new PayReq();
        request.appId = wxappid;
        request.nonceStr = nonce_str;
        request.packageValue = "Sign=WXPay";
        request.partnerId = mch_id;
        request.prepayId = prepayId;
        request.timeStamp = timeStamp;
        List<WechatKeyValueInfo> packageParams = new LinkedList<>();
        packageParams.add(new WechatKeyValueInfo("appid", WX_APPID));
        packageParams.add(new WechatKeyValueInfo("noncestr", nonce_str));
        packageParams.add(new WechatKeyValueInfo("package", "Sign=WXPay"));
        packageParams.add(new WechatKeyValueInfo("partnerid", mch_id));
        packageParams.add(new WechatKeyValueInfo("prepayid", prepayId));
        packageParams.add(new WechatKeyValueInfo("timestamp", timeStamp));
        request.sign = genAppSign(packageParams);
//        request.sign = sign;
        Log.e("TAG","=========");
        api.sendReq(request);
    }


    /**
     * 参数签名
     *
     * @param params
     * @return
     */
    public static String genAppSign(List<WechatKeyValueInfo> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getKey());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(WX_KEY);
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toLowerCase();
        return appSign;
    }

    public static String getWX_APPID() {
        return WX_APPID;
    }
}
