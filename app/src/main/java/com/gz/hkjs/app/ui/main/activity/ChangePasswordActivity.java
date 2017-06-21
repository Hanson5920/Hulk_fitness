package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.gz.hkjs.app.R;
import com.jaydenxiao.common.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ChangePasswordActivity extends BaseActivity {


    @BindView(R.id.id_back)
    ImageView idBack;
    @BindView(R.id.id_total_toolbar_title)
    TextView idTotalToolbarTitle;


    /**
     * 入口
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, ChangePasswordActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_changepwd;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        idTotalToolbarTitle.setText("更改密码");
    }

}
