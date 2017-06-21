package com.lxh.userlibrary.manager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.lxh.userlibrary.utils.ToastUtil;

import java.util.List;

import okhttp3.Call;

/**
 * Created by lxh on 2017/4/6.
 * QQ-632671653
 */

public class SelectUserDialog extends Dialog{

    private RadioGroup radioGroup;
    private RadioButton savePhone,saveAuth;
    private Button cancelButton,comfirmButton;
    private String position = "";
    private Context mContext;
    private String openId = "",platformType = "";
    private List<SelectUserBean> dataList;

    public SelectUserDialog(@NonNull Context context, List<SelectUserBean> list,String openId,String open_type) {
        super(context, R.style.CheckVersionDialog);
        this.mContext = context;
        this.dataList = list;
        this.openId = openId;
        this.platformType = open_type;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_user_dialog);
        this.setCanceledOnTouchOutside(false);
        initView();
    }
    private void initView(){
        radioGroup = (RadioGroup) findViewById(R.id.choice_box);
        savePhone = (RadioButton) findViewById(R.id.save_phone_check);
        saveAuth = (RadioButton) findViewById(R.id.save_auth_check);
        cancelButton = (Button) findViewById(R.id.cancel_bindPhone);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectUserDialog.this.dismiss();
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
                    saveUserInfo("2",openId,platformType,dataList);
                }
                if (position.equals("1")){
                    saveUserInfo("1",openId,platformType,dataList);
                }
                SelectUserDialog.this.dismiss();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.save_phone_check) {
                    position = "0";
                    saveAuth.setChecked(false);
                } else if (checkedId == R.id.save_auth_check) {
                    position = "1";
                    savePhone.setChecked(false);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    /**
     * 保留用户所选择的用户信息
     * @param select 用户选择结果：1是oauth/2是phone
     * @param openid 该三方平台对应唯一ID
     * @param open_type 4QQ，5微信，6微博
     * @param list 保留用户信息数据
     */
    private void saveUserInfo(String select,String openid,String open_type,List<SelectUserBean> list){
        String token = "",phoneUid = "",oauthUid = "";
        for (int i = 0;i < list.size();i++){
            String Select = list.get(i).getSelect();
            if (select.equals(Select)){
                token = list.get(i).getToken();
                phoneUid = list.get(i).getUid();
            }else {
                oauthUid = list.get(i).getUid();
            }
        }
        final LoadingDialog loadingDialog = new LoadingDialog(mContext,"正在保留用户信息，请稍候...");
        loadingDialog.show();
        UserManage.UserSelectPhoneOrOAuth(select, token, phoneUid, oauthUid, openid, open_type, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                ToastUtil.toastShort(mContext,msg);
                loadingDialog.dismiss();
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                Log.e("TAG","=======保留用户信息======="+obj.toString());
                ToastUtil.toastShort(mContext, msg);
                loadingDialog.dismiss();
                switch (state) {
                    case 200:
                        SelectUserDialog.this.dismiss();
                        try {
                            LoginUserInfo user = null;
                            List<LoginUserInfo> userInfos = JSON.parseArray(data, LoginUserInfo.class);
                            if (userInfos != null && userInfos.size() > 0) user = userInfos.get(0);
                            DefaultSharePrefManager.putString(Constants.KEY_USER_ID, user.getUid());
                            DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, user.getLogo());
                            DefaultSharePrefManager.putString(Constants.USER_NAME, user.getName());
                            DefaultSharePrefManager.putString(Constants.LOGIN_KEY, user.getLogin_key());
                            PageSwitcher.switchToPage(mContext, FragmentFactory.FRAGMENT_TYPE_MINE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }
}
