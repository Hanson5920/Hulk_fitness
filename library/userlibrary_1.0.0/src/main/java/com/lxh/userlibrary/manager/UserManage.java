package com.lxh.userlibrary.manager;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.base.BaseAppServerUrl;
import com.lxh.userlibrary.constant.AppServerUrl;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.tools.jm.JmTools;
import com.lxh.userlibrary.utils.HttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by wbb on 2016/5/4.
 */
public class UserManage {

    public static Context getContext() {
        return UserCenter.getContext();
    }

    /**
     * 公共请求
     * @param hashMap
     * @param userInterface
     */
    public static void initHttp(Map<String, String> hashMap, final UserInterface userInterface) {

        Log.e("1","请求="+hashMap.toString());
        HttpUtils.postStringAsync(BaseAppServerUrl.userServerUrl, hashMap, getContext(), new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e) {
                Log.e("TAG","====HTTP===onError========"+e.toString());
                userInterface.onError(call, e,"网络连接超时,请稍候再试");
            }

            @Override
            public void onResponse(String response) {
                Log.e("TAG","====HTTP===onResponse========"+response);
                try {
                    JSONObject jsonObject = JSON.parseObject(response);
                    String data = JmTools.DecryptKey(jsonObject);
                    setMyInterface(data, userInterface);
                }catch (Exception e){
                    userInterface.onError(null, e,"数据解析失败");
                }

            }
        });
    }


    public static void initUploadFile(String fileName, File file,HashMap<String, String> params,final UserInterface userInterface) {
        Log.e("TAG","========uploadFile========"+params.toString());
        HttpUtils.uploadFile(BaseAppServerUrl.userServerUrl,fileName,file,params,new StringCallback() {
            @Override
            public void onError(okhttp3.Call call, Exception e) {
                Log.e("TAG","====HTTP===onError========"+e.toString());
                userInterface.onError(call, e,"网络连接超时,请稍候再试");
            }

            @Override
            public void onResponse(String response) {
                Log.e("TAG","====HTTP===onResponse========"+response);
                try {
                    JSONObject jsonObject = JSON.parseObject(response);
                    String data = JmTools.DecryptKey(jsonObject);
                    setMyInterface(data, userInterface);
                }catch (Exception e){
                    userInterface.onError(null, e,"数据解析失败");
                }

            }
        });
    }

    /**
     * 公共方法
     * 将请求结果放到 MyInterface 中
     *
     * @param response
     * @param userInterface
     */
    public static void setMyInterface(String response, UserInterface userInterface) {
        String msg="";
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            String ret1 = jsonObject.getString("ret");
            msg = jsonObject.getString("msg");
            String data = jsonObject.getString("data");
            int ret2 = Integer.parseInt(ret1);
            //返回ret状态码、data数据 、和jsonObject 。
            userInterface.onSucceed(ret2, msg, data, jsonObject);
        } catch (Exception e) {
            userInterface.onError(null, e,msg);
            e.printStackTrace();
        }
    }


    /**
     * 用户更改手机账号/手机绑定Email/Email绑定手机（手机、email）。
     * 手机->新手机：更换手机号
     * 手机->email：绑定邮箱账号，原手机账号不变
     * email->手机：绑定手机账号，原邮箱账号不变
     * @param phone
     * @param password
     * @param name_type
     * @param pass_type
     * @param name_new
     * @param name_type_new
     * @param mark_new
     * @param userInterface
     */
    public static void UserChangeUser(String phone, String password, String name_type,String pass_type,
                                        String name_new,String name_type_new,String mark_new,
                                        UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", phone);//账号：手机号码
        hashMap.put("pass", password);//密码:用户设置的密码
        hashMap.put("name_type", name_type);//2为邮箱，3为手机
        hashMap.put("pass_type",pass_type);//1是密码，2是验证码
        hashMap.put("name_new", name_new);//新账号：手机号码/邮箱
        hashMap.put("name_type_new", name_type_new);//新账号类型：2为邮箱，3为手机
        hashMap.put("mark_new", mark_new);//验证码:新账号收到的验证码
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_CHANGEUSER), userInterface);
    }

    /**
     * 手机号注册
     * @param phone
     * @param password
     * @param mark
     * @param openid
     * @param open_type
     * @param info_name
     * @param logo_url
     * @param info_sex
     * @param info_birthday
     * @param info_desc
     * @param userInterface
     */
    public static void UserPhoneRigster(String phone, String password, String mark,String openid,
                                        String open_type,String info_name,String logo_url,String info_sex,
                                        String info_birthday,String info_desc,
                                        UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!phone.equals("")){
            hashMap.put("name", phone);//账号：手机号码
        }
        if (!password.equals("")){
            hashMap.put("pass", password);//密码:用户设置的密码
        }
        if (!mark.equals("")){
            hashMap.put("mark", mark);//验证码:用户手机接收的验证码
        }
        if (!openid.equals("")){
            hashMap.put("openid",openid);//三方平台对应唯一ID(用户第三方登录失败后注册才传此参数)
        }
        if (!open_type.equals("")){
            hashMap.put("open_type", open_type);//4QQ，5微信，6微博(用户第三方登录失败后注册才传此参数)
        }
        if (!logo_url.equals("")){
            hashMap.put("logo_url", logo_url);//用户第三方头像地址：第三方登录失败后注册才传此参数
        }
        if (info_name.equals("")){
            hashMap.put("info_name", phone);//用户昵称
        }else {
            hashMap.put("info_name", info_name);//用户昵称
        }
        if (!info_sex.equals("")){
            hashMap.put("info_sex", info_sex);//用户性别：1男2女
        }
        if (!info_birthday.equals("")){
            hashMap.put("info_birthday", info_birthday);//用户生日：YYYY-MM-DD格式
        }
        if (!info_desc.equals("")){
            hashMap.put("info_desc", info_desc);//用户个性签名
        }
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_AUTHREG), userInterface);
    }

    /**
     * 用户账号登录
     * @param name 账号：手机号码/邮箱
     * @param pass 密码:用户设置的密码/验证码
     * @param name_type 1为用户名登录，2为邮箱登录，3为手机登录
     * @param pass_type 1是密码，2是验证码
     * @param userInterface
     */
    public static void UserLogin(String name, String pass, String name_type,
                                 String pass_type,UserInterface userInterface) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("name",name);//账号：手机号码/邮箱
        hashMap.put("pass",pass);//密码:用户设置的密码/验证码
        hashMap.put("name_type",name_type);//1为用户名登录，2为邮箱登录，3为手机登录
        hashMap.put("pass_type",pass_type);//1是密码，2是验证码
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_LOGIN), userInterface);
    }


    /**
     * 三方登录
     * @param openid
     * @param open_type
     * @param userInterface
     */
    public static void UserOAuthLogin(String openid, String open_type,UserInterface userInterface) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("openid",openid);//三方平台对应唯一ID
        hashMap.put("open_type",open_type);//4QQ，5微信，6微博
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_LOGIN_OAUTH), userInterface);
    }


    /**
     * 第三方登录：openid关联已有手机
     * @param openid
     * @param open_type
     * @param userInterface
     */
    public static void BindOAuthHasPhone(String openid, String open_type,String name, String pass,
                                         String name_type,String pass_type,String has_phone_verify,
                                         UserInterface userInterface) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("openid",openid);//三方平台对应唯一ID
        hashMap.put("open_type",open_type);//4QQ，5微信，6微博
        hashMap.put("name",name);//账号：手机号码/邮箱
        hashMap.put("pass",pass);//密码:用户设置的密码/验证码
        hashMap.put("name_type",name_type);//1为用户名登录，2为邮箱登录，3为手机登录
        hashMap.put("pass_type",pass_type);//1是密码，2是验证码
        hashMap.put("has_phone_verify",has_phone_verify);//	接口来源验证值:上一个接口返回的值
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_BIND_OAUTH_HAS_PHONE), userInterface);
    }

    /**
     * 第三方登录：openid关联未有手机
     * @param openid
     * @param open_type
     * @param userInterface
     */
    public static void BindOAuthNOPhone(String openid, String open_type,String name, String mark,
                                        String no_phone_verify,UserInterface userInterface) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("openid",openid);//三方平台对应唯一ID
        hashMap.put("open_type",open_type);//4QQ，5微信，6微博
        hashMap.put("name",name);//账号：手机号码/邮箱
        hashMap.put("mark",mark);//密码:用户设置的密码/验证码
        hashMap.put("no_phone_verify",no_phone_verify);//	接口来源验证值:上一个接口返回的值
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_BIND_OAUTH_NO_PHONE), userInterface);
    }

    /**
     * 第三方登录-用户选择保留哪个账号
     * @param select
     * @param token
     * @param phoneUid
     * @param oauthUid
     * @param openid
     * @param open_type
     * @param userInterface
     */
    public static void UserSelectPhoneOrOAuth(String select, String token, String phoneUid,String oauthUid,
                                        String openid,String open_type, UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("select", select);//用户选择结果：1是oauth/2是phone
        hashMap.put("token", token);//验证接口值（bind_phone_to_OAuth返回的token）
        hashMap.put("phoneUid", phoneUid);//手机用户账号ID（bind_phone_to_OAuth返回的uid）
        hashMap.put("oauthUid",oauthUid);//三方用户账号ID（bind_phone_to_OAuth返回的uid）
        hashMap.put("openid", openid);//该三方平台对应唯一ID
        hashMap.put("open_type", open_type);//4QQ，5微信，6微博
        Log.e("TAG","==选择保留用户信息==hashMap="+hashMap.toString());
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_SELECT_PHONE_OR_OAUTH), userInterface);
    }

    /**
     *
     * @param uid
     * @param userInterface
     */
    public static void UserLogout(String uid, UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户id
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_LOGOUT), userInterface);
    }
    /**
     * 发送验证码
     * @param name  账号：手机号码/邮箱
     */
    public static void UserSendMark(String name, UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);//账号：手机号码/邮箱
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_SEND_MARK), userInterface);
    }


    /**
     * 检查验证码是否正确
     * @param name  账号：手机号码/邮箱
     */
    public static void UserCheckMark(String name,String mark, UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);//账号：手机号码/邮箱
        hashMap.put("mark", mark);//短信验证码
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_CHECKMARK), userInterface);
    }



    /**
     * 手机/邮箱找回密码,期间需要用户发送验证码
     * @param name  账号：手机号码/邮箱
     */
    public static void UserNewPassword(String name,String pass,String mark,String name_type, UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", name);//账号：手机号码/邮箱
        hashMap.put("pass", pass);//密码:用户设置的新密码
        hashMap.put("mark", mark);//验证码:用户接收的验证码
        hashMap.put("name_type", name_type);//2为邮箱，3为手机
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USER_NEWPASSWORD), userInterface);
    }


    /**
     * 获取用户信息
     * @param uid
     * @param login_key
     * @param userInterface
     */
    public static void getUserInfo(String uid,String login_key, UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        hashMap.put("login_key", login_key);//登录后服务器返回的凭证
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USERINFO_INFO), userInterface);
    }


    /**
     * 修改用户信息
     * @param uid
     * @param login_key
     * @param logo
     * @param name
     * @param sex
     * @param birthday
     * @param mail
     * @param qq
     * @param desc
     * @param userInterface
     */
    public static void editUserInfo(String uid,String login_key,String logo,String name,
                                    String sex,String birthday, String mail,String qq,
                                    String desc,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        hashMap.put("login_key", login_key);//登录后服务器返回的凭证
        if (!logo.equals("")){
            hashMap.put("logo", logo);//头像
        }
        if (!name.equals("")){
            hashMap.put("name", name);//昵称，初始值：用户+（系统随机生成）数字
        }
        if (!sex.equals("")){
            hashMap.put("sex", sex);//性别
        }
        if (!birthday.equals("")){
            hashMap.put("birthday", birthday);//生日,格式：YYYY-MM-DD
        }
        if (!mail.equals("")){
            hashMap.put("mail", mail);//通信邮箱（不能用于登录）
        }
        if (!qq.equals("")){
            hashMap.put("qq", qq);//qq号码
        }
        if (!desc.equals("")){
            hashMap.put("desc", desc);//个人简介,初始值：这家伙太懒神马都木有留下!!!
        }
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.USERINFO_EDIT_INFO), userInterface);
    }

    /**
     * 上传头像
     * @param uid
     * @param logo
     * @param userInterface
     */
    public static void UserUploadLogo(String fileName, File file,String uid,String url,String logo,String login_key,UserInterface userInterface) {

        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        if (url.equals("")){
            hashMap.put("logo", logo);//头像(文件流)
        }else {
            try {
                url = URLEncoder.encode(url,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            hashMap.put("url", url);//头像地址（完整URL路径）,与logo二选一传值
        }
        hashMap.put("login_key", login_key);//头像地址（完整URL路径）,与logo二选一传值
        initUploadFile(fileName,file,JmTools.JM(getContext(),hashMap,AppServerUrl.USERINFO_UPLOAD_LOGO), userInterface);
    }

//    /**
//     * 扫码支付
//     * @param paytype
//     * @param uid
//     * @param rmb
//     * @param userInterface
//     */
//    public static void PayNativeMoney(String paytype,String uid,String rmb,UserInterface userInterface) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("paytype", paytype);//引擎类型:1是weixin/2是alipay/3是网银
//        hashMap.put("uid", uid);//用户ID
//        hashMap.put("rmb", rmb);//人民币(分)
//        Log.e("TAG","===hashMap==="+hashMap.toString());
//        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.PAY_NATIVE_MONEY), userInterface);
//    }

    /**
     * 微信支付
     * @param uid
     * @param rmb
     * @param userInterface
     */
    public static void PayWeixin(String uid,String rmb,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        hashMap.put("rmb", rmb);//人民币(分)
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.PAY_WEIXIN_MONEY), userInterface);
    }

    /**
     * 支付宝支付
     * @param uid
     * @param rmb
     * @param userInterface
     */
    public static void PayAlipay(String uid,String rmb,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        hashMap.put("rmb", rmb);//人民币(分)
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.PAY_ALIPAY_MONEY), userInterface);
    }




    /**
     * 点击关注
     * @param uid
     * @param fuid
     * @param userInterface
     */
    public static void FollowSetUp(String uid,String fuid,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID(关注者)
        hashMap.put("fuid", fuid);//被关注者ID
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.FOLLOW_SETUP), userInterface);
    }

    /**
     * 取消关注
     * @param uid
     * @param fuid
     * @param userInterface
     */
    public static void FollowSetDown(String uid,String fuid,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID(关注者)
        hashMap.put("fuid", fuid);//被关注者ID
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.FOLLOW_SETDOWN), userInterface);
    }

    /**
     * 随机推荐关注
     * @param uid 当前用户ID(过滤自己已关注)
     * @param limit 请求多少个
     * @param userInterface
     */
    public static void FollowGetRecommend(String uid,String limit,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//当前用户ID(过滤自己已关注)
        hashMap.put("limit", limit);//请求多少个
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.FOLLOW_GETRECOMMEND), userInterface);
    }

    /**
     * 获取我/他的关注
     * @param uid
     * @param limit
     * @param userInterface
     */
    public static void FollowGetTop(String uid,String myid,String page,String limit,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//当前用户ID(过滤自己已关注)
        if (!myid.equals("")){
            hashMap.put("myid", myid);//当前用户ID，用于表示是否我已经关注了!如果是请求我的关注(uid就是当前用户ID)，则不传此参数。
        }
        hashMap.put("page", page);//第几页
        hashMap.put("limit", limit);//请求多少个
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.FOLLOW_GETTOP), userInterface);
    }

    /**
     * 获取我/他的粉丝
     * @param uid
     * @param myid
     * @param page
     * @param limit
     * @param userInterface
     */
    public static void FollowGetUnder(String uid,String myid,String page,String limit,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        if (!myid.equals("")){
            hashMap.put("myid", myid);//当前用户ID，用于表示是否我已经关注了!
        }
        hashMap.put("page", page);//第几页
        hashMap.put("limit", limit);//每页多少条
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.FOLLOW_GETUNDER), userInterface);
    }

    /**
     * 获取用户金币
     * @param uid
     * @param userInterface
     */
    public static void MoneyGet(String uid,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.MONEY_GET), userInterface);
    }

    /**
     * 获取用户金币消费记录
     * @param uid
     * @param page
     * @param limit
     * @param userInterface
     */
    public static void MoneyGetCostDetail(String uid,String page,String limit,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        hashMap.put("page", page);//第几页
        hashMap.put("limit", limit);//每页多少条
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.MONEY_GET_COST_DETAIL), userInterface);
    }


    /**
     * 减少用户金币
     * @param uid
     * @param num
     * @param content
     * @param userInterface
     */
    public static void MoneySetLess(String uid,String num,String content,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);//用户ID
        hashMap.put("num", num);//第几页
        hashMap.put("content", content);//每页多少条
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.MONEY_SET_LESS), userInterface);
    }

    /**
     * 用户发表评论
     * @param uid 用户ID
     * @param pid 	父级评论ID,一级评论则不传
     * @param theme_type 评论的主题类型
     * @param theme_id 评论的主题ID
     * @param content 评论内容
     * @param login_key 	登录后服务器返回的凭证
     * @param userInterface
     */
    public static void CommitSetUp(String uid,String pid,String theme_type,String theme_id,String content,String login_key,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        if (!pid.equals("")){
            hashMap.put("pid", pid);
        }
        if (!theme_type.equals("")){
            hashMap.put("theme_type", theme_type);
        }
        hashMap.put("theme_id", theme_id);
        hashMap.put("content", content);
        hashMap.put("login_key", login_key);
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.COMMIT_SETUP), userInterface);
    }

    /**
     * 根据应用获取评论
     * @param theme_type 	评论的主题类型
     * @param theme_id 评论的主题ID
     * @param page 第几页
     * @param limit 每页多少条
     * @param userInterface
     */
    public static void CommitGetByTheme(String theme_type,String theme_id,String page,String limit,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (!theme_type.equals("")){
            hashMap.put("theme_type", theme_type);
        }
        hashMap.put("theme_id", theme_id);
        if (!page.equals("")){
            hashMap.put("page", page);
        }
        if (!limit.equals("")){
            hashMap.put("limit", limit);
        }
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.COMMIT_GETBYTHEME), userInterface);
    }

    /**
     * 根据用户ID获取评论
     * @param uid 当前用户ID
     * @param page 第几页
     * @param limit 每页多少条
     * @param userInterface
     */
    public static void CommitGetByUid(String uid,String page,String limit,UserInterface userInterface) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        if (!page.equals("")){
            hashMap.put("page", page);
        }
        if (!limit.equals("")){
            hashMap.put("limit", limit);
        }
        initHttp(JmTools.JM(getContext(),hashMap,AppServerUrl.COMMIT_GETBYUID), userInterface);
    }



















//    public static void test(final UserInterface userInterface) {
//        initHttp("http:192.168.1.80:8080/page", null, userInterface);
//    }

}
