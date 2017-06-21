package com.lxh.userlibrary.manager.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lxh.userlibrary.R;
import com.lxh.userlibrary.utils.Photo_State;

/**
 * 拍照窗体
 * cq
 */
public class ShowPopupPhoto {
    private PopupWindow popWindow;
    private TextView photograph, albums;
    private TextView cancel;
    private LayoutInflater layoutInflater;
    int state = 0;//需要使用的状态 默认0 photo  1更换图片 2更换视频
    Activity activity;

    @SuppressWarnings("deprecation")
    public void showPopupWindow(Activity activity, View parent, Photo_State photo_state, int state) {
        this.state = state;
        this.activity = activity;
        backgroundAlpha(0.3f);

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (popWindow == null) {
            View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            initPop(view, photo_state);
        }
        popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
//        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public void initPop(View view, final Photo_State photo_state) {
        photograph = (TextView) view.findViewById(R.id.photograph);//拍照
        albums = (TextView) view.findViewById(R.id.albums);//相册
        cancel = (TextView) view.findViewById(R.id.cancel);//取消

        if (state == 1) {//图片
            albums.setText("更换图片");
            photograph.setText("删除图片");
        } else if (state == 2) {//视频
            albums.setText("更换视频");
            photograph.setText("删除视频");
        }
        if (state == 3) {
            albums.setText("视频");
            photograph.setText("图片");
        }

        photograph.setOnClickListener(new View.OnClickListener() {//拍照
            @Override
            public void onClick(View arg0) {
                photo_state.second_item();
                popWindow.dismiss();
            }
        });
        albums.setOnClickListener(new View.OnClickListener() {//相册
            @Override
            public void onClick(View arg0) {
                photo_state.first_item();
                popWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                backgroundAlpha(1f);
                popWindow.dismiss();
            }
        });
    }


    /**
     * 设置窗体
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        this.activity.getWindow().setAttributes(lp);
    }

}
