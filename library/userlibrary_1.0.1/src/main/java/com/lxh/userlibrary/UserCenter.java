package com.lxh.userlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.constant.Configs;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.dialog.LoadingDialog;
import com.lxh.userlibrary.entity.ShareInfo;
import com.lxh.userlibrary.load.ImageLoaderManager;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.message.MessagePump;
import com.lxh.userlibrary.mineInterface.AliPayInterface;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.utils.AliPayUtil;
import com.lxh.userlibrary.utils.ShareUtils;
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

    //对应请求加密时签名的key值
    public static String URL_KEY;
    //对应请求的c_appid
    public static String APPID;
    //对应请求的api
    public static String USER_API;

    /**
     * 用户中心初始化，所有参数询问后台
     *
     * @param context 上下文
     * @param urlKey  对应请求加密时签名的key值
     * @param appid   对应加密请求的c_appid
     * @param userApi 对应请求时的api接口版本号
     */
    public static void init(Context context, String urlKey, String appid, String userApi) {
        URL_KEY = urlKey;
        APPID = appid;
        USER_API = userApi;
        mContext = context.getApplicationContext();
        sInstance = new UserCenter();
        Configs.init();//网络接口连接初始化
        ImageLoaderManager.init(context);
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


    /**
     * 获取当前登录的用户id
     *
     * @return 返回值为:"" 时，则用户未登录
     */
    public static String getUid() {
        return DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
    }

    /**
     * 获取当前登录后服务器返回的凭证
     *
     * @return 返回值为:"" 时，则用户未登录
     */
    public static String getLoginKey() {
        return DefaultSharePrefManager.getString(Constants.LOGIN_KEY, "");
    }

    /**
     * 获取当前登录用户的昵称
     *
     * @return 返回值为:"" 时，则用户未登录
     */
    public static String getUserName() {
        return DefaultSharePrefManager.getString(Constants.USER_NAME, "");
    }

    /**
     * 获取当前登录用户的头像地址
     *
     * @return 返回值为:"" 时，则用户未登录
     */
    public static String getUserLogoUrl() {
        return DefaultSharePrefManager.getString(Constants.USER_LOGO_URL, "");
    }

    /**
     * 微信支付
     *
     * @param context
     * @param uid     用户id
     * @param rmb     人民币（单位：分）
     * @param wxappid 微信appid
     * @param wxkey   微信key
     */
    public static void weixinPay(Context context, String uid, String rmb, final String wxappid, final String wxkey) {
        if (uid == null || uid.equals("")) {
            ToastUtil.toastShort(mContext, "检测到您未登录，请登录之后再试...");
            return;
        }
        final LoadingDialog loadingDialog = new LoadingDialog(context, "正在调起微信支付，请稍候...");
        loadingDialog.show();
        UserManage.PayWeixin(uid, rmb, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastShort(mContext, msg);
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
                            WechatPayUtil.wechatPay(mContext, wxappid, wxkey, sign, noncestr, partnerid, prepay_id, time);
                        } catch (Exception e) {

                        }
                        break;
                    default:
                        ToastUtil.toastShort(mContext, msg);
                        break;
                }
                ToastUtil.toastShort(mContext, msg);
            }
        });
    }

    /**
     * 支付宝支付
     *
     * @param activity  调用页面的activity
     * @param uid       用户id
     * @param rmb       人民币（单位：分）
     * @param Interface 支付回调
     */
    public static void aliPay(final Activity activity, String uid, String rmb, final AliPayInterface Interface) {
        if (uid == null || uid.equals("")) {
            ToastUtil.toastShort(mContext, "检测到您未登录，请登录之后再试...");
            return;
        }
        final LoadingDialog loadingDialog = new LoadingDialog(activity, "正在调起支付宝支付，请稍候...");
        loadingDialog.show();
        UserManage.PayAlipay(uid, rmb, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastShort(mContext, msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                loadingDialog.dismiss();
                switch (state) {
                    case 200:
                        JSONObject jsonObject = JSON.parseObject(data);
                        String requestInfo = jsonObject.getString("request_info");
                        AliPayUtil.pay(activity, requestInfo, Interface);
                        break;
                }
                ToastUtil.toastShort(mContext, msg);
            }
        });
    }

    /**
     * 用户更改手机账号/手机绑定Email/Email绑定手机（手机、email）。
     * 手机->新手机：更换手机号
     * 手机->email：绑定邮箱账号，原手机账号不变
     * email->手机：绑定手机账号，原邮箱账号不变
     *
     * @param phone         账号：手机号码
     * @param password      密码:用户设置的密码
     * @param name_type     2为邮箱，3为手机  ---可选字段，如不需要此字段可传"";
     * @param pass_type     1是密码，2是验证码  ---可选字段，如不需要此字段可传"";
     * @param name_new      新账号：手机号码/邮箱
     * @param name_type_new 2为邮箱，3为手机   ---可选字段，如不需要此字段可传"";
     * @param mark_new      验证码:新账号收到的验证码
     * @param userInterface 回调接口
     */
    public static void UserChangeUser(String phone, String password, String name_type, String pass_type,
                                      String name_new, String name_type_new, String mark_new,
                                      UserInterface userInterface) {
        if (phone.isEmpty() || password.isEmpty() || name_new.isEmpty() || mark_new.isEmpty()) {
            return;
        }
        UserManage.UserChangeUser(phone, password, name_type, pass_type, name_new, name_type_new, mark_new, userInterface);
    }

    /**
     * 获取用户信息
     *
     * @param uid           用户ID
     * @param login_key     登录后服务器返回的凭证
     * @param userInterface
     */
    public static void getUserInfo(String uid, String login_key, UserInterface userInterface) {
        UserManage.getUserInfo(uid, login_key, userInterface);
    }

//    /**
//     * 修改用户信息
//     * @param uid 用户ID
//     * @param login_key 登录后服务器返回的凭证
//     * @param logo 头像 ---可选字段，如不需要此字段可传"";
//     * @param name 昵称，初始值：用户+（系统随机生成）数字 ---可选字段，如不需要此字段可传"";
//     * @param sex 性别 ---可选字段，如不需要此字段可传"";
//     * @param birthday 生日,格式：YYYY-MM-DD ---可选字段，如不需要此字段可传"";
//     * @param mail 通信邮箱（不能用于登录） ---可选字段，如不需要此字段可传"";
//     * @param qq qq号码 ---可选字段，如不需要此字段可传"";
//     * @param desc 个人简介,初始值：这家伙太懒神马都木有留下!!! ---可选字段，如不需要此字段可传"";
//     * @param userInterface
//     */
//    public static void editUserInfo(String uid,String login_key,String logo,String name,
//                                    String sex,String birthday, String mail,String qq,
//                                    String desc,UserInterface userInterface){
//        UserManage.editUserInfo(uid,login_key,logo,name,sex,birthday,mail,qq,desc,userInterface);
//    }

    /**
     * 修改用户信息
     *
     * @param uid           用户ID
     * @param login_key     登录后服务器返回的凭证
     * @param logo          头像 ---可选字段，如不需要此字段可传"";
     * @param name          昵称，初始值：用户+（系统随机生成）数字 ---可选字段，如不需要此字段可传"";
     * @param sex           性别 ---可选字段，如不需要此字段可传"";
     * @param birthday      生日,格式：YYYY-MM-DD ---可选字段，如不需要此字段可传"";
     * @param mail          通信邮箱（不能用于登录） ---可选字段，如不需要此字段可传"";
     * @param qq            qq号码 ---可选字段，如不需要此字段可传"";
     * @param desc          个人简介,初始值：这家伙太懒神马都木有留下!!! ---可选字段，如不需要此字段可传"";
     * @param more          扩展字段，可传值示例："height:170,weight:65"  ---可选字段，如不需要此字段可传"";
     * @param userInterface
     */
    public static void editUserInfo(String uid, String login_key, String logo, String name,
                                    String sex, String birthday, String mail, String qq,
                                    String desc, String more, UserInterface userInterface) {
        UserManage.editUserInfo(uid, login_key, logo, name, sex, birthday, mail, qq, desc, more, userInterface);
    }

    /**
     * 点击关注
     *
     * @param uid           用户ID(关注者)
     * @param fuid          被关注者ID
     * @param userInterface
     */
    public static void FollowSetUp(String uid, String fuid, UserInterface userInterface) {
        UserManage.FollowSetUp(uid, fuid, userInterface);
    }

    /**
     * 取消关注
     *
     * @param uid           用户ID(关注者)
     * @param fuid          被关注者ID
     * @param userInterface
     */
    public static void FollowSetDown(String uid, String fuid, UserInterface userInterface) {
        UserManage.FollowSetDown(uid, fuid, userInterface);
    }

    /**
     * 随机推荐关注
     *
     * @param uid           当前用户ID(过滤自己已关注)
     * @param limit         请求多少个 默认10 ---可选字段，如不需要此字段可传""
     * @param userInterface
     */
    public static void FollowGetRecommend(String uid, String limit, UserInterface userInterface) {
        UserManage.FollowGetRecommend(uid, limit, userInterface);
    }

    /**
     * 获取我/他的关注
     *
     * @param uid           当前用户ID(过滤自己已关注)
     * @param myid          当前用户ID，用于表示是否我已经关注了!如果是请求我的关注(uid就是当前用户ID)，则传""。
     * @param page          第几页 默认1 ---可选字段，如不需要此字段可传""
     * @param limit         请求多少个 默认10  ---可选字段，如不需要此字段可传""
     * @param userInterface
     */
    public static void FollowGetTop(String uid, String myid, String page, String limit, UserInterface userInterface) {
        UserManage.FollowGetTop(uid, myid, page, limit, userInterface);
    }

    /**
     * 获取我/他的粉丝
     *
     * @param uid           用户ID
     * @param myid          当前用户ID，用于表示是否我已经关注了! ---可选字段，如不需要此字段可传"";
     * @param page          第几页 默认1 ---可选字段，如不需要此字段可传""
     * @param limit         每页多少条 默认10  ---可选字段，如不需要此字段可传""
     * @param userInterface
     */
    public static void FollowGetUnder(String uid, String myid, String page, String limit, UserInterface userInterface) {
        UserManage.FollowGetUnder(uid, myid, page, limit, userInterface);
    }

    /**
     * 获取用户金币
     *
     * @param uid           用户ID
     * @param userInterface
     */
    public static void MoneyGet(String uid, UserInterface userInterface) {
        UserManage.MoneyGet(uid, userInterface);
    }

    /**
     * 获取用户金币消费记录
     *
     * @param uid           用户ID
     * @param page          第几页 默认1 ---可选字段，如不需要此字段可传""
     * @param limit         每页多少条 默认10 ---可选字段，如不需要此字段可传""
     * @param userInterface
     */
    public static void MoneyGetCostDetail(String uid, String page, String limit, UserInterface userInterface) {
        UserManage.MoneyGetCostDetail(uid, page, limit, userInterface);
    }

    /**
     * 减少用户金币
     *
     * @param uid           用户ID
     * @param num           需要减少的金币数
     * @param content       文字说明
     * @param userInterface
     */
    public static void MoneySetLess(String uid, String num, String content, UserInterface userInterface) {
        UserManage.MoneySetLess(uid, num, content, userInterface);
    }

    /**
     * 用户发表评论
     *
     * @param uid           用户ID
     * @param pid           父级评论ID,一级评论则不传 ---可选字段，如不需要此字段可传""
     * @param theme_type    评论的主题类型 ---可选字段，如不需要此字段可传""
     * @param theme_id      评论的主题ID
     * @param content       评论内容
     * @param login_key     登录后服务器返回的凭证
     * @param userInterface
     */
    public static void CommitSetUp(String uid, String pid, String theme_type, String theme_id, String content, String login_key, UserInterface userInterface) {
        UserManage.CommitSetUp(uid, pid, theme_type, theme_id, content, login_key, userInterface);
    }

    /**
     * 根据应用获取评论
     *
     * @param theme_type    评论的主题类型 ---可选字段，如不需要此字段可传""
     * @param theme_id      评论的主题ID
     * @param page          第几页 默认1 ---可选字段，如不需要此字段可传""
     * @param limit         每页多少条 默认10 ---可选字段，如不需要此字段可传""
     * @param userInterface
     */
    public static void CommitGetByTheme(String theme_type, String theme_id, String page, String limit, UserInterface userInterface) {
        UserManage.CommitGetByTheme(theme_type, theme_id, page, limit, userInterface);
    }

    /**
     * 根据用户ID获取评论
     *
     * @param uid           获取评论的用户ID
     * @param page          第几页 默认1 ---可选字段，如不需要此字段可传""
     * @param limit         每页多少条 默认10 ---可选字段，如不需要此字段可传""
     * @param userInterface
     */
    public static void CommitGetByUid(String uid, String page, String limit, UserInterface userInterface) {
        UserManage.CommitGetByUid(uid, page, limit, userInterface);
    }

    /**
     * 第三方分享
     *
     * @param context      上下文
     * @param mData        分享数据
     * @param platformName 分享平台
     *                     Wechat.NAME 微信
     *                     WechatMoments.NAME 微信朋友圈
     *                     QQ.NAME qq
     *                     SinaWeibo.NAME 新浪微博
     * @param isShareShot  是否截屏
     */
    public static void share(Context context, ShareInfo mData, String platformName, boolean isShareShot) {
        ShareUtils.Share(context, mData, platformName, isShareShot);
    }

    /**
     * 跳转至指定fragment
     *
     * @param context            上下文
     * @param fragmentType//用户中心 FragmentFactory.FRAGMENT_TYPE_USER_LOGIN 登录界面
     *                           FragmentFactory.FRAGMENT_TYPE_USER_REGISTER 注册界面
     *                           FragmentFactory.FRAGMENT_TYPE_USER_SET_PASSWORD 设置密码界面
     *                           FragmentFactory.FRAGMENT_TYPE_USER_DATA_UPDATE 用户资料修改界面
     *                           FragmentFactory.FRAGMENT_TYPE_BIND_PHONE 绑定手机
     * @param bundle             ----跳转数据，如不需要，可传null
     */
    public static void jumpToFragment(Context context, int fragmentType, Bundle bundle) {
        PageSwitcher.switchToPage(context, fragmentType, bundle);
    }
}
