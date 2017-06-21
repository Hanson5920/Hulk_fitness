package com.lxh.userlibrary.manager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.FragmentFactory;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.entity.LoginUserInfo;
import com.lxh.userlibrary.entity.SelectUserBean;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.tools.RegularExpressionTools;
import com.lxh.userlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by lxh on 2017/4/6.
 * QQ-632671653
 */

public class BindNoPhoneDialog extends Dialog {

    TimeCount time = new TimeCount(60000, 1000);//构造CountDownTimer对象
    private Button cancelButton,comfirmButton,sendMarkBtn;
    private Context mContext;
    private EditText phoneEt,markEt;
    private String openId = "",platformType = "",logoUrl = "",userName = "",no_phone_verify = "";

    public BindNoPhoneDialog(@NonNull Context context, String openId, String platformType,
                             String no_phone_verify) {
        super(context, R.style.CheckVersionDialog);
        this.mContext = context;
        this.openId = openId;
        this.platformType = platformType;
        this.no_phone_verify = no_phone_verify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_no_phone_dialog);
        this.setCanceledOnTouchOutside(false);
        initView();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 111:
                    bindNoPhone(phoneEt.getText().toString(),markEt.getText().toString());
                    break;
            }
        }
    };
    private void initView(){
        phoneEt = (EditText) findViewById(R.id.phone_et);
        markEt = (EditText) findViewById(R.id.mark_et);
        sendMarkBtn = (Button) findViewById(R.id.sendMark_btn);
        sendMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = phoneEt.getText().toString().trim();
                if (phoneNum.equals("")) {
                    ToastUtil.toastLong(mContext, "请输入手机号");
                    return;
                }
                if (!RegularExpressionTools.isMobile(phoneNum)) {
                    ToastUtil.toastLong(mContext, "请输入正确的手机号码");
                    return;
                }
//                XUtilLog.log_i("wbb", "======发送验证码======:");
                sendCode(phoneNum);
            }
        });
        cancelButton = (Button) findViewById(R.id.cancel_bindPhone);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindNoPhoneDialog.this.dismiss();
            }
        });
        comfirmButton = (Button) findViewById(R.id.comfirm_bindPhone);
        comfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneEt.getText().toString().trim();
                if (phone.equals("") || phone == null) {
                    ToastUtil.toastLong(mContext, "请输入手机号");
                    return;
                }
                if (!RegularExpressionTools.isMobile(phone)) {
                    ToastUtil.toastLong(mContext, "请输入正确的手机号码");
                    return;
                }
                String code = markEt.getText().toString().trim();
                if (code.equals("") || code == null) {
                    ToastUtil.toastLong(mContext, "请输入验证码");
                    return;
                }
                checkMark(phone,code);
            }
        });

    }

    /**
     * 验证短信码
     * @param phone
     * @param mark
     */
    private void checkMark(String phone,String mark){
        String showText = mContext.getResources().getString(R.string.in_validation);
        final LoadingDialog loadingDialog = new LoadingDialog(mContext, showText);
        loadingDialog.show();
        UserManage.UserCheckMark(phone, mark, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastShort(mContext, msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                ToastUtil.toastShort(mContext, msg);
                loadingDialog.dismiss();
                switch (state){
                    case 200:
                        handler.sendEmptyMessage(111);
                        break;
                }
            }
        });
    }

    /**
     * 老账户关联手机
     * @param phone
     * @param mark
     */
    private void bindNoPhone(String phone,String mark){
        final LoadingDialog loadingDialog = new LoadingDialog(mContext,"正在关联中，请稍候...");
        loadingDialog.show();
        UserManage.BindOAuthNOPhone(openId, platformType, phone, mark, no_phone_verify, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastShort(mContext, msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                Log.e("TAG","=======bindNoPhone======="+obj.toString());
                ToastUtil.toastShort(mContext, msg);
                loadingDialog.dismiss();
                switch (state){
                    case 200:
                        try{
                            LoginUserInfo user = null;
                            List<LoginUserInfo> userInfos = JSON.parseArray(data, LoginUserInfo.class);
                            if(userInfos!=null && userInfos.size()>0) user = userInfos.get(0);
                            DefaultSharePrefManager.putString(Constants.KEY_USER_ID,user.getUid());
                            DefaultSharePrefManager.putString(Constants.USER_LOGO_URL,user.getLogo());
                            DefaultSharePrefManager.putString(Constants.USER_NAME,user.getName());
                            DefaultSharePrefManager.putString(Constants.LOGIN_KEY,user.getLogin_key());
                            PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_MINE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 215:
                        try {
                            List<SelectUserBean> selectUserBeanList = new ArrayList<>();
                            selectUserBeanList.addAll(JSON.parseArray(data,SelectUserBean.class));
                            SelectUserDialog selectUserDialog = new SelectUserDialog(mContext,selectUserBeanList,openId,platformType);
                            selectUserDialog.show();
                            BindNoPhoneDialog.this.dismiss();
                        }catch (Exception e){

                        }
                        break;
                }
            }
        });
    }

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
                Log.e("TAG", "======发送验证码请求成功===="+obj.toString());
                loadingDialog.dismiss();
                switch (state){
                    case 200:
                        ToastUtil.toastLong(mContext,"验证码获取成功");
                        time.start();//开始计时
                        sendMarkBtn.setClickable(false);
                        sendMarkBtn.setTextColor(mContext.getResources().getColor(R.color.common_text_color));
                        sendMarkBtn.setBackgroundResource(R.drawable.send_code_button_gray);
                        sendMarkBtn.setEnabled(false);
                        break;
                    default:
                        ToastUtil.toastLong(mContext,msg);
                        break;
                }
            }
        });
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
            sendMarkBtn.setTextColor(Color.WHITE);
            sendMarkBtn.setBackgroundResource(R.drawable.send_code_button);
            sendMarkBtn.setText("发验证码");
            sendMarkBtn.setClickable(true);
            comfirmButton.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            sendMarkBtn.setText(millisUntilFinished / 1000 + " S");
        }
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
