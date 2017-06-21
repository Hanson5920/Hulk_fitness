package com.gz.hkjs.app.util.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public abstract class BasePopupWindow extends PopupWindow implements View.OnClickListener {
    public Activity activity;

    public BasePopupWindow(Activity activity) {
        this(activity, null);
    }

    public BasePopupWindow(Activity activity, AttributeSet attrs) {
        this(activity, attrs, 0);
    }


    public BasePopupWindow(Activity activity, AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        this.activity = activity;
        setBasePopupWindow(activity);
    }


    private void setBasePopupWindow(Activity activity) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(setContentViewLayoutId(), null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        initPopupWindow(activity, contentView);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }


    /**
     * 设置布局文件
     *
     * @return
     */

    protected abstract
    @LayoutRes
    int setContentViewLayoutId();


    /**
     * 初始化 PopupWindow  布局
     */
    protected abstract void initPopupWindow(Activity activity, View contentView);


}