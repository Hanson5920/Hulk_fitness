package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.app.AppConstant;
import com.gz.hkjs.app.bean.TabEntity;
import com.gz.hkjs.app.ui.main.fragment.FindMainFragment;
import com.gz.hkjs.app.ui.main.fragment.MineMainFragment;
import com.gz.hkjs.app.ui.main.fragment.RecipesMainFragment;
import com.gz.hkjs.app.ui.main.fragment.TrainingMainFragment;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baseapp.AppManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import rx.functions.Action1;


public class MainActivity extends BaseActivity {


    private static final String TAG = "MainActivity";
//    private long exitTime = 0;

    @BindView(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private static Boolean isExit = false;
    private String[] mTitles = {"训练", "食谱", "发现", "我"};
    private int[] mIconUnselectIds = {
            R.mipmap.s_xunlian, R.mipmap.s_shipu, R.mipmap.s_faxian, R.mipmap.s_me};
    private int[] mIconSelectIds = {
            R.mipmap.xunlian, R.mipmap.shipu, R.mipmap.faxian, R.mipmap.me};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private TrainingMainFragment trainingMainFragment;
    private RecipesMainFragment recipesMainFragment;
    private FindMainFragment findMainFragment;
    private MineMainFragment mineMainFragment;

    public static final String NO_LOGIN = "no_login";

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        //初始化菜单
        initTab();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(savedInstanceState);
        tabLayout.measure(0, 0);
    }


    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            trainingMainFragment = (TrainingMainFragment) getSupportFragmentManager().findFragmentByTag("trainingMainFragment");
            recipesMainFragment = (RecipesMainFragment) getSupportFragmentManager().findFragmentByTag("recipesMainFragment");
            findMainFragment = (FindMainFragment) getSupportFragmentManager().findFragmentByTag("findMainFragment");
            mineMainFragment = (MineMainFragment) getSupportFragmentManager().findFragmentByTag("mineMainFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            trainingMainFragment = new TrainingMainFragment();
            recipesMainFragment = new RecipesMainFragment();
            findMainFragment = new FindMainFragment();
            mineMainFragment = new MineMainFragment();

            transaction.add(R.id.fl_body, trainingMainFragment, "trainingMainFragment");
            transaction.add(R.id.fl_body, recipesMainFragment, "recipesMainFragment");
            transaction.add(R.id.fl_body, findMainFragment, "findMainFragment");
            transaction.add(R.id.fl_body, mineMainFragment, "mineMainFragment");
        }

        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);

        mRxManager.on(NO_LOGIN, new Action1<String>() {
            @Override
            public void call(String s) {
                SwitchTo(0);
                tabLayout.setCurrentTab(0);
                AppManager.getAppManager().returnToActivity(MainActivity.class);
            }
        });
    }

    private void initTab() {

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //训练
            case 0:
                transaction.hide(recipesMainFragment);
                transaction.hide(findMainFragment);
                transaction.hide(mineMainFragment);
                transaction.show(trainingMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //食谱
            case 1:
                transaction.hide(trainingMainFragment);
                transaction.hide(findMainFragment);
                transaction.hide(mineMainFragment);
                transaction.show(recipesMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //发现
            case 2:
                transaction.hide(mineMainFragment);
                transaction.hide(trainingMainFragment);
                transaction.hide(recipesMainFragment);
                transaction.show(findMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //我
            case 3:
                transaction.hide(trainingMainFragment);
                transaction.hide(recipesMainFragment);
                transaction.hide(findMainFragment);
                transaction.show(mineMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }


    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
            exitBy2Click(); //调用双击退出函数
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击退出
     */
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            com.lxh.userlibrary.utils.ToastUtil.toastLong(this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        // LogUtils.loge("onSaveInstanceState进来了1");
        if (tabLayout != null) {
            //LogUtils.loge("onSaveInstanceState进来了2");
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }

}
