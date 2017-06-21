package com.lxh.userlibrary.entity;

/**
 * Created by lxh on 2017/4/7.
 * QQ-632671653
 */

public class SelectUserBean {

    private String token;
    private String uid;
    private String select;
    private String type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SelectUserBean{" +
                "token='" + token + '\'' +
                ", uid='" + uid + '\'' +
                ", select='" + select + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
