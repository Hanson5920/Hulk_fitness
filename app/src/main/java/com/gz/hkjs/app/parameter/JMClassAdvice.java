package com.gz.hkjs.app.parameter;

import java.util.HashMap;

/**
 * Created by lwy on 2017/4/24.
 */

public class JMClassAdvice {

    public static HashMap<String, String> MyJMClass(String content, String contact, String picture) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("content", content);
        mJmh.put("contact", contact);
        mJmh.put("picture", picture);
        return mJmh;
    }
}
