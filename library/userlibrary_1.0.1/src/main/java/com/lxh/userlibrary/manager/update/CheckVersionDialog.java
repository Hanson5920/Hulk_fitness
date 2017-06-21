package com.lxh.userlibrary.manager.update;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.lxh.userlibrary.R;

/**
 * Created by hulei on 2016/6/6.
 * 检查Wifi弹窗
 */
public class CheckVersionDialog extends Dialog implements View.OnClickListener {

    private TextView tvDialogCancel;
    private TextView tvDialogSure;
    private TextView tvDialogContent;
    private String content;
    private CallBack callBack;

    public CheckVersionDialog(Context context, String content, CallBack callBack) {
        super(context, R.style.MyDialogStyle);
        this.content = content;
        this.callBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_version_dialog);
        tvDialogCancel = (TextView) findViewById(R.id.tvDialogCancel);
        tvDialogCancel.setOnClickListener(this);
        tvDialogSure = (TextView) findViewById(R.id.tvDialogSure);
        tvDialogSure.setOnClickListener(this);
        tvDialogContent = (TextView) findViewById(R.id.tvDialogContent);
        tvDialogContent.setText(content);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvDialogCancel) {
            dismiss();

        } else if (i == R.id.tvDialogSure) {
            callBack.onClick();
            dismiss();

        }
    }
}
