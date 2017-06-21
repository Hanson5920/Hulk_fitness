package com.gz.hkjs.app.parameter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/31.
 */

public class JMClassVideo {
    public static HashMap<String, String> MyJMClass(String page, String load_type) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("page", page);
        mJmh.put("load_type", load_type);
        return mJmh;
    }
}
