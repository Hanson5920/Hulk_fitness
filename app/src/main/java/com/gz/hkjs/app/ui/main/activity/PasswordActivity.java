package com.gz.hkjs.app.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.util.CountDownTimerUtils;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.MineTitleBar;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.dialog.LoadingDialog;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.tools.RegularExpressionTools;
import com.lxh.userlibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.lxh.userlibrary.message.Message.Type.USER_EXIT_SATEA;


/**
 * 更换密码 lwy
 */

public class PasswordActivity extends BaseActivity {

    @BindView(R.id.mtb_password)
    MineTitleBar mineTitleBar;

    @BindView(R.id.et_set_phone)
    EditText etSetPhone;
    @BindView(R.id.et_set_auth_code)
    EditText etSetAuthCode;
    @BindView(R.id.tv_get_auth_code)
    TextView tvGetAuthCode;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;

    @BindView(R.id.cb_get_new_password)
    CheckBox cbGetNewPassword;
    @BindView(R.id.bt_change_password_sure)
    Button btChangePasswordSure;

    private LoadingDialog loadingDialog;
    private String newPassWord = "";
    private String code = "";
    private String userPhone = "";
    private String uid="";

    @Override
    public int getLayoutId() {
        return R.layout.mine_change_password;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mineTitleBar.setLeftBack(true);
        mineTitleBar.setOnLeftImagListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mineTitleBar.setTitleText(getString(R.string.my_password_change));

        etSetPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//手机号最多11位
        etNewPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});//密码最多11位
        cbGetNewPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {

                    etNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.et_set_phone, R.id.et_set_auth_code, R.id.tv_get_auth_code, R.id.et_new_password, R.id.bt_change_password_sure})
    public void onViewClicked(View view) {
        userPhone = etSetPhone.getText().toString();
        code = etSetAuthCode.getText().toString();
        newPassWord = etNewPassword.getText().toString();
        uid = UserCenter.getUid();
        switch (view.getId()) {
            case R.id.et_set_phone:
                break;
            case R.id.et_set_auth_code:
                break;
            case R.id.tv_get_auth_code:
                if (userPhone.equals("") || etSetPhone == null) {

                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入手机号");
                } else {
                    if (!RegularExpressionTools.isMobile(userPhone)) {
                        com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入正确的手机号码");
                        return;
                    }
                    loadingDialog = new LoadingDialog(mContext, "正在发送验证码，请稍候...");
                    loadingDialog.show();
                    UserManage.UserSendMark(userPhone, new UserInterface() {
                        @Override
                        public void onError(Call call, Exception e, String msg) {
                            com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, msg);
                            Logger.e("msg" + msg);
                            loadingDialog.dismiss();
                            Logger.e("请求验证码失败");
                        }

                        @Override
                        public void onSucceed(int state, String msg, String data, JSONObject obj) {
                            if (state == 200) {
                                Logger.e("请求验证码成功");
                                loadingDialog.dismiss();
                            }
                        }
                    });
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetAuthCode, 60000, 1000);
                    mCountDownTimerUtils.start();
                }
                break;
            case R.id.et_new_password:
                break;
            case R.id.bt_change_password_sure:
                if (userPhone.equals("") || userPhone == null) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入手机号");
                    return;
                }
                if (!RegularExpressionTools.isMobile(userPhone)) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入正确的手机号码");
                    return;
                }
                if (newPassWord.equals("") || newPassWord == null || newPassWord.length() < 6) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入密码6~16位数字或密码");
                    return;
                }

                if (code.equals("") || code == null) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入验证码");
                    return;
                }
                checkMark(userPhone, code);
                //修改密码
                loadingDialog.show();
                UserManage.UserNewPassword(userPhone, newPassWord, code, "3", new UserInterface() {
                    @Override
                    public void onError(Call call, Exception e, String msg) {
                        loadingDialog.dismiss();
                        Logger.i("请求修改密码失败");
                        com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请求修改密码失败");
                    }

                    @Override
                    public void onSucceed(int state, String msg, String data, JSONObject obj) {

                        loadingDialog.dismiss();
                        if (state == 200) {
                            Logger.e("msg = " + msg);
                            com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "修改密码成功");
                        }
                        finish();
                        UserManage.UserLogout(uid, new UserInterface() {
                            @Override
                            public void onError(Call call, Exception e, String msg) {
                                Logger.e("请求退出失败");
                                ToastUtil.toastLong(PasswordActivity.this, "退出失败，请稍候再试");
                            }

                            @Override
                            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                                Logger.i("state" + state);
                                if (state == 200) {
                                    DefaultSharePrefManager.putString(Constants.KEY_USER_ID, "");
                                    DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, "");
                                    DefaultSharePrefManager.putString(Constants.USER_NAME, "");
                                    DefaultSharePrefManager.putString(Constants.LOGIN_KEY, "");
                                    UserCenter.getInstance().getMessagePump().
                                            broadcastMessage(USER_EXIT_SATEA);
                                    ToastUtil.toastShort(PasswordActivity.this, "退出成功");
                                    finish();
                                }
                            }
                        });
                    }
                });
                break;
        }
    }

    /**
     * 验证码是否正确
     *
     * @param phone
     * @param code
     */
    private void checkMark(String phone, String code) {
        String showText = mContext.getResources().getString(com.lxh.userlibrary.R.string.in_validation);
        final LoadingDialog loadingDialog = new LoadingDialog(mContext, showText);
        loadingDialog.show();
        UserManage.UserCheckMark(phone, code, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                com.lxh.userlibrary.utils.ToastUtil.toastShort(mContext, msg);
                ToastUtil.toastLong(mContext, "获取验证码失败");
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                if (state == 200) {
                    loadingDialog.dismiss();

                }
            }
        });
    }
}
