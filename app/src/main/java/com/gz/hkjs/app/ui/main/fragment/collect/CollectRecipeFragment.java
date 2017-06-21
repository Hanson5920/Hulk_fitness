package com.gz.hkjs.app.ui.main.fragment.collect;


import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.gz.hkjs.app.parameter.JMClass;
import com.gz.hkjs.app.ui.detail.activity.RecipesDetailActivity;
import com.gz.hkjs.app.ui.main.contract.CollectListContract;
import com.gz.hkjs.app.ui.main.model.CollectListModel;
import com.gz.hkjs.app.ui.main.presenter.CollectListPresenter;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by lwy on 2017/4/25.
 */

public class CollectRecipeFragment extends BaseFragment<CollectListPresenter, CollectListModel>
        implements CollectListContract.View, OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.irc_find)
    IRecyclerView ircRecipes;
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;

    private int mStartPage = 1;

    private CommonRecycleViewAdapter<RecipesSummary.DataBean> adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.collect_recipes;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        adapter = new CommonRecycleViewAdapter<RecipesSummary.DataBean>(getContext(), R.layout.item_recipes) {
            @Override
            public void convert(ViewHolderHelper helper, final RecipesSummary.DataBean recipes) {
                final ImageView imageView = helper.getView(R.id.id_item_recips_img);
                RelativeLayout rootrl = helper.getView(R.id.rl_root);

                Glide.with(mContext).load(recipes.getLogo_url())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .placeholder(com.jaydenxiao.common.R.drawable.ic_image_loading)
                        .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                        .centerCrop().override(1090, 1090 * 3 / 4)
                        .crossFade().into(imageView);

                TextView title = helper.getView(R.id.id_item_recips_title);
                title.setText(recipes.getName());
                TextView energy = helper.getView(R.id.id_item_recips_energy);
                energy.setText(recipes.getEnergy() + "千卡");
                TextView tag1 = helper.getView(R.id.id_item_recips_foodtag1);
                TextView tag2 = helper.getView(R.id.id_item_recips_foodtag2);
                List<RecipesSummary.DataBean.FoodTagsBean> tags = recipes.getFood_tags();

                if (tags.size() > 1) {
                    tag1.setText(tags.get(0).getName());
                    tag2.setText(tags.get(1).getName());
                } else {
                    tag1.setText(tags.get(0).getName());
                    tag2.setVisibility(View.INVISIBLE);
                }

                TextView dsc = helper.getView(R.id.id_item_recips_dsc);
                dsc.setText(recipes.getDsc());

                rootrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RecipesDetailActivity.startAction(mContext, imageView, recipes.getId(), recipes.getLogo_url(), (ArrayList<RecipesSummary.DataBean.FoodTagsBean>) recipes.getFood_tags());
                    }
                });
            }
        };

        ircRecipes.setAdapter(adapter);
        ircRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        ircRecipes.setOnLoadMoreListener(this);
        ircRecipes.setOnRefreshListener(this);

        mPresenter.getCollectRecipesListDataRequest(JMClass.collectionLists("1", String.valueOf(mStartPage)));

        mRxManager.on(RecipesDetailActivity.TAG, new Action1<String>() {
            @Override
            public void call(String postId) {
                List<RecipesSummary.DataBean> list = adapter.getAll();
                for (RecipesSummary.DataBean bean : list) {
                    if (bean.getId().equals(postId)) {
                        adapter.remove(bean);
                        break;
                    }
                }
                stopLoading();
            }
        });
    }

    @Override
    public void onRefresh() {
        adapter.getPageBean().setRefresh(true);
        //发起请求
        mStartPage = 1;
        ircRecipes.setRefreshing(true);
        mPresenter.getCollectRecipesListDataRequest(JMClass.collectionLists("1", String.valueOf(mStartPage)));
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        adapter.getPageBean().setRefresh(false);
        //发起请求
        ircRecipes.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getCollectRecipesListDataRequest(JMClass.collectionLists("1", String.valueOf(mStartPage)));
    }

    @Override
    public void showLoading(String title) {
        if (adapter.getPageBean().isRefresh())
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public void stopLoading() {
        if (adapter.getSize() == 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        } else {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        }
    }

    @Override
    public void showErrorTip(String msg) {
        if (adapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
            loadedTip.setTips(msg);
            ircRecipes.setRefreshing(false);
        } else {
            ircRecipes.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }


    @Override
    public void returnCollectRecipesSummaryListData(List<RecipesSummary.DataBean> recipesSummaries) {
        if (recipesSummaries != null) {
            mStartPage += 1;
            if (adapter.getPageBean().isRefresh()) {
                ircRecipes.setRefreshing(false);
                adapter.replaceAll(recipesSummaries);
            } else {
                if (recipesSummaries.size() > 0) {
                    ircRecipes.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    adapter.addAll(recipesSummaries);
                } else {
                    ircRecipes.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }

    @Override
    public void returnCollectFindSummaryListData(List<FindSummary.DataBean> collectListData) {
        // Find
    }
}
