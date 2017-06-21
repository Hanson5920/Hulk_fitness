package com.gz.hkjs.app.ui.main.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.gz.hkjs.app.parameter.JMClassUser;
import com.gz.hkjs.app.ui.main.contract.RecipesListContract;
import com.gz.hkjs.app.ui.main.model.RecipesListModel;
import com.gz.hkjs.app.ui.main.presenter.RecipesListPresenter;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 食谱
 * Created by Administrator on 2017/3/15.
 */

public class RecipesMainFragment extends BaseFragment<RecipesListPresenter, RecipesListModel> implements RecipesListContract.View, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.ntb)
    NormalTitleBar ntb;
    //    @BindView(R.id.irc_recipes)
//    IRecyclerView ircRecipes;
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;
    @BindView(R.id.recipse_tab)
    TabLayout mTabLayout;
    @BindView(R.id.recipse_viewpager)
    ViewPager mViewPager;

    private FragmentAdapter fragmentAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recipes;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        ntb.setTvLeftVisiable(false);
        ntb.setTitleText(getString(R.string.recipes));

        mPresenter.getRecipesTabDataRequest(JMClassUser.MyJMClass());
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        private List<RecipesItemFragment> fragments;

        public FragmentAdapter(FragmentManager fm, List<RecipesItemFragment> fragmentList) {
            super(fm);
            fragments = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore(View loadMoreView) {

    }

    @Override
    public void showLoading(String title) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public void stopLoading() {
//        if (adapter.getSize() == 0) {
//            loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
//        } else {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
//        }
    }

    @Override
    public void showErrorTip(String msg) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
    }

    @Override
    public void returnRecipesListData(List<RecipesSummary.DataBean> recipesSummaries) {

    }

    @Override
    public void returnRecipesTabData(List<RecipesSummary.DataBean> dataBeen) {
        List<RecipesItemFragment> fragment_s = new ArrayList<>();
        if(dataBeen!=null){
            for (RecipesSummary.DataBean db : dataBeen){
                RecipesItemFragment fragment = new RecipesItemFragment();
                fragment.setTagId(db.getId());
                fragment_s.add(fragment);
            }
        }
        fragmentAdapter = new FragmentAdapter(getFragmentManager(), fragment_s);
        mViewPager.setAdapter(fragmentAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < fragmentAdapter.getCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(R.layout.recipes_tab_item_layout);

            TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tab_text);
            View unselect = tab.getCustomView().findViewById(R.id.tab_unselect);
            ImageView root = (ImageView) tab.getCustomView().findViewById(R.id.tab_root);
            if (i == 0) {
                unselect.setVisibility(View.GONE);
                tv.setTextColor(getResources().getColor(R.color.white));
            }

            Glide.with(mActivity).load(dataBeen.get(i).getLogo_url())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(com.jaydenxiao.common.R.drawable.ic_empty_picture).centerCrop()
                    .into(root);
            tv.setText(dataBeen.get(i).getName());
        }

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tab_text);
                View unselect = tab.getCustomView().findViewById(R.id.tab_unselect);
                unselect.setVisibility(View.GONE);
                tv.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tv = (TextView) tab.getCustomView().findViewById(R.id.tab_text);
                View unselect = tab.getCustomView().findViewById(R.id.tab_unselect);
                unselect.setVisibility(View.VISIBLE);
                tv.setTextColor(getResources().getColor(R.color.text_color));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
