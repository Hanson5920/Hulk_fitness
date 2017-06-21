package com.gz.hkjs.app.ui.main.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
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
import com.gz.hkjs.app.ui.main.fragment.TrainingMainFragment;
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
 * 更换手机号 lwy
 */

public class PhoneActivity extends BaseActivity {


    @BindView(R.id.mtb_phone)
    MineTitleBar mtbPhone;
    @BindView(R.id.et_set_phone)
    EditText etSetPhone;
    @BindView(R.id.et_set_auth_code)
    EditText etSetAuthCode;
    @BindView(R.id.tv_get_auth_code)
    TextView tvGetAuthCode;
    @BindView(R.id.bt_change_phone_sure)
    Button btChangePhoneSure;
    @BindView(R.id.et_old_phone)
    TextView etOldPhone;
    @BindView(R.id.et_old_password)
    EditText etOldPassWord;
    @BindView(R.id.cb_get_old_password)
    CheckBox cbGetOldPassword;
    private String newPhone;//新手机号
    private String oldPhone;//老手机号
    private String oldPassWord;//老手机号密码
    private String newCode;//新手机号获取的验证码
    private LoadingDialog loadingDialog;
    private String username;
    private String uid;

    @Override
    public int getLayoutId() {
        return R.layout.mine_change_phone;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        readAccount();
        mtbPhone.setLeftBack(true);
        mtbPhone.setOnLeftImagListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mtbPhone.setTitleText(getString(R.string.my_phone_change));

        etSetPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//新手机号最多11位
//        newPhone = etSetPhone.getText().toString();
        etOldPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});//原手机号最多11位
//        oldPhone = etSetPhone.getText().toString();
//        oldPassWord = etOldPassWord.getText().toString();
//        newCode = etSetAuthCode.getText().toString().trim();
        uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
        cbGetOldPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etOldPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {

                    etOldPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
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


    @OnClick({R.id.tv_get_auth_code, R.id.bt_change_phone_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_auth_code://获取验证码
                newPhone = etSetPhone.getText().toString();
                if (TextUtils.isEmpty(newPhone) || etSetPhone == null) {
                    ToastUtil.toastShort("请输入手机号");
                } else {
                    if (!RegularExpressionTools.isMobile(newPhone)) {
                        com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入正确的手机号码");
                        return;
                    }

                    if(TextUtils.equals(newPhone, username)){
                        com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "新手机号与原号码相同，请重新输入");
                        return;
                    }

                    String etOldPwd = etOldPassWord.getText().toString().trim();
                    if(!TextUtils.equals(oldPassWord, etOldPwd)){
                        com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "原密码有误");
                        return;
                    }

                    loadingDialog = new LoadingDialog(mContext, "正在发送验证码，请稍候");
                    loadingDialog.show();
                    UserManage.UserSendMark(newPhone, new UserInterface() {
                        @Override
                        public void onError(Call call, Exception e, String msg) {
                            Logger.e("msgfail = " + msg);
                            loadingDialog.dismiss();
                            ToastUtil.toastShort("发送失败，请稍候再试");
                        }

                        @Override
                        public void onSucceed(int state, String msg, String data, JSONObject obj) {

                            if (state == 200) {
                                Logger.e("msgsuccess = " + msg);
                                ToastUtil.toastShort("发送成功");
                                loadingDialog.dismiss();
                            }
                        }
                    });
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetAuthCode, 60000, 1000);
                    mCountDownTimerUtils.start();
                }
                break;

            case R.id.bt_change_phone_sure://确定修改
                newPhone = etSetPhone.getText().toString();
                oldPassWord = etOldPassWord.getText().toString();
                newCode = etSetAuthCode.getText().toString().trim();
//                if (oldPhone.equals("") || oldPhone == null) {
//                    ToastUtil.toastShort("请输入原手机号");
//                    return;
//                }
                if (oldPassWord.equals("") || oldPassWord == null) {
                    ToastUtil.toastShort("请输入密码");
                    return;
                }
                if (newPhone.equals("") || newPhone == null) {
                    ToastUtil.toastShort("请输入手机号");
                    return;
                }
                if (newCode.equals("") || newCode == null) {
                    ToastUtil.toastShort("请输入验证码");
                    return;
                }
                checkMark(newPhone, newCode);
                break;
        }
    }

    public void readAccount() {

        //创建SharedPreferences对象
        SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);

        //获得保存在SharedPredPreferences中的用户名和密码
        username = sp.getString("username", "");
        oldPassWord = sp.getString("password", "");
//        //在用户名和密码的输入框中显示用户名和密码
        etOldPhone.setText(username.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
    }

    private void checkMark(String changPhone, String code) {
        String showText = mContext.getResources().getString(com.lxh.userlibrary.R.string.in_validation);
        loadingDialog = new LoadingDialog(mContext, showText);
        loadingDialog.show();
        UserManage.UserCheckMark(changPhone, code, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                com.lxh.userlibrary.utils.ToastUtil.toastShort(mContext, msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                if (state == 200) {
                    loadingDialog.dismiss();

                    String changge = "正在修改，请稍候";
                    loadingDialog = new LoadingDialog(mContext, changge);
                    loadingDialog.show();
                    UserManage.UserChangeUser(username, oldPassWord, "3", "1", newPhone, "3", newCode, new UserInterface() {
                        @Override
                        public void onError(Call call, Exception e, String msg) {
                            loadingDialog.dismiss();
                            ToastUtil.toastShort("修改失败，请稍候再试");
                        }

                        @Override
                        public void onSucceed(int state, String msg, String data, JSONObject obj) {
                            loadingDialog.dismiss();
                            if (state == 200) {
                                ToastUtil.toastShort("修改成功！");
//                                finish();

                                UserManage.UserLogout(uid, new UserInterface() {
                                    @Override
                                    public void onError(Call call, Exception e, String msg) {
                                        Logger.e("退出失败 msg:" + msg);
                                    }

                                    @Override
                                    public void onSucceed(int state, String msg, String data, JSONObject obj) {
                                        if (state == 200) {
                                            DefaultSharePrefManager.putString(Constants.KEY_USER_ID, "");
                                            DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, "");
                                            DefaultSharePrefManager.putString(Constants.USER_NAME, "");
                                            DefaultSharePrefManager.putString(Constants.LOGIN_KEY, "");
                                            UserCenter.getInstance().getMessagePump().
                                                    broadcastMessage(USER_EXIT_SATEA);
                                            mRxManager.post(TrainingMainFragment.strLogout, "Logout");
                                            Intent i = new Intent(mContext, LoginActivity.class);
                                            mContext.startActivity(i);
                                            mRxManager.post(AccountActivity.FINISH_ACTIVITY, "finish_activity");
                                            finish();
                                        }
                                    }
                                });
//                                finish();
                            }else {
                                ToastUtil.toastShort(msg);
                            }
                        }
                    });
                }else{
                    com.lxh.userlibrary.utils.ToastUtil.toastShort(mContext, msg);
                    loadingDialog.dismiss();
                }
            }
        });
    }
}
