package com.lxh.userlibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lxh.userlibrary.R;
import com.lxh.userlibrary.base.BaseActivity;
import com.lxh.userlibrary.base.BaseFragment;
import com.lxh.userlibrary.base.FragmentFactory;
import com.lxh.userlibrary.manager.PageSwitcher;


/**
 * Created by Administrator on 2016/5/25.
 */
public class SubActivity extends BaseActivity {

    private final static int LAYOUT_CONTAINER = android.R.id.content;
    private FragmentFactory mFragmentFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    public FragmentFactory getFragmentFactory(){
        if (mFragmentFactory == null) {
            mFragmentFactory = new FragmentFactory();
        }
        return mFragmentFactory;
    }

    public Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(LAYOUT_CONTAINER);
    }

    @Override
    public void onBackPressed() {
        Fragment baseFragment = getCurrentFragment();
        if (baseFragment instanceof BaseFragment) {
            if (((BaseFragment) baseFragment).goBack()) {
                return;
            }
        }
        /* 解决fragment addToBackStack后，按返回键出现空白的Activity问题 */
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            try {
                super.onBackPressed();
            } catch (Exception e) {
//                XUtilLog.log_i("wbb","SubActivity exception"+e);
            }
        }
    }
    public void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        setIntent(intent);

        int type = intent.getIntExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_TYPE, 0);//intent_extra_fragment_type
        Bundle args = intent.getBundleExtra(PageSwitcher.INTENT_EXTRA_FRAGMENT_ARGS);
        boolean useCache = false;//是否使用缓存
        boolean useAnim = false;//是否使用动画

        if (args != null) {
            useCache = args.getBoolean(PageSwitcher.BUNDLE_FRAGMENT_CACHE);
            useAnim = args.getBoolean(PageSwitcher.BUNDLE_FRAGMENT_ANIM);
        } else {
            args = new Bundle();
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                args.putAll(intent.getExtras());
            }
        }

        if (!useAnim) {//不使用动画
            pushFragment(type, args, LAYOUT_CONTAINER, useCache, false);

        } else {//使用动画
            pushFragment(type, args, LAYOUT_CONTAINER, useCache, true);
        }
    }

    /**
     * 将fragment加到backStack
     * @param type      Fragment对应的clazz
     * @param args      Fragment参数
     * @param container 放置Fragment的View节点
     * @param useCache  是否缓存，如果缓存则使用完需要调用removeFragment清除缓存
     * @param useAnim   是否播放切换动画
     */
    protected void pushFragment(int type, Bundle args, int container, boolean useCache, boolean useAnim) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fT = fragmentManager.beginTransaction();

        if (useAnim) {
            fT.setCustomAnimations(R.anim.open_slide_in, R.anim.open_slide_out, R.anim.close_slide_in, R.anim.close_slide_out);
        }

        Fragment fragment = null;
        if (useCache) {
            fragment = fragmentManager.findFragmentByTag(String.valueOf(type));
            if (fragment == null) {
                fragment = (Fragment) getFragmentFactory().getFragment(type, true);
            }

        } else {
            removeFragment(type);
            fragment = getFragmentFactory().getFragment(type, false);
        }

        if (fragment == fragmentManager.findFragmentById(container)) {
            return;
        }

        if (fragment != null) {
            if (args != null) {
                ((BaseFragment) fragment).setArguments(args);

            }

            fT.replace(container, fragment, String.valueOf(type));
            fT.addToBackStack(String.valueOf(type));
        }

        fT.commitAllowingStateLoss();
    }

    /**
     * 从缓存中移除Fragment
     * @param type
     */
    public void removeFragment(int type) {
        getFragmentFactory().removeFragment(type);
    }

    /**
     * 向当前Fragment分发onActivityResult事件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = getCurrentFragment();
        f.onActivityResult(requestCode, resultCode, data);
    }

}
