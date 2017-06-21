package com.lxh.userlibrary.mineInterface;

/**
 * Created by lxh on 2017/4/13.
 * QQ-632671653
 */

public interface AliPayInterface {
    void success(String result);
    void cancel(String result);
    void fail(String result);
}
