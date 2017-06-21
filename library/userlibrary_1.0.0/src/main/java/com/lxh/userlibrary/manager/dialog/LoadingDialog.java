package com.lxh.userlibrary.manager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.lxh.userlibrary.R;


public class LoadingDialog extends Dialog {

    /**
     * 检查更新中
     */
    private TextView tvContent;
    private String content;

    public LoadingDialog(Context context, String showText) {
        super(context, R.style.CheckVersionDialog);
        setContentView(R.layout.dialog_checking_update);
        this.setCanceledOnTouchOutside(false);
        initView(showText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
    }

    private void initView(String text){
        tvContent = (TextView) findViewById(R.id.dialog_umeng_content);
        tvContent.setText(text);
    }

    public void setContent(String content){
        if (!TextUtils.isEmpty(content)) {
            this.content = content;
        }
    }
}
