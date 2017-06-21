package com.gz.hkjs.app.ui.main.fragment.collect;


import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.gz.hkjs.app.parameter.JMClass;
import com.gz.hkjs.app.ui.detail.activity.FindDetailActivity;
import com.gz.hkjs.app.ui.main.adapter.FindListAdapter;
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
public class CollectFindFragment extends BaseFragment<CollectListPresenter, CollectListModel>
        implements CollectListContract.View, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.irc_find)
    IRecyclerView ircFind;

    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;

    private FindListAdapter findListAdapter;
    private List<FindSummary.DataBean> datas = new ArrayList<>();

    private int mStartPage = 1;


    @Override
    protected int getLayoutResource() {
        return R.layout.collect_find;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        ircFind.setLayoutManager(new LinearLayoutManager(getContext()));
        datas.clear();
        findListAdapter = new FindListAdapter(getContext(), datas);
//        findListAdapter.openLoadAnimation(new ScaleInAnimation());
        ircFind.setAdapter(findListAdapter);
        ircFind.setOnRefreshListener(this);
        ircFind.setOnLoadMoreListener(this);

        mPresenter.getCollectFindListDataRequest(JMClass.collectionLists("2", String.valueOf(mStartPage)));
        mRxManager.on(FindDetailActivity.TAG, new Action1<String>() {
            @Override
            public void call(String postId) {
                List<FindSummary.DataBean> list = findListAdapter.getAll();
                for (FindSummary.DataBean bean : list) {
                    if (bean.getId().equals(postId)) {
                        findListAdapter.remove(bean);
                        break;
                    }
                }
                stopLoading();
            }
        });
    }

    @Override
    public void onRefresh() {
        findListAdapter.getPageBean().setRefresh(true);
        mStartPage = 1;
        //发起请求
        ircFind.setRefreshing(true);
        mPresenter.getCollectFindListDataRequest(JMClass.collectionLists("2", String.valueOf(mStartPage)));
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        findListAdapter.getPageBean().setRefresh(false);
        //发起请求
        ircFind.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getCollectFindListDataRequest(JMClass.collectionLists("2", String.valueOf(mStartPage)));
    }

    @Override
    public void showLoading(String title) {
        if (findListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    @Override
    public void stopLoading() {
        if (findListAdapter.getSize() == 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        } else {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        }
    }

    @Override
    public void showErrorTip(String msg) {
        if (findListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
            loadedTip.setTips(msg);
            ircFind.setRefreshing(false);
        } else {
            ircFind.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    @Override
    public void returnCollectRecipesSummaryListData(List<RecipesSummary.DataBean> recipesSummaries) {
        // 发现模块
    }

    @Override
    public void returnCollectFindSummaryListData(List<FindSummary.DataBean> findSummaries) {
        if (findSummaries != null) {
            mStartPage += 1;
            if (findListAdapter.getPageBean().isRefresh()) {
                ircFind.setRefreshing(false);
                findListAdapter.replaceAll(findSummaries);
            } else {
                if (findSummaries.size() > 0) {
                    ircFind.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    findListAdapter.addAll(findSummaries);
                } else {
                    ircFind.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }
}
