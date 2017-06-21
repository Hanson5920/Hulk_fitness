package com.lxh.userlibrary;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.DisplayMetrics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.constant.Configs;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.load.ImageLoaderManager;
import com.lxh.userlibrary.load.LocationService;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.manager.dialog.LoadingDialog;
import com.lxh.userlibrary.message.MessagePump;
import com.lxh.userlibrary.mineInterface.AliPayInterface;
import com.lxh.userlibrary.utils.AliPayUtil;
import com.lxh.userlibrary.utils.ToastUtil;
import com.lxh.userlibrary.utils.WechatPayUtil;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.Call;

/**
 * Created by wbb on 2016/8/2.
 */
public class UserCenter {

    private static UserCenter sInstance;
    private static Context mContext;
    private MessagePump mMessagePump;
    public static LocationService locationService;
    public static Vibrator mVibrator;
    public static String DKAETYA;
    public static String URL_KEY;
    //对应请求的c_appid
    public static String APPID;
    //对应请求的api版本号
    public static String USER_API;

    public static void init(Context context, String dkaetya, String urlKey, String appid, String userApi) {
        DKAETYA = dkaetya;
        URL_KEY = urlKey;
        APPID = appid;
        USER_API = userApi;
        mContext = context.getApplicationContext();
        sInstance = new UserCenter();
        Configs.init();//网络接口连接初始化
        ImageLoaderManager.init(context);
        locationService = new LocationService(context);
        mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        ShareSDK.initSDK(context);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Constants.Pwide = width;
        Constants.Phigh = height;
    }

    public static Context getContext() {
        return mContext;
    }

    public static synchronized UserCenter getInstance() {
        return sInstance;
    }

    public MessagePump getMessagePump() {
        if (mMessagePump == null)
            mMessagePump = new MessagePump();
        return mMessagePump;
    }

    public SharedPreferences getSharedPreferences(String name, int modePrivate) {
        return mContext.getSharedPreferences(name, modePrivate);
    }

    public static void weixinPay(Context context,String uid, String rmb, final String wxappid, final String wxkey) {
        if (uid == null || uid.equals("")) {
            ToastUtil.toastShort(mContext, "检测到您未登录，请登录之后再试...");
            return;
        }
        final LoadingDialog loadingDialog = new LoadingDialog(context,"正在调起微信支付，请稍候...");
        loadingDialog.show();
        UserManage.PayWeixin(uid, rmb, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastShort(mContext,msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                loadingDialog.dismiss();
                switch (state) {
                    case 200:
                        try {
                            JSONObject jsonObject = JSON.parseObject(data);
                            String prepay_id = jsonObject.getString("prepay_id");
                            String partnerid = jsonObject.getString("partnerid");
                            String noncestr = jsonObject.getString("noncestr");
                            String sign = jsonObject.getString("sign");
                            String time = String.valueOf(System.currentTimeMillis() / 1000);
                            WechatPayUtil.wechatPay(mContext,wxappid,wxkey,sign,noncestr, partnerid, prepay_id, time);
                        } catch (Exception e) {

                        }
                        break;
                    default:
                        ToastUtil.toastShort(mContext, msg);
                        break;
                }
                ToastUtil.toastShort(mContext,msg);
            }
        });
    }

    public static void aliPay(final Activity activity, String uid, String rmb, final AliPayInterface Interface){
        if (uid == null || uid.equals("")) {
            ToastUtil.toastShort(mContext, "检测到您未登录，请登录之后再试...");
            return;
        }
        final LoadingDialog loadingDialog = new LoadingDialog(activity,"正在调起支付宝支付，请稍候...");
        loadingDialog.show();
        UserManage.PayAlipay(uid, rmb, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastShort(mContext,msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                loadingDialog.dismiss();
                switch (state){
                    case 200:
                        JSONObject jsonObject = JSON.parseObject(data);
                        String requestInfo = jsonObject.getString("request_info");
                        AliPayUtil.pay(activity,requestInfo,Interface);
                        break;
                }
                ToastUtil.toastShort(mContext,msg);
            }
        });
    }


}
