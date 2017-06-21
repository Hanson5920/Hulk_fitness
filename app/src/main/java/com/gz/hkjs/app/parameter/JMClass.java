package com.gz.hkjs.app.parameter;


import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/31.
 */

public class JMClass {
    public static HashMap<String, String> MyJMClass(String page) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("page", page);
        return mJmh;
    }

    public static HashMap<String, String> RecipesJMClass(String page, String tagId) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("page", page);
        if(!TextUtils.isEmpty(tagId))
            mJmh.put("tags", tagId);
        return mJmh;
    }

    public static HashMap<String, String> collectionLists(String type, String page) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("page", page);
        mJmh.put("type", type);
        return mJmh;
    }
}
