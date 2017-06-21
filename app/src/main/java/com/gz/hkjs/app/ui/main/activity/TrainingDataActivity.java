package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;

import com.gz.hkjs.app.R;
import com.jaydenxiao.common.base.BaseActivity;

/**
 * Created by Administrator on 2017/4/14.
 */

public class TrainingDataActivity extends BaseActivity {

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, TrainingDataActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_training_data;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {


    }
}
