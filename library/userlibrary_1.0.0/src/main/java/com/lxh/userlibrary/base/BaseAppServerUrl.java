package com.lxh.userlibrary.base;

/**
 * Created by wbb on 2016/3/24.
 */
public class BaseAppServerUrl {

    //用户信息 接口
    public static String userServerUrl = "";

    public static String appStatisticalUrl = "";


    /*用户注册登录*/
//    public static String getAppServerUserUrl() {
//        return BaseAppServerUrl.userServerUrl + "?service=";
//    }

    /*APP统计接口路径*/
    public static String getAppStatisticaUrl() {
        return BaseAppServerUrl.appStatisticalUrl;
    }


}
