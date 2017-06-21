package com.gz.hkjs.app.ui.main.activity;

import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.ui.main.adapter.CollectFragmentAdapter;
import com.gz.hkjs.app.ui.main.fragment.collect.CollectFindFragment;
import com.gz.hkjs.app.ui.main.fragment.collect.CollectRecipeFragment;
import com.gz.hkjs.app.ui.main.model.CollectListModel;
import com.gz.hkjs.app.ui.main.presenter.CollectListPresenter;
import com.jaydenxiao.common.base.BaseActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收藏lwy
 */
public class CollectActivity extends BaseActivity<CollectListPresenter, CollectListModel> {

    private List<Fragment> collectFragmentList = new ArrayList<>();
    private CollectFindFragment collectFindFragment;
    private CollectRecipeFragment collectRecipeFragment;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    public int getLayoutId() {
        return R.layout.mine_collect;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        collectFindFragment = new CollectFindFragment();
        collectRecipeFragment = new CollectRecipeFragment();

        collectFragmentList.add(collectRecipeFragment);
        collectFragmentList.add(collectFindFragment);


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        CollectFragmentAdapter collectFragmentAdapter = new CollectFragmentAdapter(getSupportFragmentManager(),
                this, collectFragmentList);

        viewPager.setAdapter(collectFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @OnClick({R.id.iv_collect_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_collect_back:
                finish();
                break;
        }
    }


    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabLayout, 50, 50);
            }
        });
    }
}
