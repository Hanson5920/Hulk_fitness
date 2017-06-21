package com.lxh.userlibrary.user;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.BaseFragment;
import com.lxh.userlibrary.base.FragmentFactory;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.entity.LoginUserInfo;
//import com.lxh.userlibrary.greendao.LoginUserInfoDao;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.manager.dialog.BindHasPhoneDialog;
import com.lxh.userlibrary.manager.dialog.BindNoPhoneDialog;
import com.lxh.userlibrary.manager.dialog.LoadingDialog;
import com.lxh.userlibrary.manager.update.UpdateUtil;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.tools.RegularExpressionTools;
import com.lxh.userlibrary.tools.ThirdPartyPlatFrom;
import com.lxh.userlibrary.utils.ToastUtil;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import okhttp3.Call;

import static com.lxh.userlibrary.message.Message.Type.USER_LOGIN_END;
import static com.lxh.userlibrary.message.Message.Type.USER_START_LOGIN;


/**
 * Created by wbb on 2016/10/8.
 */

public class UserLoginFragment extends BaseFragment implements PlatformActionListener {

    private String platformType = "4";
    private EditText mobilePhone;
    private EditText userPassword;
    private LoadingDialog loadingDialog;
    private Bundle bundle;

    public static UserLoginFragment newInstance() {
        return new UserLoginFragment();
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.user_login_layout;
    }

    @Override
    protected void bindView() {
        bundle = getArguments();
        setHeadTitle("登录");
        loadingDialog = new LoadingDialog(mActivity,"登录中...");

        mobilePhone = findView(R.id.mobile_phone);
        userPassword = findView(R.id.user_password);
        findViewIcon(R.id.icon_qq);
        findViewIcon(R.id.icon_weibon);
        findViewIcon(R.id.icon_weixin);
        findViewAttachOnclick(R.id.login_button);
        findViewAttachOnclick(R.id.login_registered_account);
        findViewAttachOnclick(R.id.login_forgot_password);
        findViewAttachOnclick(R.id.weixin_layout);
        findViewAttachOnclick(R.id.qq_layout);
        findViewAttachOnclick(R.id.xinlang_layout);

    }

    @Override
    protected void attachAllMessage() {
        attachMessage(USER_START_LOGIN);
        attachMessage(USER_LOGIN_END);
    }

    @Override
    public void onReceiveMessage(Message message) {
        super.onReceiveMessage(message);
        switch (message.type){
            case USER_START_LOGIN://登陆开始了
                loadingDialog.show();
                break;
            case USER_LOGIN_END://登录结束（成功/失败/取消）
                loadingDialog.dismiss();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.login_button) {
            String phone = mobilePhone.getText().toString().trim();
            String password = userPassword.getText().toString().trim();
            if (!RegularExpressionTools.isMobile(phone)) {
                ToastUtil.toastLong(mActivity, "请输入正确的手机号码");
                return;
            }
            if ("".equals(password)) {
                ToastUtil.toastLong(mActivity, "请输入密码");
                return;
            }
            UpdateUtil.UserLogin(mActivity, true, phone, password);

        } else if (i == R.id.login_registered_account) {
            Bundle bundle1 = new Bundle();
            bundle1.putString("ISNEW", "0");
            PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_USER_REGISTER, bundle1);

        } else if (i == R.id.login_forgot_password) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("ISNEW", "1");
            PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_USER_REGISTER, bundle2);

        } else if (i == R.id.icon_qq) {
            loadingDialog.show();
            platformType = Constants.QQ;
            ThirdPartyPlatFrom.getInstance().AdjustPlatform(platformType,this);

        } else if (i == R.id.icon_weixin) {
            loadingDialog.show();
            platformType = Constants.WECHAT;
            ThirdPartyPlatFrom.getInstance().AdjustPlatform(platformType,this);

        } else if (i == R.id.icon_weibon) {
            loadingDialog.show();
            platformType = Constants.WEIBO;
            ThirdPartyPlatFrom.getInstance().AdjustPlatform(platformType,this);
        }
    }


    private Platform sharePlatform;
    private HashMap<String,Object> shareHashMap;
    private static final int SUCCESS = 100;
    private static final int ERROR = 200;
    private static final int CANCEL = 300;

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.e("TAG", "=====[第三方登录授权 成功]======");
        sharePlatform = platform;
        shareHashMap = hashMap;
        handler.sendEmptyMessage(SUCCESS);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Log.e("TAG", "=====[第三方登录授权 失败]======");
        sharePlatform = platform;
        handler.sendEmptyMessage(ERROR);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Log.e("TAG", "=====[第三方登录授权 取消]======");
        sharePlatform = platform;
        handler.sendEmptyMessage(CANCEL);
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case SUCCESS:
                    PlatformDb platformDb = sharePlatform.getDb();
                    final String openId = platformDb.getUserId();
                    final String userName = platformDb.getUserName();
                    String logoUrl = platformDb.getUserIcon();
                    if (platformType.equals(Constants.QQ)) {
                        logoUrl = shareHashMap.get("figureurl_qq_2").toString();
                    }
                    UserCenter.getInstance().getMessagePump().
                            broadcastMessage(USER_START_LOGIN);
                    final String finalLogoUrl = logoUrl;
                    UserManage.UserOAuthLogin(openId,platformType,new UserInterface() {
                        @Override
                        public void onError(Call call, Exception e, String msg) {
                            ToastUtil.toastLong(mContext,"授权失败");
                            UserCenter.getInstance().getMessagePump().
                                    broadcastMessage(USER_LOGIN_END);
                            loadingDialog.dismiss();
//                XUtilLog.log_i("wbb", "=====[第三方登录授权 失败]======:" + msg);
                        }

                        @Override
                        public void onSucceed(int state, String msg, String data, JSONObject obj) {
                            Log.e("TAG","==三方登录======obj===="+obj.toString());
                            loadingDialog.dismiss();
                            switch (state){
                                case 200:
                                    try{
                                        LoginUserInfo user = null;
                                        List<LoginUserInfo> userInfos = JSON.parseArray(data, LoginUserInfo.class);
                                        if(userInfos!=null && userInfos.size()>0) user = userInfos.get(0);
                                        DefaultSharePrefManager.putString(Constants.KEY_USER_ID,user.getUid());
                                        DefaultSharePrefManager.putString(Constants.USER_LOGO_URL,user.getLogo());
                                        DefaultSharePrefManager.putString(Constants.USER_NAME,user.getName());
                                        DefaultSharePrefManager.putString(Constants.LOGIN_KEY,user.getLogin_key());
                                        UserCenter.getInstance().getMessagePump().
                                                broadcastMessage(USER_LOGIN_END);
                                        mActivity.onBackPressed();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    break;
                                case 211:
                                    try {
                                        JSONObject jsonObject = JSON.parseObject(data);
                                        String no_phone_verify = jsonObject.getString("no_phone_verify");
                                        BindNoPhoneDialog bindNoPhoneDialog = new BindNoPhoneDialog(mContext,openId,platformType,no_phone_verify);
                                        bindNoPhoneDialog.show();
                                    }catch (Exception e){

                                    }
                                    break;
                                case 212:
                                    try {
                                        JSONObject jsonObject = JSON.parseObject(data);
                                        String has_phone_verify = jsonObject.getString("has_phone_verify");
                                        BindHasPhoneDialog bindHasPhoneDialog = new BindHasPhoneDialog(mContext,openId,platformType,
                                                finalLogoUrl,userName,has_phone_verify);
                                        bindHasPhoneDialog.show();
                                    }catch (Exception e){

                                    }
                                    break;
                            }
                        }
                    });
                    break;
                case ERROR:
                    loadingDialog.dismiss();
                    ToastUtil.toastLong(mContext,"授权出现错误");
                    break;
                case CANCEL:
                    loadingDialog.dismiss();
                    ToastUtil.toastLong(mContext,"取消授权");
                    break;
            }

        }
    };


}
