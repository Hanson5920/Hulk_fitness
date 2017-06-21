package com.lxh.userlibrary.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxh.userlibrary.R;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.message.MessageCallback;
import com.lxh.userlibrary.message.MessageHelper;
import com.lxh.userlibrary.utils.ActivityUtils;
import com.lxh.userlibrary.utils.StatusBarUtil;
import com.lxh.userlibrary.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;


/**
 * 作者：wbb
 */
public class BaseActivity extends FragmentActivity implements View.OnClickListener {
    protected View mRootView;
    public Context mContext;
    public Bundle savedInstanceState;

    public ImageView mainBack;
    public TextView headTitle;
    public TextView headRightText;
    public ImageView headRightImg;
    public RelativeLayout mainHeadLayout;

    private MessageHelper mMessageHelper;
    private FragmentFactory mFragmentFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = BaseActivity.this;
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        this.savedInstanceState = savedInstanceState;
        ActivityUtils.getScreenManager().pushActivity(this);//将activity加入栈中
        int layoutRes = getLayoutRes();
        if (layoutRes > 0) {
            if (mRootView == null) {
                mRootView = View.inflate(this, layoutRes, null);
            }
            setContentView(mRootView);
            bindViews();
        }
        mFragmentFactory = new FragmentFactory();
        attachAllMessage();
        mMessageHelper.registerMessages();
    }

    public void setHeadTitle(String title){
        findHead();
        ViewUtil.visible(headTitle);
        headTitle.setText(title);
    }

    public void setHeadTitle(int StringId){
        findHead();
        ViewUtil.visible(headTitle);
        headTitle.setText(StringId);
    }

    public void setRightImg(int id){
        findHead();
        ViewUtil.visible(headRightImg);
        ViewUtil.gone(headRightText);
        headRightImg.setBackgroundResource(id);
    }

    public void setRightText(String name){
        findHead();
        ViewUtil.visible(headRightText);
        ViewUtil.gone(headRightImg);
        headRightText.setText(name + "");
    }

    private void findHead(){
        mainBack = findView(mRootView, R.id.main_back);
        mainBack.setOnClickListener(this);
        headTitle = findView(mRootView, R.id.head_title);
        headRightImg = findView(mRootView, R.id.head_right_img);
        headRightText = findView(mRootView, R.id.head_right_text);
        headRightImg.setOnClickListener(this);
        headRightText.setOnClickListener(this);
        mainHeadLayout = findView(mRootView, R.id.main_head_layout);
    }

    public void setupStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (StatusBarUtil.MIUISetStatusBarLightMode(getWindow(), true)) {
//                XUtilLog.log_i("leibown", "miui");//小米
            } else if (StatusBarUtil.FlymeSetStatusBarLightMode(getWindow(), true)) {
//                XUtilLog.log_i("leibown", "flyme");//魅族
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                XUtilLog.log_i("leibown", ">6.0");
            } else {
//                XUtilLog.log_i("leibown", ">4.0");
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 返回Activity的布局文件ID
     *
     * @return
     */
    @LayoutRes
    protected int getLayoutRes() {
        return 0;
    }

    /**
     * 初始化View
     */
    protected void bindViews() {
    }

    /**
     * 初始化 MessageHelper 对象
     * 监听所有消息
     */
    protected void attachAllMessage() {
        mMessageHelper = new MessageHelper();
    }

    public void setMessageCallback(MessageCallback messageCallback){
        mMessageHelper.setMessageCallback(messageCallback);
    }

    protected void attachMessage(Message.Type type){
        mMessageHelper.attachMessage(type);
    }

    @Override
    protected void onDestroy() {
        if (mMessageHelper != null) {
            mMessageHelper.unRegisterMessages();//反注册MessageHelper
            mMessageHelper.clearMessages();//清除Messages
        }
        mFragmentFactory.clearCache();//清除Fragment工厂缓存
        mFragmentFactory = null;//FragmentFactory 置为null，使得GC能快速回收
        super.onDestroy();
        ActivityUtils.getScreenManager().popActivity(this);
    }

    public FragmentFactory getFragmentFactory() {
        return mFragmentFactory;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.head_right:
//                RightClick();
//                break;
        }
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param <V>具体的View类型
     * @return
     */
    protected <V> V findView(@IdRes int resId) {
        return ViewUtil.findView(this, resId);
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param <V>具体的View类型
     * @return
     */
    protected <V> V findViewIcon(@IdRes int resId) {
        TextView tv = ViewUtil.findViewAttachOnclick(this, resId, this);
        tv.setTypeface(ViewUtil.setIconFont(mContext));
        return (V) tv;
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param <V>具体的View类型
     * @return
     */
    protected <V> V findView(View rootView, @IdRes int resId) {
        return ViewUtil.findView(rootView, resId);
    }

    /**
     * findViewById 并添加OnClick事件
     *
     * @param resId
     * @param <V>   具体的View类型
     * @return
     */
    protected <V> V findViewAttachOnclick(@IdRes int resId) {
        return ViewUtil.findViewAttachOnclick(this, resId, this);
    }

    /**
     * findViewById 并添加OnClick事件
     *
     * @param resId
     * @param <V>   具体的View类型
     * @return
     */
    protected <V> V findViewAttachOnclickIcon(@IdRes int resId) {
        TextView tv = ViewUtil.findViewAttachOnclick(this, resId, this);
        tv.setTypeface(ViewUtil.setIconFont(mContext));
        return (V) tv;
    }

    protected <V> V findViewAttachOnclick(View rootView, @IdRes int resId) {
        return ViewUtil.findViewAttachOnclick(rootView, resId, this);
    }

}
