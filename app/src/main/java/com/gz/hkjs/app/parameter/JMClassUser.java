package com.gz.hkjs.app.parameter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/31.
 */

public class JMClassUser {

    public static HashMap<String, String> MyJMClass() {
        HashMap<String, String> mJmh = new HashMap<>();
        return mJmh;
    }

    public static HashMap<String, String> MyHomesDelete(String id) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("id", id);
        return mJmh;
    }
}
