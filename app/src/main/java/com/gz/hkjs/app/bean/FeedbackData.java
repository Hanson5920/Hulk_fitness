package com.gz.hkjs.app.bean;

import java.util.List;

/**
 * Created by lwy on 2017/4/24.
 */

public class FeedbackData {

    /**
     * ret : 200
     * msg : 意见反馈接口创建接口
     * data : ["10"]
     */

    private String ret;
    private String msg;
    private List<String> data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
