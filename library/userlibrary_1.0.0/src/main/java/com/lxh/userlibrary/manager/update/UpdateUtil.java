package com.lxh.userlibrary.manager.update;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.FragmentFactory;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.entity.LoginUserInfo;
import com.lxh.userlibrary.entity.NewVersion;
//import com.lxh.userlibrary.greendao.LoginUserInfoDao;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.manager.dialog.LoadingDialog;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.utils.DisplayUtil;
import com.lxh.userlibrary.utils.ToastUtil;


import java.util.List;

import okhttp3.Call;


/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/9/29 10:40
 * Des：应用升级更新/用户中心处理
 */
public class UpdateUtil {

    private static LoadingDialog loadingDialog;

    public static void updateAPK(final Context mContext, final ShowUpdateDialog showUpdateDialog, final boolean isShowToast) {
        if (isShowToast) {
            String showText = mContext.getResources().getString(R.string.dialog_checking_update_text);
            loadingDialog = new LoadingDialog(mContext, showText);
            loadingDialog.show();
        }
//        UserManage.VersionUpdate(new UserInterface() {
//            @Override
//            public void onError(Call call, Exception e, String msg) {
//                Log.e("UPDATE","=====onError====="+msg);
//                if (isShowToast){
//                    checkingUpdateDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onSucceed(int state, String msg, String data, JSONObject obj) {
//                Log.e("UPDATE","=====onSucceed====="+msg);
//                if (state==200){
//                    if (isShowToast){
//                        checkingUpdateDialog.dismiss();
//                    }
//                    NewVersion version = JSON.parseObject(data,NewVersion.class);
//                    if (version!=null){
//                        if ((version.getApp_version().compareTo(DeviceUtil.getVersionName(mContext)))>0){
//                            showUpdateDialog.showUpdateDialog(version);
//                        }else {
//                            if (isShowToast) {
//                                String isNew = mContext.getResources().getString(R.string.checkversion_none);
//                                DisplayUtil.showToast(mContext, isNew);
//                            }
//                        }
//                    }else {
//                        if (isShowToast) {
//                            String isNew = mContext.getResources().getString(R.string.checkversion_none);
//                            DisplayUtil.showToast(mContext, isNew);
//                        }
//                    }
//                }else {
//                    if (isShowToast) {
//                        String isNew = mContext.getResources().getString(R.string.checkversion_none);
//                        DisplayUtil.showToast(mContext, isNew);
//                    }
//                }
//            }
//        });
    }

    /**
     * 用户 手机登录
     *
     * @param mContext    上下文
     * @param isShowToast 是否显示 toast ; true = 显示  ， false=不显示
     * @param phoneUnm    手机号
     * @param password    密码
     */
    public static void UserLogin(final Activity mContext, final boolean isShowToast,
                                 final String phoneUnm, final String password) {
        if (isShowToast) {
            String showText = mContext.getResources().getString(R.string.in_login);
            loadingDialog = new LoadingDialog(mContext, showText);
            loadingDialog.show();
        }
        UserManage.UserLogin(phoneUnm, password, "3", "1", new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                if (isShowToast) {
                    DisplayUtil.showToast(mContext, msg);
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                if (state == 200) {
                    if (isShowToast) {
                        DisplayUtil.showToast(mContext, msg);
                    }
                    try {
                        LoginUserInfo user = null;
                        List<LoginUserInfo> userInfos = JSON.parseArray(data, LoginUserInfo.class);
                        if (userInfos != null && userInfos.size() > 0) user = userInfos.get(0);
                        DefaultSharePrefManager.putString(Constants.KEY_USER_ID, user.getUid());
                        DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, user.getLogo());
                        DefaultSharePrefManager.putString(Constants.USER_NAME, user.getName());
                        DefaultSharePrefManager.putString(Constants.LOGIN_KEY, user.getLogin_key());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    UserCenter.getInstance().getMessagePump().
                            broadcastMessage(Message.Type.USER_LOGIN_END);
                    loadingDialog.dismiss();
                    mContext.onBackPressed();
                } else {
                    if (isShowToast) {
                        DisplayUtil.showToast(mContext, msg);
                    }
                    loadingDialog.dismiss();
                }
            }
        });
    }

    /**
     * 验证 验证码
     *
     * @param mContext    上下文
     * @param isShowToast 是否显示 toast ; true = 显示  ， false=不显示
     * @param phoneUnm    手机号
     * @param mark        验证码
     * @param isNew       0=是新注册 ; 1=找回密码
     */
    public static void VerifyVerificationCode(final Activity mContext, final boolean isShowToast,
                                              final String phoneUnm, final String mark, final String openId, final String platformType,
                                              final String logoUrl, final String userName, final String has_phone_verify, final String isNew) {
        if (isShowToast) {
            String showText = mContext.getResources().getString(R.string.in_validation);
            loadingDialog = new LoadingDialog(mContext, showText);
            loadingDialog.show();
        }
        //用户注册 验证验证码
        UserManage.UserCheckMark(phoneUnm, mark, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                if (isShowToast) {
                    ToastUtil.toastShort(mContext, msg);
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                if (state == 200) {
                    if (isShowToast) {
                        ToastUtil.toastShort(mContext, msg);
                    }
//                    XUtilLog.log_i("wbb","验证验证码=====isNew==="+isNew+"  =====[返回数据]=====:"+data);
                    Bundle bundle = new Bundle();
                    bundle.putString("ISNEW", isNew);
                    bundle.putString("PHONE", phoneUnm);
                    bundle.putString("CODE", mark);
                    bundle.putString("openid",openId);
                    bundle.putString("open_type",platformType);
                    bundle.putString("logo_url",logoUrl);
                    bundle.putString("info_name",userName);
                    bundle.putString("has_phone_verify",has_phone_verify);
                    //验证成功进入设置密码界面
                    loadingDialog.dismiss();
                    PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_USER_SET_PASSWORD, bundle);
                    mContext.onBackPressed();
                } else {
                    if (isShowToast) {
                        DisplayUtil.showToast(mContext, msg);
                    }
                    loadingDialog.dismiss();
                }
            }
        });
    }

    /**
     * 设置密码
     *
     * @param mActivity   上下文
     * @param isShowToast 是否显示 toast ; true = 显示  ， false=不显示
     * @param phoneUnm    手机号
     * @param vCode       验证码
     * @param password    密码
     * @param isNew       是否新账号
     */
    public static void SetPassword(final Activity mActivity, final boolean isShowToast,
                                   final String phoneUnm, final String password,final String vCode,final String openid,
                                   final String open_type,final String logo_url,final String info_name,final String has_phone_verify,
                                   final String info_sex, final String info_birthday,final String info_desc, boolean isNew) {
        if (isShowToast) {
            String showText = mActivity.getResources().getString(R.string.in_submit);
            loadingDialog = new LoadingDialog(mActivity, showText);
            loadingDialog.show();
        }
        if (isNew) {
            //用户注册 新密码
            UserManage.UserPhoneRigster(phoneUnm, password, vCode, openid, open_type, logo_url,info_name , info_sex, info_birthday, info_desc, new UserInterface() {
                @Override
                public void onError(Call call, Exception e, String msg) {
                    Log.e("TAG","===注册====onError===msg="+msg);
                    if (isShowToast) {
                        DisplayUtil.showToast(mActivity, msg);
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onSucceed(int state, String msg, String data, JSONObject obj) {
                    Log.e("TAG","===注册====onSucceed===obj="+obj.toString());
                    switch (state) {
                        case 200:
                            if (isShowToast) {
                                DisplayUtil.showToast(mActivity, msg);
                            }
                            try {
                                LoginUserInfo user = null;
                                List<LoginUserInfo> userInfos = JSON.parseArray(data, LoginUserInfo.class);
                                if (userInfos != null && userInfos.size() > 0)
                                    user = userInfos.get(0);
                                DefaultSharePrefManager.putString(Constants.KEY_USER_ID, user.getUid());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            loadingDialog.dismiss();
                            mActivity.onBackPressed();
                            break;
                        default:
                            if (isShowToast) {
                                DisplayUtil.showToast(mActivity, msg);
                            }
                            loadingDialog.dismiss();
                            break;
                    }
                }
            });
        } else {
            //旧密码找回 设置密码
            UserManage.UserNewPassword(phoneUnm, password, vCode, "3", new UserInterface() {
                @Override
                public void onError(Call call, Exception e, String msg) {
                    if (isShowToast) {
                        DisplayUtil.showToast(mActivity, msg);
                        loadingDialog.dismiss();
                    }
                }

                @Override
                public void onSucceed(int state, String msg, String data, JSONObject obj) {
                    switch (state) {
                        case 200:
                            if (isShowToast) {
                                DisplayUtil.showToast(mActivity, "密码修改成功");
                            }
                            try {
                                LoginUserInfo user = null;
                                List<LoginUserInfo> userInfos = JSON.parseArray(data, LoginUserInfo.class);
                                if (userInfos != null && userInfos.size() > 0)
                                    user = userInfos.get(0);
                                DefaultSharePrefManager.putString(Constants.KEY_USER_ID, user.getUid());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            loadingDialog.dismiss();
                            mActivity.onBackPressed();
                            break;
                        default:
                            if (isShowToast) {
                                DisplayUtil.showToast(mActivity, msg);
                            }
                            loadingDialog.dismiss();
                            break;
                    }
                }
            });
        }
    }

    /**
     * 得到用户id
     *
     * @param msg 数据源
     * @return
     */
    private static String getUid(String msg) {
        String uid = "0";
        try {
            JSONArray ja = JSON.parseArray(msg);
            JSONObject jo = ja.getJSONObject(0);
            uid = jo.getString("uid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uid;
    }

    public interface ShowUpdateDialog {
        public void showUpdateDialog(final NewVersion updateInfo);
    }

}
