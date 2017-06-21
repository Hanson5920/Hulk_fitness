package com.lxh.userlibrary.user;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.BaseFragment;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.manager.dialog.LoadingDialog;
import com.lxh.userlibrary.manager.update.UpdateUtil;
import com.lxh.userlibrary.tools.RegularExpressionTools;
import com.lxh.userlibrary.utils.ToastUtil;

import okhttp3.Call;


/**
 * Created by wbb on 2016/10/9.
 */

public class UserRegisterFragment extends BaseFragment {

    TimeCount time = new TimeCount(60000, 1000);//构造CountDownTimer对象

    private EditText registerPhone;
    private EditText registerCode;
    private Button sendCodeBtn;
    private String isNew = "0";
    private String openId,platformType,logoUrl,userName,has_phone_verify;

    public static UserRegisterFragment newInstance() {
        return new UserRegisterFragment();
    }


    @Override
    protected int getlayoutRes() {
        return R.layout.user_register_layout;
    }

    @Override
    protected void bindView() {
        Bundle bundle = mActivity.getIntent().getBundleExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_ARGS);
        isNew = bundle.getString("ISNEW","");
        openId = bundle.getString("openId","");
        platformType = bundle.getString("platformType","");
        logoUrl = bundle.getString("logoUrl","");
        userName = bundle.getString("userName","");
        has_phone_verify = bundle.getString("has_phone_verify","");
        if (isNew.equals("0")) {
            setHeadTitle("注册");
        } else {
            setHeadTitle("找回密码");
        }
        registerPhone = findView(R.id.register_phone);
        registerCode = findView(R.id.register_code);
        registerPhone.addTextChangedListener(mTextWatcher);

        sendCodeBtn = findViewAttachOnclick(R.id.send_code_btn);
        findViewAttachOnclick(R.id.register_on_button);
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void afterTextChanged(Editable s) {
            editStart = registerPhone.getSelectionStart();
            editEnd = registerPhone.getSelectionEnd();
            if (temp.length() > 0) {
                sendCodeBtn.setTextColor(mActivity.getResources().getColor(R.color.white));
                sendCodeBtn.setBackgroundResource(R.drawable.send_code_button);
            } else {
                sendCodeBtn.setTextColor(mActivity.getResources().getColor(R.color.common_text_color));
                sendCodeBtn.setBackgroundResource(R.drawable.send_code_button_gray);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
//          mTextView.setText(s);//将输入的内容实时显示
        }

    };

    /**
     * 发送 验证码
     *
     * @param phoneUnm 手机号
     */
    private void sendCode(String phoneUnm) {
        final LoadingDialog loadingDialog = new LoadingDialog(mContext,"正在发送验证码，请稍候...");
        loadingDialog.show();
        //用户注册 发送验证码
        UserManage.UserSendMark(phoneUnm, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastLong(mContext, msg);
                loadingDialog.dismiss();
              Log.e("TAG", "======发送验证码失败======:" + msg);
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                Log.e("TAG", "======发送验证码成功了====state==:" + state+"===data==="+data);
                loadingDialog.dismiss();
                switch (state){
                    case 200:
                        ToastUtil.toastLong(mContext,"验证码获取成功");
                        time.start();//开始计时
                        sendCodeBtn.setClickable(false);
                        sendCodeBtn.setTextColor(mActivity.getResources().getColor(R.color.common_text_color));
                        sendCodeBtn.setBackgroundResource(R.drawable.send_code_button_gray);
                        registerPhone.setEnabled(false);
                        break;
                    default:
                        ToastUtil.toastLong(mContext,msg);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.send_code_btn) {
            String phoneNum = registerPhone.getText().toString().trim();
            if (phoneNum.equals("") || phoneNum == null) {
                ToastUtil.toastLong(mActivity, "请输入手机号");
                return;
            }
            if (!RegularExpressionTools.isMobile(phoneNum)) {
                ToastUtil.toastLong(mActivity, "请输入正确的手机号码");
                return;
            }
//                XUtilLog.log_i("wbb", "======发送验证码======:");
            sendCode(phoneNum);

        } else if (i == R.id.register_on_button) {
            String phone = registerPhone.getText().toString().trim();
            if (phone.equals("") || phone == null) {
                ToastUtil.toastLong(mActivity, "请输入手机号");
                return;
            }
            if (!RegularExpressionTools.isMobile(phone)) {
                ToastUtil.toastLong(mActivity, "请输入正确的手机号码");
                return;
            }
            String code = registerCode.getText().toString().trim();
            if (code.equals("") || code == null) {
                ToastUtil.toastLong(mActivity, "请输入验证码");
                return;
            }
            //验证 验证码是否正确 注册/找回密码
            UpdateUtil.VerifyVerificationCode(mActivity, true, phone, code
                    ,openId,platformType,logoUrl,userName,has_phone_verify,isNew);

        }
    }

    /**
     * 发送验证码计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            sendCodeBtn.setTextColor(mActivity.getResources().getColor(R.color.white));
            sendCodeBtn.setBackgroundResource(R.drawable.send_code_button);
            sendCodeBtn.setText("发验证码");
            sendCodeBtn.setClickable(true);
            registerPhone.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            sendCodeBtn.setText(millisUntilFinished / 1000 + " S");
        }
    }

}
