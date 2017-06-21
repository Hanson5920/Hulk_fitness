package com.lxh.userlibrary.manager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.FragmentFactory;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.utils.ToastUtil;

/**
 * Created by lxh on 2017/4/5.
 * QQ-632671653
 */

public class BindHasPhoneDialog extends Dialog{


    private RadioGroup radioGroup;
    private RadioButton hasPhone,noPhone;
    private Button cancelButton,comfirmButton;
    private String position = "";
    private Context mContext;
    private String openId = "",platformType = "",logoUrl = "",userName = "",has_phone_verify = "";

    public BindHasPhoneDialog(@NonNull Context context, String openId, String platformType,
                              String logoUrl, String userName, String has_phone_verify) {
        super(context, R.style.CheckVersionDialog);
        this.mContext = context;
        this.openId = openId;
        this.platformType = platformType;
        this.logoUrl = logoUrl;
        this.userName = userName;
        this.has_phone_verify = has_phone_verify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bindphone_dialog);
        this.setCanceledOnTouchOutside(false);
        initView();
    }
    private void initView(){
        radioGroup = (RadioGroup) findViewById(R.id.choice_box);
        hasPhone = (RadioButton) findViewById(R.id.has_phone_check);
        noPhone = (RadioButton) findViewById(R.id.no_phone_check);
        cancelButton = (Button) findViewById(R.id.cancel_bindPhone);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindHasPhoneDialog.this.dismiss();
            }
        });
        comfirmButton = (Button) findViewById(R.id.comfirm_bindPhone);
        comfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position.equals("")){
                    ToastUtil.toastShort(mContext,"请选择所要关联的类型");
                    return;
                }
                if (position.equals("0")){
                    Bundle bundle = new Bundle();
                    bundle.putString("openid",openId);
                    bundle.putString("open_type",platformType);
                    bundle.putString("has_phone_verify",has_phone_verify);
                    PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_BIND_PHONE,bundle);
                }
                if (position.equals("1")){
                    Bundle bundle = new Bundle();
                    bundle.putString("openid",openId);
                    bundle.putString("open_type",platformType);
                    bundle.putString("logo_url",logoUrl);
                    bundle.putString("info_name",userName);
                    bundle.putString("has_phone_verify",has_phone_verify);
                    bundle.putString("ISNEW","0");
                    PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_USER_REGISTER,bundle);
                }
                BindHasPhoneDialog.this.dismiss();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.has_phone_check) {
                    position = "0";
                    noPhone.setChecked(false);
                } else if (checkedId == R.id.no_phone_check) {
                    position = "1";
                    hasPhone.setChecked(false);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
