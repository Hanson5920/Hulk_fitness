package com.lxh.userlibrary.constant;


import com.lxh.userlibrary.base.BaseAppServerUrl;

/**
 * Created by wbb on 2016/4/27.
 */
public final class Configs {

    public static void init() {
        switch (EnvType.getEnvType()) {
            case TEST_D_IP:
                // AppServer
                BaseAppServerUrl.userServerUrl = "http://testapi.user.1122.com/User3/";//用户中心
                break;
            case RELEASE:
                BaseAppServerUrl.userServerUrl = "http://api.user.xiaohua.com/User3/";//用户中心
                break;
        }
    }

}
