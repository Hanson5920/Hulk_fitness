package com.gz.hkjs.app.parameter;

import java.util.HashMap;

/**
 * 收藏列表
 */

public class JMClassCollectList {
    public static HashMap<String, String> MyJMClass(String page, String type) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("page", page);
        mJmh.put("type", type);
        return mJmh;
    }
}
