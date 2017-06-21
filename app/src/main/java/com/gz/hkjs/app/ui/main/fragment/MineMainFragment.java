package com.gz.hkjs.app.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.ui.main.activity.AccountActivity;
import com.gz.hkjs.app.ui.main.activity.CollectActivity;
import com.gz.hkjs.app.ui.main.activity.FeedbackActivity;
import com.gz.hkjs.app.ui.main.activity.InformationActivity;
import com.gz.hkjs.app.ui.main.activity.LoginActivity;
import com.gz.hkjs.app.util.CacheDataManager;
import com.gz.hkjs.app.util.CircleImageView;
import com.gz.hkjs.app.util.DataCleanUtil;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.baseapp.BaseApplication;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.jaydenxiao.common.commonwidget.SwitchView;
import com.jaydenxiao.common.imagePager.BigImagePagerActivity;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.entity.LoginUserInfo;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.message.MessageCallback;
import com.lxh.userlibrary.message.MessageHelper;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.lxh.userlibrary.message.Message.Type.USER_DATA_PHOTO_CHAANGE;
import static com.lxh.userlibrary.message.Message.Type.USER_DATA_STATE;
import static com.lxh.userlibrary.message.Message.Type.USER_EXIT_SATEA;

/**
 * Created by Administrator on 2017/3/15.
 */

public class MineMainFragment extends BaseFragment implements MessageCallback {


    public static final int PHOTOZOOM = 0; // 相册/拍照
    public static final int PHOTOTAKE = 1; // 相册/拍照
    public static final int IMAGE_COMPLETE = 2; // 拍照结果
    @BindView(R.id.ntb_mine)
    NormalTitleBar ntbMine;

    @BindView(R.id.tv_mine_name)
    TextView tvMineName;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.sv_mine_img)
    SwitchView svMineImg;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.rl_collect)
    RelativeLayout rlCollect;
    @BindView(R.id.rl_account)
    RelativeLayout rlAccount;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @BindView(R.id.civ_user_img)
    CircleImageView civUserImg;
    @BindView(R.id.tv_mine_information)
    TextView tvMineInformation;

    private String cacheSize;
    private String nickName = "";
    private String headUrl = "";
    private String uid = "";
    private String loginKey = "";
    private MessageHelper mMessageHelper;


    //图片放大缩小
    private String userHeadUrl;
    List<String> userImgList = new ArrayList<>();//用户头像集合

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my;
    }


    /**
     * 刷新用户资料
     */
    @Override
    public void onResume() {
        super.onResume();
        UserCenter.getInstance().getMessagePump().
                broadcastMessage(USER_DATA_STATE);


        if (!uid.equals("")) {
            UserManage.getUserInfo(uid, loginKey, new UserInterface() {
                @Override
                public void onError(Call call, Exception e, String msg) {
                    Logger.e("获取用户信息失败");
                }

                @Override
                public void onSucceed(int state, String msg, String data, JSONObject obj) {
                    if (state == 200) {
                        Log.e("husong","获取用户信息成功 data:" + data);
                        String obj2 = obj.getJSONArray("data").get(0).toString();
                        LoginUserInfo loginUserInfo = JSON.parseObject(obj2, LoginUserInfo.class);

                        refreshUser(loginUserInfo.getName(), loginUserInfo.getLogo());
                        userHeadUrl = loginUserInfo.getLogo();

                        DefaultSharePrefManager.putString(Constants.USER_NAME, loginUserInfo.getName());
                        DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, loginUserInfo.getLogo());

                    }
                }
            });
        }
    }

    private void refreshUser(String name, String logo) {
        tvMineName.setText(name);
        Logger.e("更新用户头像:" + logo);
        ImageLoaderUtils.display(getContext(), civUserImg, logo);

    }

    @Override
    public void initPresenter() {

        //获取缓存大小（格式化size大小）
        try {
            cacheSize = CacheDataManager.getTotalCacheSize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvCache.setText(cacheSize);


    }

    @Override
    protected void initView() {
        mMessageHelper = new MessageHelper();
        mMessageHelper.setMessageCallback(this);
        mMessageHelper.attachMessage(USER_DATA_PHOTO_CHAANGE);
        mMessageHelper.attachMessage(USER_DATA_STATE);
        mMessageHelper.attachMessage(USER_EXIT_SATEA);
        mMessageHelper.registerMessages();//注册MessageHelper
        readNet();

        ntbMine.setTvLeftVisiable(false);
        ntbMine.setTitleText(getString(R.string.mine));
        uid = UserCenter.getUid();
    }


    private void initHeadView() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initHeadView();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMessageHelper.unRegisterMessages();
        mMessageHelper.clearMessages();
    }

    /**
     * 清理缓存
     */
    private void cleanCache() {
        Context context = BaseApplication.getInstance();
        File cacheDir = context.getExternalCacheDir(); // /storage/emulated/0/android/data/cache
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        DataCleanUtil.cleanInternalCache(getContext());// /data/data/com.xxx.xxx/cache
        if (cacheDir != null && cacheDir.isDirectory()) {
            DataCleanUtil.cleanCustomCache(cacheDir.getPath());
        }

        Toast.makeText(getContext(), "已清除" + cacheSize + "缓存,清理完成", Toast.LENGTH_SHORT).show();
        //更新界面
        tvCache.setText("0.00 B");
    }


    @OnClick({R.id.civ_user_img, R.id.tv_mine_name, R.id.rl_collect, R.id.rl_account, R.id.sv_mine_img, R.id.tv_cache, R.id.rl_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //头像的更新
            case R.id.civ_user_img:
                if (!uid.equals("")) {
                    userImgList.clear();
                    userImgList.add(userHeadUrl);
                    BigImagePagerActivity.startImagePagerActivity(getActivity(), userImgList, 0);
                }

                break;
            //名字更新
            case R.id.tv_mine_name:
                break;
            //收藏的点击事件
            case R.id.rl_collect:
                if (uid.equals("")) {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                } else {
                    startActivity(new Intent(mActivity, CollectActivity.class));
                }
                break;
            //账号安全
            case R.id.rl_account:
                if (uid.equals("")) {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                } else {
                    startActivity(new Intent(mActivity, AccountActivity.class));
                }
                break;

            //清理缓存的点击事件
            case R.id.tv_cache:

                if (tvCache.getText().toString().equals("0.00 B")) {
                    ToastUtil.toastShort(mActivity, "没有缓存可以清理了");
                } else {
                    ToastUtil.toastShort(mActivity, "缓存清理中...");
                    cleanCache();
                }

                break;
            //意见反馈
            case R.id.rl_feedback:
                startActivity(new Intent(mActivity, FeedbackActivity.class));
                break;
        }
    }

    /**
     * 开关wifi
     */
    public void readNet() {
        if (DefaultSharePrefManager.getBoolean("wifi", true)) {
            svMineImg.setChecked(true);
        } else {
            svMineImg.setChecked(false);
        }

        svMineImg.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {
                if (isChecked) {
                    DefaultSharePrefManager.putBoolean("wifi", true);
                } else {
                    DefaultSharePrefManager.putBoolean("wifi", false);
                }
            }
        });
    }

    @Override
    public void onReceiveMessage(Message message) {
        switch (message.type) {
            case USER_DATA_PHOTO_CHAANGE: //更新头像
                Logger.e("更新头像：" + headUrl);
                ImageLoaderUtils.display(getContext(), civUserImg, headUrl);
                break;
            case USER_DATA_STATE: //登录状态 处理
//                uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
                uid = UserCenter.getUid();
                if (uid.equals("")) {//没有登录
                    civUserImg.setImageResource(R.mipmap.meheadimg);
//                    ImageLoaderUtils.display(getContext(), civUserImg, R.mipmap.meheadimg);
                    tvMineName.setText("未登录");
                    tvLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mActivity, LoginActivity.class));
                        }
                    });
                    Logger.e("没有登录：" + headUrl);
                } else {
                    nickName = UserCenter.getUserName();//用户昵称
                    headUrl = UserCenter.getUserLogoUrl();//用户头像
                    tvMineName.setText(nickName + "");
                    tvMineName.setCompoundDrawablePadding(10);
                    ImageLoaderUtils.display(getContext(), civUserImg, headUrl);
                    Logger.e("登录：" + headUrl);
                    tvLogin.setVisibility(View.GONE);
                    tvMineInformation.setVisibility(View.VISIBLE);
                    tvMineInformation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(mActivity, InformationActivity.class));
                        }
                    });
                }
                break;

            case USER_EXIT_SATEA://退出登录
                UserCenter.getInstance().getMessagePump().
                        broadcastMessage(USER_DATA_STATE);
                Logger.e("退出登录：" + headUrl);
                civUserImg.setImageResource(R.mipmap.meheadimg);
                tvLogin.setVisibility(View.VISIBLE);
                tvLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mActivity, LoginActivity.class));
                    }
                });
                tvMineInformation.setVisibility(View.GONE);
                tvMineName.setText("未登录");
                Logger.e("执行了");

        }
    }
}
