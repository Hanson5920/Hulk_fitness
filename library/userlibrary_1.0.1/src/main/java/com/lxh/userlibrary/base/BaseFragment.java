package com.lxh.userlibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lxh.userlibrary.R;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.message.MessageCallback;
import com.lxh.userlibrary.message.MessageHelper;
import com.lxh.userlibrary.utils.StatusBarUtil;
import com.lxh.userlibrary.utils.ViewUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/7/22.
 */
public class BaseFragment extends Fragment implements View.OnClickListener, MessageCallback {

    public Context mContext;
    public Bundle savedInstanceState;
    protected BaseActivity mActivity;
    public ImageView mainBack;
    public TextView headTitle;
    public TextView headRightText;
    public ImageView headRightImg;
    public RelativeLayout mainHeadLayout;
    private int layoutRes;
    public View mRootView;

    private MessageHelper mMessageHelper;

    protected int getlayoutRes() {
        return 0;
    }

    public String className = "123";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageHelper = new MessageHelper();
        mMessageHelper.setMessageCallback(this);
        attachAllMessage();
        mMessageHelper.registerMessages();//注册MessageHelper
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        layoutRes = getlayoutRes();
        LinearLayout actionbarRootLayout = (LinearLayout) View.inflate(getContext(), R.layout.base_layout, null);
        View contentView = View.inflate(getContext(), layoutRes, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        lp.weight = 1;
        actionbarRootLayout.addView(contentView, lp);
        mRootView = actionbarRootLayout;
        bindView();
        return actionbarRootLayout;
    }

    public void setHeadTitle(String title) {
        findHead();
        ViewUtil.visible(headTitle);
        headTitle.setText(title);
    }

    public void setHeadTitle(int StringId) {
        findHead();
        ViewUtil.visible(headTitle);
        headTitle.setText(StringId);
    }

    public void setRightImg(int id) {
        findHead();
        ViewUtil.visible(headRightImg);
        ViewUtil.gone(headRightText);
        headRightImg.setBackgroundResource(id);
    }

    public void setRightText(String name) {
        findHead();
        ViewUtil.visible(headRightText);
        ViewUtil.gone(headRightImg);
        headRightText.setText(name + "");
    }

    private void findHead() {
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
            if (StatusBarUtil.MIUISetStatusBarLightMode(mActivity.getWindow(), true)) {
//                XUtilLog.log_i("leibown", "miui");//小米
            } else if (StatusBarUtil.FlymeSetStatusBarLightMode(mActivity.getWindow(), true)) {
//                XUtilLog.log_i("leibown", "flyme");//魅族
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                XUtilLog.log_i("leibown", ">6.0");
            } else {
//                XUtilLog.log_i("leibown", ">4.0");
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    protected final void attachMessage(Message.Type type) {
        mMessageHelper.attachMessage(type);
    }

    /**
     * 监听所有消息
     */
    protected void attachAllMessage() {

    }

    /**
     * 初始化View
     */
    protected void bindView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(className);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(className);
    }

    /**
     * 处理返回事件
     *
     * @return true表示在fragment内部已经完成了对返回事件的处理，外部不需要再处理;
     * false表示外部需要对返回事件继续处理
     */
    public boolean goBack() {
        return false;
    }

    @Override
    public void onClick(final View v) {
        int i = v.getId();
        if (i == R.id.main_back) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }

        }
    }

    @Override
    public void onDestroy() {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);//将此对象的 accessible 标志设置为指示的布尔值。值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
            childFragmentManager.set(this, null);//将指定对象变量上此 Field 对象表示的字段设置为指定的新值。
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        mMessageHelper.unRegisterMessages();
        mMessageHelper.clearMessages();
        super.onDestroy();
    }

    /**
     * findViewById 省略强转过程
     *
     * @param resId
     * @param <V>具体的View类型
     * @return
     */
    protected <V> V findView(View rootView, @IdRes int resId) {
        return ViewUtil.findView(mRootView, resId);
    }

    /**
     * findViewById 并添加OnClick事件
     *
     * @param resId
     * @param <V>   具体的View类型
     * @return
     */
    protected <V> V findViewAttachOnclick(@IdRes int resId) {
        return ViewUtil.findViewAttachOnclick(mRootView, resId, this);
    }


    /**
     * 接口MessageCallback 的实现方法
     *
     * @param message
     */
    @Override
    public void onReceiveMessage(Message message) {

    }

    /**
     * findViewById 并添加OnClick事件
     *
     * @param resId
     * @param <V>   具体的View类型
     * @return
     */
    protected <V> V findViewAttachOnclickIcon(@IdRes int resId) {
        TextView tv = ViewUtil.findViewAttachOnclick(mRootView, resId, this);
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
    protected <V> V findViewIcon(@IdRes int resId) {
        TextView tv = ViewUtil.findViewAttachOnclick(mRootView, resId, this);
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
    protected <V> V findView(@IdRes int resId) {
        return ViewUtil.findView(mRootView, resId);
    }

}
