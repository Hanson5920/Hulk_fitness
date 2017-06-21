package com.lxh.userlibrary.tools;

import com.lxh.userlibrary.constant.Constants;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by wbb on 2016/10/12.
 */

public class ThirdPartyPlatFrom {

    public ThirdPartyPlatFrom() {
    }

    private static final ThirdPartyPlatFrom mInstance = new ThirdPartyPlatFrom();

    public static ThirdPartyPlatFrom getInstance() {
        return mInstance;
    }

    /**
     * 第三方平台 登录
     * @param type
     * @param listener
     */
    public void AdjustPlatform(String type, PlatformActionListener listener){
        if (type.equals(Constants.QQ)){// QQ
            Platform qq = ShareSDK.getPlatform(QQ.NAME);
            qq.SSOSetting(false);  //设置false表示使用SSO授权方式
            qq.setPlatformActionListener(listener); // 设置分享事件回调
            qq.showUser(null);//授权并获取用户信息
        }
        if (type.equals(Constants.WECHAT)){// 微信
            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
            wechat.SSOSetting(false);  //设置false表示使用SSO授权方式
            wechat.setPlatformActionListener(listener); // 设置分享事件回调
            wechat.showUser(null);//授权并获取用户信息
        }
        if (type.equals(Constants.WEIBO)){// 微博
            Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            weibo.SSOSetting(false);  //设置false表示使用SSO授权方式
            weibo.setPlatformActionListener(listener); // 设置分享事件回调
            weibo.showUser(null);//授权并获取用户信息
        }

    }

}
