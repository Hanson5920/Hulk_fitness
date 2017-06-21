package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.Version;
import com.gz.hkjs.app.parameter.JMClassVersion;
import com.jaydenxiao.common.baseapp.AppManager;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * des:启动页
 * Created by xsf
 * on 2016.09.15:16
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
//                GetVersionData();
                MainActivity.startAction(SplashActivity.this);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    private void GetVersionData() {
        Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getVersionData(Api.getCacheControl(), JMClassVersion.MyJMClass("", ""))
                .subscribeOn(Schedulers.io())//在非UI线程中获取数据
                .observeOn(AndroidSchedulers.mainThread())//在UI线程中执行更新UI
                .subscribe(new Observer<Version>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("--------versiThrowableon---------:" + e);

                    }

                    @Override
                    public void onNext(Version version) {
                        System.out.println("--------version---------:" + version.getData().getApp_version());
                        //SPUtils.setSharedStringData(SplashActivity.this, AppConstant.VERSION, version.getData().getApp_version());
//                        MainActivity.startAction(SplashActivity.this);
                        finish();
                    }
                });
    }

}
