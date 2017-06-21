package com.gz.hkjs.app.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.gz.hkjs.app.BuildConfig;
import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.TimeUtil;
import com.lxh.userlibrary.UserCenter;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

import cn.jpush.android.api.JPushInterface;

/**
 * APPLICATION
 */
public class AppApplication extends BaseApplication {
    private boolean DEBUG = BuildConfig.LOG_DEBUG;
    private static SharedPreferences mPreferences;

    private static Context mContext;

    public static synchronized SharedPreferences getPreferences() {
        return mPreferences;
    }

    public static Context getContext() {
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        //初始化logger
        if (DEBUG) {
            Logger.init("fitness")                 // default PRETTYLOGGER or use just init()
                    .methodCount(1)
                    .logLevel(LogLevel.FULL)        // default LogLevel.FULL
                    .methodOffset(0);                // default 0
        } else {
            Logger.init()                 // default PRETTYLOGGER or use just init()
                    .methodCount(0)                 // default 2
                    .hideThreadInfo()               // default shown
                    .logLevel(LogLevel.NONE)        // default LogLevel.FULL
                    .methodOffset(0);
        }
        mContext = getApplicationContext();

        UserCenter.init(getApplicationContext(), "cxmxjianshen", "10", "3");//TODO
        //初始化OSS配置

        mPreferences = getSharedPreferences("config", Context.MODE_PRIVATE);

        addDefaultCache(mPreferences);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }


    private void addDefaultCache(SharedPreferences mPreferences) {
        String defaultCache = mPreferences.getString("cache", "-1");
        SharedPreferences.Editor editor = mPreferences.edit();
        if (defaultCache.equals("-1")) {
            editor.putString("cache", "0");
            String str = TimeUtil.getSystemTime("yyyyMMdd");
            editor.putString("date", str);
        }
        editor.commit();
    }
}
