package com.jaydenxiao.common.commonwidget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaydenxiao.common.R;
import com.jaydenxiao.common.commonutils.DisplayUtil;

/**
 * Created by lwy on 2017/4/17.
 */

public class MineTitleBar extends RelativeLayout {
    private ImageView ivTitleBack;
    private TextView tvTitleChange, tvTitleRight;
    private RelativeLayout rlCommonTitle;
    private Context context;

    public MineTitleBar(Context context) {
        super(context, null);
        this.context = context;
    }

    public MineTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View.inflate(context, R.layout.title_layout, this);
        tvTitleChange = (TextView) findViewById(R.id.tv_title_change);
        tvTitleRight = (TextView) findViewById(R.id.tv_title_right);
        ivTitleBack = (ImageView) findViewById(R.id.iv_title_back);
        rlCommonTitle = (RelativeLayout) findViewById(R.id.mine_common_title);
    }

    public void setHeaderHeight() {
        rlCommonTitle.setPadding(0, DisplayUtil.getStatusBarHeight(context), 0, 0);
        rlCommonTitle.requestLayout();
    }

    /**
     * 设置左侧返回是否显示
     *
     * @param visible
     */
    public void setLeftBack(boolean visible) {
        if (visible) {
            ivTitleBack.setVisibility(VISIBLE);
        } else {
            ivTitleBack.setVisibility(GONE);
        }
    }
    /**
     * 设置左侧返回是否显示
     *
     * @param visible
     */
    public void setRightcommit(boolean visible) {
        if (visible) {
            tvTitleRight.setVisibility(VISIBLE);
        } else {
            tvTitleRight.setVisibility(GONE);
        }
    }
    /**
     * 右标题是否显示
     */
    public void setRightTitleVisibility(boolean visible) {
        tvTitleRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    /**
     * 管理标题
     */
    public void setTitleVisibility(boolean visible) {
        if (visible) {
            ivTitleBack.setVisibility(View.VISIBLE);
        } else {
            ivTitleBack.setVisibility(View.GONE);
        }
    }

    public void setTitleText(String string) {
        tvTitleChange.setText(string);
    }
    public void setTitleText(int string) {
        tvTitleChange.setText(string);
    }

    public void setTitleColor(int color) {
        tvTitleChange.setTextColor(color);
    }


    public void setTtileRightText(int string) {
        tvTitleRight.setText(string);
    }
    public void setTtileRightText(String string) {
        tvTitleRight.setText(string);
    }



    /**
     * 获取右按钮
     *
     * @return
     */
    public View getRightImage() {
        return tvTitleRight;
    }


    public void setRightTitle(String text) {
        tvTitleRight.setText(text);
    }

    /*
     * 点击事件
     */
    public void setOnBackListener(OnClickListener listener) {
        ivTitleBack.setOnClickListener(listener);
    }

    public void setOnLeftImagListener(OnClickListener listener) {
        ivTitleBack.setOnClickListener(listener);
    }
    public void setOnRightImagListener(OnClickListener listener) {
        tvTitleRight.setOnClickListener(listener);
    }

    public void setOnRightTextListener(OnClickListener listener) {
        tvTitleRight.setOnClickListener(listener);
    }

    /**
     * 标题背景颜色
     *
     * @param color
     */
    public void setBackGroundColor(int color) {
        rlCommonTitle.setBackgroundColor(color);
    }

    public Drawable getBackGroundDrawable() {
        return rlCommonTitle.getBackground();
    }
}
