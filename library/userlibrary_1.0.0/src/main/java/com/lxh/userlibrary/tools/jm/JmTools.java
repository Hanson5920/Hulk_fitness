package com.lxh.userlibrary.tools.jm;


import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wbb on 2016/5/25.
 */
public class JmTools {

    /**
     * 输入加密 T
     *
     * @param str
     * @return
     */
    public static String DKAETYA16(String str) {

        String a = str.substring(0, 16);

        return a;
    }

    /**
     * 加密方法
     *
     * @param hashMap
     * @return
     */
    public static HashMap<String, String> JM(Context context, Map<String, String> hashMap, String service) {
        hashMap.put("api", UserCenter.USER_API);//接口的版本号
        hashMap.put("c_appid", UserCenter.APPID);//客户端应用平台标识ID
        hashMap.put("c_os","1");//客户端操作系统：1是安卓，2是IOS，3是WAP，4是PC，5是其它
        hashMap.put("c_version",DeviceUtil.getVersionName(context));//客户端应用版本号
        hashMap.put("service", service);
        String  logo = "";
        if (hashMap.containsKey("logo")){
            logo = hashMap.get("logo");
            hashMap.remove("logo");
        }
        String s = JmTools.NRJM(hashMap);
        hashMap.put("s", s);
        if (!logo.equals("")){
            hashMap.put("logo",logo);
        }
        Log.e("TAG","======hashMap==111==="+hashMap);
        Iterator iter = hashMap.keySet().iterator();
        JSONObject jsonObject = new JSONObject();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            jsonObject.put(key, hashMap.get(key));
        }
        long time = System.currentTimeMillis() / 1000;
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("data", JmTools.encryptionEnhanced(time + "", jsonObject.toJSONString()));
        hashMap1.put("T", time + "");
        return hashMap1;
    }

    /**
     * json 解密
     *
     * @param object
     * @return
     */
    public static String DecryptKey(JSONObject object){

        String result = object.getString("result");

        String t = object.getString("T");

        String keyString = Security.md5(t + UserCenter.DKAETYA);

        String key = DKAETYA16(keyString);

        //解密
        String data = Security.decrypt(key, result);

        return data;
    }

    /**
     * 字符串 加密
     *
     * @param time
     * @param mString
     * @return
     */
    public static String encryptionEnhanced(String time, String mString) {

        String key = Security.md5(time + UserCenter.DKAETYA);

        String key16 = DKAETYA16(key);

        String date = Security.encrypt(key16, mString);

        return date;
    }

    /**
     * 加密 url signkey
     *
     * @param hashMap
     * @return
     */
    public static String NRJM(Map<String, String> hashMap) {
        List<String> listKey = new ArrayList();
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listKey.add(key);
        }
        List<String> listSort = Sorting.listSort(listKey);
        String key = "";
        StringBuilder sb = new StringBuilder();
        for (String string : listSort) {
            String name = hashMap.get(string);
            key = sb.append(name).toString();
        }
        return Security.md5(key + UserCenter.URL_KEY);
    }

    /**
     * 用户加密
     *
     * @param hashMap
     * @param methond
     * @return
     */
    public static String USER_NRJM(HashMap<String, String> hashMap, String methond) {
//        hashMap.put("service", methond);
        List<String> listKey = new ArrayList();
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listKey.add(key);
        }
        List<String> listSort = Sorting.listSort(listKey);
        String key = "";
        StringBuilder sb = new StringBuilder();
        for (String string : listSort) {
            String name = hashMap.get(string);
            key = sb.append(name).toString();
        }
        return Security.md5(key +  UserCenter.URL_KEY);
    }

}
