package com.gz.hkjs.app.util;

import android.content.Context;
import android.content.Intent;

import com.gz.hkjs.app.ui.main.activity.LoginActivity;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;

/**
 * Created by Administrator on 2017/5/16 0016.
 * 登录工具
 */

public class LoginUtil {


    public static Boolean isLogin(Context context) {
        if (DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "").equals("")) {//没有登录
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        } else {//登录了
            return true;
        }
    }

}
