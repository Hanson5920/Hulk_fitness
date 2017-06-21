package com.gz.hkjs.app.util.popwindow;


import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.widget.Photo_State;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 拍照窗体
 * cq
 * ShowPopupPhoto
 */
public class ShowPopupPhoto extends BasePopupWindow {

    public static final int statePicture = 0;
    public static final int statePhoto = 1;
//    public static final int stateVideo = 2;
//    public static final int stateOther = 3;

    //UI
    @BindView(R.id.tv_albums)
    TextView tvAlbums;
    @BindView(R.id.tv_photograph)
    TextView tvPhotograph;
    @BindView(R.id.cancel)
    TextView cancel;
    //Data
    private Photo_State photo_state;
    private int state = 0;//需要使用的状态 默认0 photo  1更换图片


    public ShowPopupPhoto(Activity activity) {
        super(activity);
    }

    /**
     * 设置窗体
     *
     * @param bgAlpha
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        this.activity.getWindow().setAttributes(lp);
    }


    @Override
    protected int setContentViewLayoutId() {
        return R.layout.photo_graph;
    }


    @Override
    protected void initPopupWindow(Activity activity, View contentView) {
        backgroundAlpha(0.3f);
        setAnimationStyle(android.R.style.Animation_InputMethod);
        ButterKnife.bind(this, contentView);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public ShowPopupPhoto showPopupWindow(View parent) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        return this;
    }


    public ShowPopupPhoto setConfig(int states, Photo_State photoState) {
        this.state = states;
        this.photo_state = photoState;
        if (state == statePicture) {//拍照
            tvAlbums.setText("从相册选择");
            tvPhotograph.setText("拍照");
        } else if (state == statePhoto) {//图片
            tvAlbums.setText("更换图片");
            tvPhotograph.setText("删除图片");
        }
        return this;
    }


    @OnClick({R.id.tv_albums, R.id.tv_photograph, R.id.cancel})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_albums:
                photo_state.first_item();
                dismiss();
                break;
            case R.id.tv_photograph:
                photo_state.second_item();
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }
}