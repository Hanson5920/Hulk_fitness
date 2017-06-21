package com.lxh.userlibrary.entity;

/**
 * 微信支付键值对信息
 * @author leibown
 * create at 2016/11/2 下午9:48
 */

public class WechatKeyValueInfo {
    private String key;
    private String value;

    public WechatKeyValueInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
