package com.gz.hkjs.app.parameter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/31.
 */

public class JMClassDetail {


    public static HashMap<String, String> MyJMClass(String id) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("id", id);
        return mJmh;
    }

    public static HashMap<String, String> FinishTrainJMClass(String energy,String id,String name,
                                                             String times, String score,String service) {
        HashMap<String, String> mJmh = new HashMap<>();
        mJmh.put("energy", energy);
        mJmh.put("id", id);
        mJmh.put("name", name);
        mJmh.put("times", times);
        mJmh.put("score", score);
        return mJmh;
    }
}
