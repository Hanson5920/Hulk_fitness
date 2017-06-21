package com.lxh.userlibrary.mineInterface;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;

/**
 * Created by wbb on 2016/05/25.
 */
public interface UserInterface {

    /**
     * 接口请求失败
     *
     * @param call
     * @param e    异常信息
     * @param msg  后天返回错误提示信息
     */
    void onError(Call call, Exception e, String msg);

    /**
     * 接口请求返回成功
     *
     * @param state 状态码
     * @param msg   后天返回成功提示信息
     * @param data  返回data数据
     * @param obj   JSONObject
     */
    void onSucceed(int state, String msg, String data, JSONObject obj);

}
