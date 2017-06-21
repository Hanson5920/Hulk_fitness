package com.gz.hkjs.app.api;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.gz.hkjs.app.app.AppApplication;
import com.gz.hkjs.app.app.AppConstant;
import com.gz.hkjs.app.jm.JmTools;
import com.gz.hkjs.app.util.AppUtil;
import com.lxh.userlibrary.UserCenter;
import com.orhanobut.logger.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.gz.hkjs.app.jm.JmTools.DecryptKey;

/**
 * Created by xxq on 2017/5/18 0018.
 * 加密解密拦截器
 * 1、拦截 request 获取bitmap 加密 重构request
 * 2、拦截 response 解密
 * 3、在这里还设置了基本参数  addBaseParams() ,目前添加了  os 、version 、 uid
 */

public class EncryptDecryptInterceptor implements Interceptor {
    private boolean isEncrypt = true;//是否需要加密

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //
        request = handlerRequest(request, resetParams(request));
        if (request == null) {
            throw new RuntimeException("Request返回值不能为空");
        }

        //
        Response response = chain.proceed(request);
        if (response == null) {
            throw new RuntimeException("Response返回值不能为空");
        }
        if (isEncrypt && response.code() != 504) {//加密返回来需要解密
            String string = readResponseStr(response);
            Logger.i("返回来的数据 = " + response.toString());
            string = DecryptKey(JSON.parseObject(string));
            Logger.i("解密后的数据  " + string);
            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), string)).build();
        } else {
            return response;
        }
    }


    //===========================加密=========================

    /**
     * 重新生成参数
     *
     * @param request
     * @return
     */
    private HashMap<String, String> resetParams(Request request) {
        //GET POST DELETE PUT PATCH
        String method = request.method();
        HashMap<String, String> params = null;
        if ("GET".equals(method)) {
            params = doGet(request);
        } else if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)) {
            RequestBody body = request.body();
            if (body != null && body instanceof FormBody) {
                params = doForm(request);
            }
        }
//        Logger.i("request = " + request.toString());
        params = addBaseParams(params);
        if (isEncrypt) {
            params = encryptParams(request, params);
        }
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            Logger.i(" resetParams  entry.getValue = " + entry.getValue() + " entry.getKey = " + entry.getKey());
//        }
        return params;
    }

    /**
     * 获取get方式的请求参数
     *
     * @param request
     * @return
     */
    private HashMap<String, String> doGet(Request request) {
        HashMap<String, String> params = new HashMap<>();
        HttpUrl url = request.url();
        Set<String> strings = url.queryParameterNames();
        if (strings != null) {
            Iterator<String> iterator = strings.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                String name = iterator.next();
                String value = url.queryParameterValue(i);
                params.put(name, value);
                i++;
            }
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            Logger.i(" get  entry.getValue = " + entry.getValue() + " entry.getKey = " + entry.getKey());
        }

        return params;
    }

    /**
     * 获取表单的请求参数
     *
     * @param request
     * @return
     */
    private HashMap<String, String> doForm(Request request) {
        HashMap<String, String> params = new HashMap<>();
        FormBody body = null;
        try {
            body = (FormBody) request.body();
        } catch (ClassCastException c) {
        }
        if (body != null) {
            int size = body.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    params.put(body.name(i), body.value(i));
                }
            }
        }
//        if (params.size() != 0) {
//            for (Map.Entry<String, String> entry : params.entrySet()) {
//                Logger.i(" post  entry.getValue = " + entry.getValue() + " entry.getKey = " + entry.getKey());
//            }
//        }
        return params;
    }

    /**
     * 重构 request
     *
     * @param request
     * @param params
     * @return
     */
    private Request handlerRequest(Request request, HashMap<String, String> params) {
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            Logger.i(" handlerRequest  entry.getValue = " + entry.getValue() + " entry.getKey = " + entry.getKey());
//        }
        String method = request.method();
        if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method) || "PATCH".equals(method)) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    bodyBuilder.add(entry.getKey(), entry.getValue());
                }
                return request.newBuilder().method(method, bodyBuilder.build()).build();
            }
        }
        return request;
    }

    /**
     * 增加基本参数
     *
     * @param params
     * @return
     */
    private HashMap<String, String> addBaseParams(HashMap<String, String> params) {
        Log.e("husong", "------------>>>addBaseParams  os="+AppConstant.OS+ "  version="+AppUtil.getVersionName(AppApplication.getContext())+
        "  uid="+UserCenter.getUid());
        params.put("os", AppConstant.OS);
        params.put("version", AppUtil.getVersionName(AppApplication.getContext()));
        String uid = UserCenter.getUid();
        if (!TextUtils.isEmpty(uid)) {
            params.put("uid", uid);
        }
        return params;
    }

    /**
     * 加密参数
     *
     * @param params
     * @return
     */
    private HashMap<String, String> encryptParams(Request request, HashMap<String, String> params) {
        String serviceApi = request.url().toString();
        serviceApi = serviceApi.split("=")[1].trim();
        Logger.i("加密前的数据  = " + params.toString());
        params = JmTools.JM(params, serviceApi);
        return params;
    }


    //===================================================================

    //=================================解密==============================

    /**
     * 读取Response返回String内容
     *
     * @param response
     * @return
     */
    private String readResponseStr(Response response) {
        ResponseBody body = response.body();
        BufferedSource source = body.source();
        try {
            source.request(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        MediaType contentType = body.contentType();
        Charset charset = Charset.forName("UTF-8");
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        String s = null;
        Buffer buffer = source.buffer();
        if (isPlaintext(buffer)) {
            s = buffer.clone().readString(charset);
        }
        return s;
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

}
