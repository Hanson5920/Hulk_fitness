package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.util.CacheActivity;
import com.gz.hkjs.app.util.CountDownTimerUtils;
import com.jaydenxiao.common.base.BaseActivity;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.dialog.LoadingDialog;
import com.lxh.userlibrary.entity.LoginUserInfo;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.tools.RegularExpressionTools;
import com.lxh.userlibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/12.
 */

public class RegActivity extends BaseActivity {


    @BindView(R.id.id_back)
    ImageView idBack;
    @BindView(R.id.id_total_toolbar_title)
    TextView idTotalToolbarTitle;
    @BindView(R.id.id_reg_button)
    Button idRegButton;
    @BindView(R.id.tv_reg_xieyi)
    TextView tvRegXieyi;
    @BindView(R.id.tv_reg_num)
    TextView tvRegNum;
    @BindView(R.id.id_login_name_ed)
    TextInputEditText idLoginNameEd;
    @BindView(R.id.id_reg_name)
    TextInputLayout idRegName;
    @BindView(R.id.id_reg_num)
    TextInputEditText idRegNum;
    @BindView(R.id.id_login_pwd_ed)
    TextInputEditText idLoginPwdEd;


    private String registerPhone = "";
    private String registerPassword = "";
    private String registerCode = "";

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, RegActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(RegActivity.this)) {
            CacheActivity.addActivity(RegActivity.this);
        }
    }

    @Override
    public void initView() {
        idTotalToolbarTitle.setText("注册");
    }

    @OnClick({R.id.id_back, R.id.id_reg_button, R.id.tv_reg_xieyi, R.id.tv_reg_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_back://返回
                finish();
                break;
            case R.id.id_reg_button://注册


                registerPhone = idLoginNameEd.getText().toString().trim();
                if (registerPhone.equals("") || registerPhone == null) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入手机号");
                    return;
                }
                if (!RegularExpressionTools.isMobile(registerPhone)) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入正确的手机号码");
                    return;
                }
                registerPassword = idLoginPwdEd.getText().toString().trim();
                if (registerPassword.equals("") || registerPassword == null || registerPassword.length() < 6) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入6~16位数字或字母密码");
                    return;
                }
                registerCode = idRegNum.getText().toString().trim();
                if (registerCode.equals("") || registerCode == null) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(mContext, "请输入验证码");
                    return;
                }
                checkMark(registerPhone, registerCode);

                break;
            case R.id.tv_reg_xieyi://用户协议
                startActivity(new Intent(this, PublicWebActivity.class));
                break;
            case R.id.tv_reg_num://获取验证码
                registerPhone = idLoginNameEd.getText().toString().trim();
                if (registerPhone.equals("") || registerPhone == null) {
                    ToastUtil.toastShort("请输入手机号");

                } else {
                    final LoadingDialog loadingDialog = new LoadingDialog(mContext, "正在发送验证码，请稍候...");
                    loadingDialog.show();
                    UserManage.UserSendMark(registerPhone, new UserInterface() {
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
                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvRegNum, 60000, 1000);
                    mCountDownTimerUtils.start();
                }
                break;
        }
    }

    private void requestReg(){
        UserManage.UserPhoneRigster(registerPhone, registerPassword, registerCode, "", "", "", "", "", "", "", new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                Logger.e("请求注册失败");
                ToastUtil.toastShort("注册失败");

            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {

                if (state == 200) {
                    ToastUtil.toastShort("注册成功");
                    UserManage.UserLogin(registerPhone, registerPassword, "3", "1", new UserInterface() {//注册完成后自动登录
                        @Override
                        public void onError(Call call, Exception e, String msg) {

                            Logger.e("msg = " + msg);
                        }

                        @Override
                        public void onSucceed(int state, String msg, String data, JSONObject obj) {
                            if (state == 200) {
                                Logger.e("执行了注册后登陆");
                                LoginUserInfo user = null;
                                List<LoginUserInfo> userInfos = JSON.parseArray(data, LoginUserInfo.class);
                                if (userInfos != null && userInfos.size() > 0)
                                    user = userInfos.get(0);
                                DefaultSharePrefManager.putString(Constants.KEY_USER_ID, user.getUid());
                                DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, user.getLogo());
                                DefaultSharePrefManager.putString(Constants.USER_NAME, user.getName());
                                DefaultSharePrefManager.putString(Constants.LOGIN_KEY, user.getLogin_key());
                            }
                            UserCenter.getInstance().getMessagePump().
                                    broadcastMessage(Message.Type.USER_LOGIN_END);
                        }
                    });
                    JibenInformitionOne.startAction(RegActivity.this);
                    finish();

                } else {
                    ToastUtil.toastShort(msg);
                }

            }
        });
    }

    /**
     * 验证码是否正确
     *
     * @param phone
     * @param code
     */
    private void checkMark(String phone, final String code) {
        String showText = mContext.getResources().getString(com.lxh.userlibrary.R.string.in_validation);
        final LoadingDialog loadingDialog = new LoadingDialog(mContext, showText);
        loadingDialog.show();
        UserManage.UserCheckMark(phone, code, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                com.lxh.userlibrary.utils.ToastUtil.toastShort(mContext, msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                if (state == 200) {
                    loadingDialog.dismiss();
                    requestReg();
                }else {
                    com.lxh.userlibrary.utils.ToastUtil.toastShort(mContext, msg);
                    loadingDialog.dismiss();
                }
            }
        });
    }
}
