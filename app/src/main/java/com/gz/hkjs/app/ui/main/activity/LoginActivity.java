package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.api.ApiConstants;
import com.gz.hkjs.app.jpush.ExampleUtil;
import com.gz.hkjs.app.jpush.JPushManager;
import com.gz.hkjs.app.ui.main.fragment.TrainingMainFragment;
import com.gz.hkjs.app.util.AppUtil;
import com.gz.hkjs.app.util.CacheActivity;
import com.jaydenxiao.common.base.BaseActivity;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.entity.LoginUserInfo;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.tools.RegularExpressionTools;
import com.lxh.userlibrary.utils.HttpUtils;
import com.lxh.userlibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 登录
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.id_back)
    ImageView idBack;
    @BindView(R.id.id_total_toolbar_title)
    TextView idTotalToolbarTitle;
    @BindView(R.id.id_login_button)
    Button idLoginButton;
    @BindView(R.id.id_quick_reg)
    TextView idQuickReg;
    @BindView(R.id.id_login_name_ed)
    TextInputEditText idLoginNameEd;
    @BindView(R.id.id_login_pwd_ed)
    TextInputEditText idLoginPwdEd;
    @BindView(R.id.id_foget_pwd)
    TextView idFogetPwd;

    private String userPhone;
    private String userPassWord;

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activty_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(LoginActivity.this)) {
            CacheActivity.addActivity(LoginActivity.this);
        }
    }

    @Override
    public void initView() {

        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        idTotalToolbarTitle.setText("登录");
        idLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = idLoginNameEd.getText().toString().trim();
                userPassWord = idLoginPwdEd.getText().toString().trim();
                if (!RegularExpressionTools.isMobile(userPhone)) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(LoginActivity.this, "请输入正确的手机号码");
                    return;
                }
                if (userPassWord.equals("") || userPassWord == null||userPassWord.length()<6) {
                    com.lxh.userlibrary.utils.ToastUtil.toastLong(LoginActivity.this, "请输入6~16位数字或字母密码");
                    return;
                }
                UserManage.UserLogin(userPhone, userPassWord, "3", "1", new UserInterface() {
                    @Override
                    public void onError(Call call, Exception e, String msg) {
                        Logger.e("msg = " +msg);
                        Logger.e("失败了 e = "+e);
                        ToastUtil.toastShort("登录失败，请稍候再试");
                    }

                    @Override
                    public void onSucceed(int state, String msg, String data, JSONObject obj) {
                        Logger.e("state = " + state);
                        Logger.e("msg = " + msg);
                        if (state == 200) {
                            try {
                                LoginUserInfo user = null;
                                List<LoginUserInfo> userInfos = JSON.parseArray(data, LoginUserInfo.class);
                                if (userInfos != null && userInfos.size() > 0)
                                    user = userInfos.get(0);
                                DefaultSharePrefManager.putString(Constants.KEY_USER_ID, user.getUid());
                                Logger.i("uid=" + DefaultSharePrefManager.getString(Constants.KEY_USER_ID, ""));
                                DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, user.getLogo());
                                DefaultSharePrefManager.putString(Constants.USER_NAME, user.getName());
                                DefaultSharePrefManager.putString(Constants.LOGIN_KEY, user.getLogin_key());

                                //创建sharedPreference对象，info表示文件名，MODE_PRIVATE表示访问权限为私有的
                                SharedPreferences sp = getSharedPreferences("info", MODE_PRIVATE);
                                //获得sp的编辑器
                                SharedPreferences.Editor ed = sp.edit();

                                //以键值对的显示将用户名和密码保存到sp中
                                ed.putString("username", userPhone);
                                ed.putString("password", userPassWord);
                                //提交用户名和密码
                                ed.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            ToastUtil.toastShort("登录成功");
                            UserCenter.getInstance().getMessagePump().
                                    broadcastMessage(Message.Type.USER_LOGIN_END);
//                            MainActivity.startAction(LoginActivity.this);
                            mHandler.sendEmptyMessageDelayed(1, 500);
                            SetJPushTags();
                            checkLogin();
                            finish();

                        } else {
                            ToastUtil.toastShort("账号或密码错误，请重新输入");
                        }
                    }
                });
            }

        });


        idQuickReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegActivity.startAction(LoginActivity.this);
            }
        });
        idFogetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
                finish();
            }
        });


    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what==1){
                mRxManager.post(TrainingMainFragment.strLogin, "Login");
            }
        }
    };

    private void SetJPushTags(){
        String imei = ExampleUtil.getImei(mContext);
        String uid = UserCenter.getUid();
        String tag = imei+","+uid+","+"android";
        JPushManager.getInstance(mContext).setTag(tag);
    }

    private void checkLogin(){
        Map<String, String> map =new HashMap<>();
        map.put("os", "1");
        map.put("version", AppUtil.getVersionName(mContext));
        map.put("uid", UserCenter.getUid());
        map.put("imei", ExampleUtil.getImei(mContext));
        HttpUtils.postStringAsync(ApiConstants.API_USER_CHECKLOGIN, map, mContext, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.e("husong","----->>>onError");
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.e("husong","----->>>onResponse ::"+response);
            }
        });
    }

}
