package com.lxh.userlibrary.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.BaseFragment;
import com.lxh.userlibrary.constant.Constants;
//import com.lxh.userlibrary.greendao.LoginUserInfoDao;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.utils.MyTextUtils;
import com.lxh.userlibrary.utils.ToastUtil;

import okhttp3.Call;


/**
 * Created by wbb on 2016/10/28.
 */

public class UpdateUserDataFragment extends BaseFragment {

    private ProgressDialog loadingDialog;
    private EditText nicknameEdit;
    private String nickName;

    public static UpdateUserDataFragment newInstance() {
        return new UpdateUserDataFragment();
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.update_user_data_layout;
    }

    @Override
    protected void bindView() {
        setHeadTitle("编辑昵称");
        setRightText("确认");
        Bundle bundle = mActivity.getIntent().getBundleExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_ARGS);
        nickName = bundle.getString("NICKNAME");
        loadingDialog = new ProgressDialog(mActivity);
        loadingDialog.setMessage("修改中...");

        findViewIcon(R.id.nickname_cancel);
        nicknameEdit = findView(R.id.nickname_edit);
        nicknameEdit.setText(nickName+"");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.head_right_text) {
            String nickName = nicknameEdit.getText().toString();
            String uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
            String loginKey = DefaultSharePrefManager.getString(Constants.LOGIN_KEY, "");
            if (MyTextUtils.isEmpty(nickName)) {
                ToastUtil.toastLong(mContext, "请输入昵称");
            } else {
                updateUserData(uid,loginKey,nickName);
            }

        } else if (i == R.id.nickname_cancel) {
            nicknameEdit.setText("");
            nicknameEdit.setHint("请输入昵称");

        }
    }

    /**
     * 修改用户资料
     * @param uid 用户id
     * @param name 用户昵称
     */
    private void updateUserData(String uid,String loginKey, final String name){
        loadingDialog.show();
        UserManage.editUserInfo(uid,loginKey,"",name,"","","","","",new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                loadingDialog.dismiss();
            }
            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                loadingDialog.dismiss();
                switch (state){
                    case 200:
                        ToastUtil.toastLong(mContext,msg);
                        mActivity.onBackPressed();
                        DefaultSharePrefManager.putString(Constants.USER_NAME,name);
                        break;
                    default:
                        ToastUtil.toastLong(mContext,msg);
                        break;
                }
            }
        });
    }
}
