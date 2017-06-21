package com.gz.hkjs.app.ui.main.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.gz.hkjs.app.ui.main.contract.CollectListContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lwy on 2017/4/24.
 */

public class CollectListPresenter extends CollectListContract.Presenter {
    @Override
    public void getCollectRecipesListDataRequest(HashMap<String, String> map) {
        mRxManage.add(mModel.getCollectRecipesListData(map).subscribe(new RxSubscriber<List<RecipesSummary.DataBean>>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(List<RecipesSummary.DataBean> newsSummaries) {
                mView.returnCollectRecipesSummaryListData(newsSummaries);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void getCollectFindListDataRequest(HashMap<String, String> map) {
         mRxManage.add(mModel.getCollectFindListData(map).subscribe(new RxSubscriber<List<FindSummary.DataBean>>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(List<FindSummary.DataBean> newsSummaries) {
                mView.returnCollectFindSummaryListData(newsSummaries);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
