package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONObject;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.util.CacheActivity;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.StatusBarCompat;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/13.
 */

public class JibenInformitionThree extends BaseActivity {


    @BindView(R.id.rb_guide_nan)
    RadioButton rbGuideNan;
    @BindView(R.id.rb_guide_nv)
    RadioButton rbGuideNv;
    @BindView(R.id.jiben_rl)
    RadioGroup jibenRl;
    @BindView(R.id.button_jinen_3)
    Button buttonJinen3;

    private boolean isMustCheck = false;
    private int sex;
    private String uid = "";
    private String loginKey = "";

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, JibenInformitionThree.class);
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
    public int getLayoutId() {
        return R.layout.activity_jibenxinxi_three;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(JibenInformitionThree.this)) {
            CacheActivity.addActivity(JibenInformitionThree.this);
        }
    }

    @Override
    public void initView() {

        jibenRl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_guide_nan) {
                    Drawable myImage = getResources().getDrawable(R.drawable.me_btn_selected);
                    rbGuideNan.setCompoundDrawablesWithIntrinsicBounds(null, null, myImage, null);
                    rbGuideNv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    isMustCheck = true;
                    sex = 1;


                } else if (checkedId == R.id.rb_guide_nv) {
                    Drawable myImage = getResources().getDrawable(R.drawable.me_btn_selected);
                    rbGuideNan.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    rbGuideNv.setCompoundDrawablesWithIntrinsicBounds(null, null, myImage, null);
                    isMustCheck = true;
                    sex = 0;
                }
            }
        });

    }

    @OnClick(R.id.button_jinen_3)
    public void onViewClicked() {
        uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
        loginKey = DefaultSharePrefManager.getString(Constants.LOGIN_KEY, "");
        if (isMustCheck) {
            UserCenter.editUserInfo(uid, loginKey, "", "", String.valueOf(sex), "", "", "", "", "", new UserInterface() {
                @Override
                public void onError(Call call, Exception e, String msg) {
                    Logger.e("上传性别失败  msg :" + msg);
                }

                @Override
                public void onSucceed(int state, String msg, String data, JSONObject obj) {

                    if (state == 200) {
                    }
                }
            });
            JibenInformitionFour.startAction(JibenInformitionThree.this);
        } else {
            ToastUtil.toastShort(JibenInformitionThree.this, "请选择你的性别");
        }
    }
}
