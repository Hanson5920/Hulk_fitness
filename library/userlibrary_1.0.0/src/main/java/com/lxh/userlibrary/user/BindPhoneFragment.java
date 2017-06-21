package com.lxh.userlibrary.user;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.BaseFragment;
import com.lxh.userlibrary.base.FragmentFactory;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.entity.LoginUserInfo;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.manager.dialog.LoadingDialog;
import com.lxh.userlibrary.tools.RegularExpressionTools;
import com.lxh.userlibrary.utils.ToastUtil;

import java.util.List;

import okhttp3.Call;

/**
 * Created by lxh on 2017/4/5.
 * QQ-632671653
 */

public class BindPhoneFragment extends BaseFragment {

    private EditText phoneEt, passwordEt;
    private Button bindBt;
    private String openid, open_type,has_phone_verify;

    public static BindPhoneFragment newInstance() {
        return new BindPhoneFragment();
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.bind_phone_layout;
    }

    @Override
    protected void bindView() {
        super.bindView();
        setHeadTitle("绑定手机");
        Bundle bundle = mActivity.getIntent().getBundleExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_ARGS);
        openid = bundle.getString("openid", "");
        open_type = bundle.getString("open_type", "");
        has_phone_verify = bundle.getString("has_phone_verify","");
        phoneEt = findView(R.id.bind_phone_et);
        passwordEt = findView(R.id.bind_password_et);
        bindBt = findViewAttachOnclick(R.id.bind_now_bt);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.bind_now_bt) {
            String phone = phoneEt.getText().toString();
            if (phone.isEmpty()||phone.equals("")){
                ToastUtil.toastShort(mContext,"请输入手机号");
                return;
            }
            if (!RegularExpressionTools.isMobile(phone)) {
                ToastUtil.toastLong(mActivity, "请输入正确的手机号码");
                return;
            }
            String password = passwordEt.getText().toString();
            if ("".equals(password)) {
                ToastUtil.toastLong(mActivity, "请输入密码");
                return;
            }
            bindPhone(openid,open_type,phone,password,has_phone_verify);
        }
    }

    /**
     * 绑定手机
     * @param openid
     * @param open_type
     * @param phone
     * @param pass
     */
    private void bindPhone(String openid, String open_type,String phone, String pass,String has_phone_verify){
        final LoadingDialog loadingDialog = new LoadingDialog(mContext,"正在关联中，请稍候...");
        loadingDialog.show();
        UserManage.BindOAuthHasPhone(openid, open_type, phone, pass, "3", "1",has_phone_verify, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastShort(mContext,msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                Log.e("TAG","======关联手机===obj======="+obj.toString());
                ToastUtil.toastShort(mContext,msg);
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
                }

            }
        });
    }
}
