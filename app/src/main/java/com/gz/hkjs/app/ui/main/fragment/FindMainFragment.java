package com.gz.hkjs.app.ui.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.parameter.JMClass;
import com.gz.hkjs.app.ui.main.adapter.FindListAdapter;
import com.gz.hkjs.app.ui.main.contract.FindListContract;
import com.gz.hkjs.app.ui.main.model.FindListModel;
import com.gz.hkjs.app.ui.main.presenter.FindListPresenter;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 发现
 * Created by Administrator on 2017/3/15.
 */

public class FindMainFragment extends BaseFragment<FindListPresenter, FindListModel> implements
        FindListContract.View, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.ntb)
    NormalTitleBar ntb;
    @BindView(R.id.irc_find)
    IRecyclerView ircFind;
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;

    private FindListAdapter findListAdapter;
    private List<FindSummary.DataBean> datas = new ArrayList<>();

    private int mStartPage = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_find;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {

        ntb.setTvLeftVisiable(false);
        ntb.setTitleText(getString(R.string.find));

        ircFind.setLayoutManager(new LinearLayoutManager(getContext()));
        datas.clear();
        findListAdapter = new FindListAdapter(getContext(), datas);

        ircFind.setAdapter(findListAdapter);
        ircFind.setOnRefreshListener(this);
        ircFind.setOnLoadMoreListener(this);

        //数据为空才重新发起请求
        if (findListAdapter.getSize() <= 0) {
            mStartPage = 1;
            mPresenter.getFindListDataRequest(JMClass.MyJMClass(String.valueOf(mStartPage)));
        }
    }

    @Override
    public void onRefresh() {
        findListAdapter.getPageBean().setRefresh(true);
        mStartPage = 1;
        //发起请求
        ircFind.setRefreshing(true);
        mPresenter.getFindListDataRequest(JMClass.MyJMClass(String.valueOf(mStartPage)));
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        findListAdapter.getPageBean().setRefresh(false);
        //发起请求
        ircFind.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getFindListDataRequest(JMClass.MyJMClass( String.valueOf(mStartPage)));
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
    public void returnFindData(List<FindSummary.DataBean> findSummaries) {
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
