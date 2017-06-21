package com.lxh.userlibrary.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.BaseFragment;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.manager.update.UpdateUtil;
import com.lxh.userlibrary.utils.ToastUtil;


/**
 * Created by wbb on 2016/10/9.
 */

public class SetPasswordFragment extends BaseFragment {

    private EditText enterPassword;
    private EditText enterPasswordAgain;
    private String mPhone="";
    private String mCode="";
    private String isNew="0";// 0=是新注册 ; 1=找回密码
    private String openId,platformType,logoUrl,userName,has_phone_verify;

    public static SetPasswordFragment newInstance() {

        return new SetPasswordFragment();
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.set_password_layout;
    }

    @Override
    protected void bindView() {
        super.bindView();
        setHeadTitle("设置密码");
        Bundle bundle = mActivity.getIntent().getBundleExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_ARGS);
        isNew = bundle.getString("ISNEW");
        mPhone = bundle.getString("PHONE","");
        mCode = bundle.getString("CODE","");
        openId = bundle.getString("openId","");
        platformType = bundle.getString("platformType","");
        logoUrl = bundle.getString("logoUrl","");
        userName = bundle.getString("userName","");
        has_phone_verify = bundle.getString("has_phone_verify","");

        enterPassword = findView(R.id.enter_password);
        enterPasswordAgain = findView(R.id.enter_password_again);

        findViewAttachOnclick(R.id.registr_complete_btn);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.registr_complete_btn) {
            String password1 = enterPassword.getText().toString().trim();
            String password2 = enterPasswordAgain.getText().toString().trim();
            if (password1.equals("") || password1 == null) {
                ToastUtil.toastLong(mActivity, "请输入密码");
                return;
            }
            if (password2.equals("") || password2 == null
                    || !password1.equals(password2)) {
                ToastUtil.toastLong(mActivity, "两次密码不一致");
                return;
            }
            if (isNew.equals("0")) {
                UpdateUtil.SetPassword(mActivity,true,mPhone,password1,mCode, openId,platformType,logoUrl,userName,has_phone_verify,"","","",true);
            } else {
                UpdateUtil.SetPassword(mActivity,true,mPhone,password1,mCode,"","","","","","","","",false);
            }

        }
    }
}
