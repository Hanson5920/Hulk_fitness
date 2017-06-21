package com.gz.hkjs.app.jpush;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.api.ApiConstants;
import com.gz.hkjs.app.ui.main.activity.LoginActivity;
import com.gz.hkjs.app.ui.main.activity.MainActivity;
import com.gz.hkjs.app.ui.main.fragment.TrainingMainFragment;
import com.gz.hkjs.app.util.AppUtil;
import com.jaydenxiao.common.baserx.RxManager;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.utils.HttpUtils;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

import static com.lxh.userlibrary.message.Message.Type.USER_EXIT_SATEA;

/**
 * Created by husong on 2017/6/7.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "husong";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                showRepeatLoginDialog(context);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

                //打开自定义的Activity
//                Intent i = new Intent(context, TestActivity.class);
//                i.putExtras(bundle);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void showRepeatLoginDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, 3);
        builder.setTitle(R.string.important_note);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.login_out, new
                DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        loginOut(context, true);
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton(R.string.re_login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginOut(context, false);
                dialog.dismiss();
                Intent i = new Intent(context, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(i);
            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String message = context.getResources().getString(R.string.repeatlogin_message, str);
        builder.setMessage(message);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        alertDialog.show();
    }

    private void loginOut(final Context context, final boolean exit){
        final String uid = UserCenter.getUid();
        Map<String, String> map =new HashMap<>();
        map.put("os", "1");
        map.put("version", AppUtil.getVersionName(context));
        map.put("uid", UserCenter.getUid());
        map.put("imei", ExampleUtil.getImei(context));
        HttpUtils.postStringAsync(ApiConstants.API_USER_LOGIN_OUT, map, context, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JPushManager.getInstance(context).setTag("");
                UserManage.UserLogout(uid, new UserInterface() {
                    @Override
                    public void onError(Call call, Exception e, String msg) {
//                        Log.e("husong", "------------>>>请求退出失败 ");
                    }

                    @Override
                    public void onSucceed(int state, String msg, String data, com.alibaba.fastjson.JSONObject obj) {
                        if (state == 200) {
                            DefaultSharePrefManager.putString(Constants.KEY_USER_ID, "");
                            DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, "");
                            DefaultSharePrefManager.putString(Constants.USER_NAME, "");
                            DefaultSharePrefManager.putString(Constants.LOGIN_KEY, "");

//                            DefaultSharePrefManager.remove(Constants.KEY_USER_ID);
//                            DefaultSharePrefManager.remove(Constants.USER_LOGO_URL);
//                            DefaultSharePrefManager.remove(Constants.USER_NAME);
//                            DefaultSharePrefManager.remove(Constants.LOGIN_KEY);
                            UserCenter.getInstance().getMessagePump().
                                    broadcastMessage(USER_EXIT_SATEA);

                            if(exit){
                                mHandler.sendEmptyMessageDelayed(1,500);
                            }
                        }

                    }
                });
            }
        });
    }

    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            if(msg.what==1){
                RxManager mRxManager = new RxManager();
                mRxManager.post(TrainingMainFragment.strLogout, "Logout");
                mRxManager.post(MainActivity.NO_LOGIN, "no_login");
            }
        }
    };

}
