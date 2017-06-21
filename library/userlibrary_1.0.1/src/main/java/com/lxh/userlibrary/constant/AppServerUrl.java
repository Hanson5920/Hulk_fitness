package com.lxh.userlibrary.constant;


import com.lxh.userlibrary.base.BaseAppServerUrl;

/**
 * Created by wbb on 2016/4/27.
 */
public abstract class AppServerUrl extends BaseAppServerUrl {

    /**
     * 统计接口 BASEURL
     **/
    public final static String STATISTICS_BASEURL = getAppStatisticaUrl();

    //-----------------------------用户中心---------------------------------
    //更改账号;
    public final static String USER_CHANGEUSER = "User.ChangeUser";
    //用户注册;
    public final static String USER_AUTHREG = "User.AuthReg";
    //用户登录
    public final static String USER_LOGIN = "User.Login";
    //三方登录
    public final static String USER_LOGIN_OAUTH = "User.Login_OAuth";
    //第三方登录关联已有手机
    public final static String USER_BIND_OAUTH_HAS_PHONE = "User.Bind_OAuth_has_phone";
    //第三方登录关联未有手机
    public final static String USER_BIND_OAUTH_NO_PHONE = "User.Bind_OAuth_no_phone";
    //第三方登录-用户选择保留哪个账号
    public final static String USER_SELECT_PHONE_OR_OAUTH = "User.Select_phone_or_OAuth";
    //推出登录
    public final static String USER_LOGOUT = "User.Logout";
    //短信验证码
    public final static String USER_SEND_MARK = "User.SendMark";
    //检查验证码
    public final static String USER_CHECKMARK = "User.CheckMark";
    //手机/邮箱找回密码,期间需要用户发送验证码
    public final static String USER_NEWPASSWORD = "User.NewPassword";
    //获取用户个人信息资料
    public final static String USERINFO_INFO = "Userinfo.Info";
    //修改用户个人信息资料
    public final static String USERINFO_EDIT_INFO = "Userinfo.Edit_info";
    //用户直接上传头像/修改头像，立即生效
    public final static String USERINFO_UPLOAD_LOGO = "Userinfo.Upload_logo";
    //扫码支付
    public final static String PAY_NATIVE_MONEY = "Pay.Native_money";
    //微信支付接口
    public final static String PAY_WEIXIN_MONEY = "Pay.Weixin_money";
    //支付宝支付接口
    public final static String PAY_ALIPAY_MONEY = "Pay.Alipay_money";
    //点击关注
    public final static String FOLLOW_SETUP = "Follow.SetUp";
    //取消关注
    public final static String FOLLOW_SETDOWN = "Follow.SetDown";
    //随机推荐关注
    public final static String FOLLOW_GETRECOMMEND = "Follow.GetRecommend";
    //获取我/他的关注列表
    public final static String FOLLOW_GETTOP = "Follow.GetTop";
    //获取我/他的粉丝列表
    public final static String FOLLOW_GETUNDER = "Follow.GetUnder";
    //获取我/他的粉丝列表
    public final static String MONEY_GET = "Money.Get";
    //获取我/他的粉丝列表
    public final static String MONEY_GET_COST_DETAIL = "Money.Get_cost_detail";
    //获取我/他的粉丝列表
    public final static String MONEY_SET_LESS = "Money.Set_less";
    //用户发表评论
    public final static String COMMIT_SETUP = "Commit.SetUp";
    //根据应用获取评论
    public final static String COMMIT_GETBYTHEME = "Commit.GetByTheme";
    //根据用户ID获取评论
    public final static String COMMIT_GETBYUID = "Commit.GetByUid";

}
