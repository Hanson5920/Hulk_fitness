package com.gz.hkjs.app.ui.main.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.api.ApiConstants;
import com.gz.hkjs.app.jpush.ExampleUtil;
import com.gz.hkjs.app.jpush.JPushManager;
import com.gz.hkjs.app.ui.main.fragment.TrainingMainFragment;
import com.gz.hkjs.app.util.AppUtil;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.MineTitleBar;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.dialog.LoadingDialog;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.utils.HttpUtils;
import com.lxh.userlibrary.utils.ToastUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import rx.functions.Action1;

import static com.lxh.userlibrary.message.Message.Type.USER_EXIT_SATEA;

/**
 * 账号安全 lwy
 */

public class AccountActivity extends BaseActivity {

    private LoadingDialog exitDialog;
    @BindView(R.id.mtb_account)
    MineTitleBar mtbAccount;
    @BindView(R.id.civ_account_phone)
    ImageView civAccountPhone;
    @BindView(R.id.tv_account_phone)
    TextView tvAccountPhone;
    @BindView(R.id.bt_change_phone)
    Button btChangePhone;
    @BindView(R.id.bt_change_password)
    Button btChangePassword;
    @BindView(R.id.tv_phone_exit_login)
    TextView tvPhoneExitLogin;
    @BindView(R.id.ll_account_top)
    LinearLayout llAccountTop;
    @BindView(R.id.tv_account_version)
    TextView tvAccountVersion;//版本号
    private String userPhone = "";

    public static final String FINISH_ACTIVITY = "finish_activity";

    @Override
    public int getLayoutId() {
        return R.layout.mine_account;
    }

    @Override
    public void initPresenter() {


    }

    @Override
    public void initView() {
        readAccount();
        mtbAccount.setLeftBack(true);
        mtbAccount.setOnLeftImagListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mtbAccount.setTitleText(getString(R.string.my_account));
        if (!TextUtils.isEmpty(userPhone) && userPhone.length() > 7) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < userPhone.length(); i++) {
                char c = userPhone.charAt(i);
                if (i >= 3 && i <= 7) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            tvAccountPhone.setText(sb.toString().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }
        tvAccountVersion.setText(AppUtil.getVersionName(mContext));
        mRxManager.on(FINISH_ACTIVITY, new Action1<String>() {
            @Override
            public void call(String s) {
                finish();
            }
        });
    }


    public void readAccount() {

        //创建SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
        //获得保存在SharedPredPreferences中的用户名和密码
        String username = sp.getString("username", "");
        //在用户名和密码的输入框中显示用户名和密码
        tvAccountPhone.setText(username.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.bt_change_password, R.id.tv_phone_exit_login, R.id.bt_change_phone})//R.id.bt_change_phone//更改手机暂时隐藏
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt_change_phone:
                startActivity(new Intent(this, PhoneActivity.class));
                break;
            case R.id.bt_change_password:
                startActivity(new Intent(this, PasswordActivity.class));
                finish();
                break;
            case R.id.tv_phone_exit_login:
                String uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
                String showText = this.getResources().getString(com.lxh.userlibrary.R.string.in_exit);
                exitDialog = new LoadingDialog(this, showText);
                exitDialog.show();
                loginOut();
                UserManage.UserLogout(uid, new UserInterface() {
                    @Override
                    public void onError(Call call, Exception e, String msg) {
                        ToastUtil.toastLong(AccountActivity.this, "退出失败，请稍候再试");
                        exitDialog.dismiss();
                    }

                    @Override
                    public void onSucceed(int state, String msg, String data, JSONObject obj) {
                        if (state == 200) {
                            exitDialog.dismiss();
                            DefaultSharePrefManager.putString(Constants.KEY_USER_ID, "");
                            DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, "");
                            DefaultSharePrefManager.putString(Constants.USER_NAME, "");
                            DefaultSharePrefManager.putString(Constants.LOGIN_KEY, "");
                            UserCenter.getInstance().getMessagePump().
                                    broadcastMessage(USER_EXIT_SATEA);
                            ToastUtil.toastShort(AccountActivity.this, "退出成功");
                            mRxManager.post(TrainingMainFragment.strLogout, "Logout");
                            finish();
                        }

                    }
                });
                break;
        }
    }

    private void loginOut(){
        Map<String, String> map =new HashMap<>();
        map.put("os", "1");
        map.put("version", AppUtil.getVersionName(mContext));
        map.put("uid", UserCenter.getUid());
        map.put("imei", ExampleUtil.getImei(mContext));
        HttpUtils.postStringAsync(ApiConstants.API_USER_LOGIN_OUT, map, mContext, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("husong","----->>>Loginout onError");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e("husong","----->>>Loginout onResponse ::"+response);
                JPushManager.getInstance(mContext).setTag("");
            }
        });
    }

}
