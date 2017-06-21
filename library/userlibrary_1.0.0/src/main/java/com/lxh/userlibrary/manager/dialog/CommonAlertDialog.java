package com.lxh.userlibrary.manager.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxh.userlibrary.R;
import com.lxh.userlibrary.utils.ActivityUtils;
import com.lxh.userlibrary.utils.DisplayUtil;


/**
 * 常规通用对话框
 * Author:Eric_chan
 */

public class CommonAlertDialog extends AlertDialog implements View.OnClickListener{

    /* 视图填充 */
    private LayoutInflater mInflater;
    private View mDialogView;
    private Context mContext;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvUse;
    private TextView mTvCancel;
    private LinearLayout mLlParent;
    private LinearLayout mLlCheckParent;
    private CheckBox mCheckBox;
    private TextView mTvCheckBoxText;
    private ImageView mIvCheckUpdate;
    private DialogType mType;
    private String mTitle;
    private Object mContent;
    private String mLeftText;
    private String mRightText;
    private String mCheckboxText;
    private View.OnClickListener mOnClickListener;

    public CommonAlertDialog(Context context, DialogType type) {
        super(context, R.style.MyDialogStyle);
        this.mContext = context;
        this.mType = type;
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
        mDialogView = mInflater.inflate(R.layout.common_alert_dialog, null);
        setContentView(mDialogView);
        this.setCanceledOnTouchOutside(false);
        initView();
        initEvent();
        switch (mType) {
            case DELETE_DOWNTASK:
                mLlCheckParent.setVisibility(View.VISIBLE);
                break;
            case WIFI_CHANGED:
                mTvUse.setBackgroundResource(R.drawable.common_item_btn_green_bg);
                mTvUse.setTextColor(mContext.getResources().getColor(R.color.theme_assist));
                break;
            case CHECK_UPDATE_START:
                mIvCheckUpdate.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        init();
    }

    private void initEvent() {
        mTvUse.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        mLlCheckParent.setOnClickListener(this);
    }

    private void initView() {
        mTvTitle = (TextView) mDialogView.findViewById(R.id.tvDialogTitle);
        mTvContent = (TextView) mDialogView.findViewById(R.id.tvDialogContent);
        mTvUse = (TextView) mDialogView.findViewById(R.id.tvDialogSure);
        mTvCancel = (TextView) mDialogView.findViewById(R.id.tvDialogCancel);
        mLlParent = (LinearLayout) mDialogView.findViewById(R.id.llDialogCommonParent);
        mLlCheckParent = (LinearLayout) mDialogView.findViewById(R.id.llDialogCheckBoxParent);
        mCheckBox = (CheckBox) mDialogView.findViewById(R.id.cbDialogCheckBox);
        mTvCheckBoxText = (TextView) mDialogView.findViewById(R.id.tvDialogCheckBoxText);
        mIvCheckUpdate = (ImageView) mDialogView.findViewById(R.id.ivDialogImage);
    }

    /**
     * @param title 标题
     * @param content 内容
     * @param leftText 左侧按钮文字
     * @param rightText 右侧按钮文字
     * @param checkboxText 单选框后面的文字
     */
    public void setData(String title, Object content, String leftText, String rightText,String checkboxText) {
        this.mTitle = title;
        this.mContent = content;
        this.mLeftText = leftText;
        this.mRightText = rightText;
        this.mCheckboxText = checkboxText;
    }

    private void init() {
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText(mTitle);
        }
        if (mContent != null && !"".equals(mContent.toString().trim())) {
            if (mContent instanceof Spanned) {
                mTvContent.setText((Spanned) mContent);
            } else {
                mTvContent.setText((String) mContent);
            }
        }
        if (!TextUtils.isEmpty(mLeftText)) {
            mTvCancel.setText(mLeftText);
        }
        if (!TextUtils.isEmpty(mRightText)) {
            mTvUse.setText(mRightText);
        }
        if (!TextUtils.isEmpty(mCheckboxText)) {
            mTvCheckBoxText.setText(mCheckboxText);
        }
        measureViewWidth(mTvContent);
    }

    private void measureViewWidth(final View view) {//超过一行，居左，不到一行，水平居中
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int useableWidth = DisplayUtil.dip2px(mContext, 268);//一行的宽度
                int textLength = mTvContent.getMeasuredWidth();
                if (textLength < useableWidth) {//如果小于一行，就水平居中
                    mLlParent.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                view.requestLayout();
                return true;
            }

        });
    }

    /**
     * @return 单选框是否选中
     */
    public boolean isCheckBoxChecked(){
        //不可见，返回false
        return !(mCheckBox == null || mCheckBox.getVisibility() != View.VISIBLE) && mCheckBox.isChecked();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvDialogSure) {
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
            dismiss();

        } else if (i == R.id.tvDialogCancel) {
            switch (mType) {
                case EXIT_APP:// 还有下载任务时，退出程序提示框，后台下载
                    ActivityUtils.getScreenManager().popAllActivity();
                    break;
                default:
                    break;
            }
            dismiss();

        } else if (i == R.id.llDialogCheckBoxParent) {
            if (mCheckBox == null) {
                return;
            }
            mCheckBox.setChecked(!mCheckBox.isChecked());

        }
    }

    public enum DialogType {
        NORMAL_DIALOG,//通用类型的dialog
        DELETE_DOWNTASK, // 删除下载任务
        WIFI_CHANGED,//下载过程中，WIFI切换到普通网络
        CHECK_UPDATE_START, // 启动app时，检查更新
        EXIT_APP // 退出应用
    }
}