package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.util.CacheActivity;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.jaydenxiao.common.commonwidget.StatusBarCompat;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/4/13.
 */

public class JibenInformitionOne extends BaseActivity {


    @BindView(R.id.bt_guide_one)
    Button btGuideOne;
    @BindView(R.id.cb_one)
    CheckBox cbOne;
    @BindView(R.id.cb_two)
    CheckBox cbTwo;
    @BindView(R.id.cb_three)
    CheckBox cbThree;


    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, JibenInformitionOne.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.start_color));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarCompat.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(JibenInformitionOne.this)) {
            CacheActivity.addActivity(JibenInformitionOne.this);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_jibenxinxi_one;
    }

    @Override
    public void initPresenter() {

    }

    private boolean isMustChecked = false;

    @Override
    public void initView() {


        cbOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    cbTwo.setChecked(false);
                    cbThree.setChecked(false);
                    isMustChecked = true;
                }
            }
        });
        cbTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    cbOne.setChecked(false);
                    cbThree.setChecked(false);
                    isMustChecked = true;
                }
            }
        });
        cbThree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    cbOne.setChecked(false);
                    cbTwo.setChecked(false);
                    isMustChecked = true;
                }
            }
        });
        btGuideOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMustChecked) {
                    Logger.e("跳转了");
                    JibenInformitionTwo.startAction(JibenInformitionOne.this);
                } else {
                    ToastUtil.showShort("请选择你的健身目的，好让我们为你推荐合适的课程");
                }
            }
        });

    }
}
