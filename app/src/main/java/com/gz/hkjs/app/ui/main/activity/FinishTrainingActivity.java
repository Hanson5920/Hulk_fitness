package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.api.ApiConstants;
import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.gz.hkjs.app.parameter.JMClassDetail;
import com.gz.hkjs.app.ui.detail.contract.FinishTrainingContract;
import com.gz.hkjs.app.ui.detail.model.FinishTrainModel;
import com.gz.hkjs.app.ui.detail.presenter.FinishTrainPresenter;
import com.gz.hkjs.app.ui.main.fragment.TrainingMainFragment;
import com.gz.hkjs.app.util.CircleImageView;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.lxh.userlibrary.UserCenter;

import butterknife.BindView;

/**
 * Company: chuangxinmengxiang-chongqingwapushidai
 * User: HongBin(Hongbin1011@gmail.com)
 * Date: 2017-04-21
 * Time: 10:54
 */

public class FinishTrainingActivity extends BaseActivity<FinishTrainPresenter, FinishTrainModel> implements FinishTrainingContract.View{

    private PopupWindow popupWindow;
    public static final String TAG = "FinishTrainingActivity";

    @BindView(R.id.id_finishtrain_bt)
    Button idFinishtrainBt;

    @BindView(R.id.difficulty)
    TextView tvDifficulty;

    @BindView(R.id.duration)
    TextView tvDuration;

    @BindView(R.id.energy)
    TextView tvEnergy;

    @BindView(R.id.id_finishtrain_tv2)
    TextView tvName;

    @BindView(R.id.user_logo)
    CircleImageView imgUserLogo;

    private static final String INTENT_STRING = "trainingVideo";

    private TrainVideoDetail.DataBean mTrainVideoDetail;
    private String score;

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity, TrainVideoDetail.DataBean returnDataBean) {
        Intent intent = new Intent(activity, FinishTrainingActivity.class);
        intent.putExtra(INTENT_STRING, returnDataBean);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_finish_train;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        idFinishtrainBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPopupWindow();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.5f;
                getWindow().setAttributes(lp);
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
        });

        mTrainVideoDetail = getIntent().getParcelableExtra(INTENT_STRING);
        if(mTrainVideoDetail != null){
            tvDifficulty.setText(mTrainVideoDetail.getExaggerate_name());
            tvEnergy.setText(mTrainVideoDetail.getEnergy());
            int min = Integer.parseInt(mTrainVideoDetail.getWtime()) / 60;
            tvDuration.setText(String.valueOf(min));
            tvName.setText(mTrainVideoDetail.getName());
        }

        ImageLoaderUtils.display(this, imgUserLogo, UserCenter.getUserLogoUrl());
    }

    private String getScore(String str){
       if(TextUtils.equals(str, "轻松"))  return "1";
        else if(TextUtils.equals(str, "刚好"))  return "2";
        else if(TextUtils.equals(str, "略难"))  return "3";
        else if(TextUtils.equals(str, "好难"))  return "4";
        else return "";
    }

    /**
     * 获取PopipWinsow实例
     */
    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopupWindow();
        }
    }

    private void initPopupWindow() {
        //获取自定义布局文件activity_pop_bottom.xml 布局文件
        final View popupWindowBottom = getLayoutInflater().inflate(R.layout.activity_pop_bottom, null, false);
        //创建Popupwindow 实例，200，LayoutParams.MATCH_PARENT 分别是宽高
        popupWindow = new PopupWindow(popupWindowBottom, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        //设置动画效果
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        //点击其他地方消失
        popupWindowBottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindowBottom != null && popupWindowBottom.isShown()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
//        popupWindow.setBackgroundDrawable(new ColorDrawable(0x88000000));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        RadioGroup rg = (RadioGroup) popupWindowBottom.findViewById(R.id.finish_radiogroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton)popupWindowBottom.findViewById(group.getCheckedRadioButtonId());
                String str=radioButton.getText().toString();
                score = getScore(str);

                sendRecord();
            }
        });
    }

    private void sendRecord(){
        String energy = mTrainVideoDetail.getEnergy();
        String id = mTrainVideoDetail.getId();
        String name = mTrainVideoDetail.getName();

//        int min = Integer.parseInt(mTrainVideoDetail.getWtime()) / 60;
//        String times = min+"";
        mPresenter.finishTrainRequest(JMClassDetail.FinishTrainJMClass(energy, id, name, mTrainVideoDetail.getWtime(), score, ApiConstants.API_USER_HOME_ADDRECORD));
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void returnFinishTrain(String state) {
        if(TextUtils.equals(state,"1")){
            popupWindow.dismiss();
            ToastUtil.showToastWithImg(getResources().getString(R.string.record), R.drawable.ic_success);
            mRxManager.post(TrainingMainFragment.strLogin, "Login");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    }catch (Exception e){}
                    finish();
                }
            });
        }
    }
}
