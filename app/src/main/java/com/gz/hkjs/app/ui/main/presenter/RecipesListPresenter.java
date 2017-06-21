package com.gz.hkjs.app.ui.main.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.gz.hkjs.app.ui.main.contract.RecipesListContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class RecipesListPresenter extends RecipesListContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void getRecipesListDataRequest(HashMap<String, String> map) {
        mRxManage.add(
                mModel.getRecipesListData(map).subscribe(new RxSubscriber<List<RecipesSummary.DataBean>>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(List<RecipesSummary.DataBean> photoGirls) {
                mView.returnRecipesListData(photoGirls);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void getRecipesTabDataRequest(HashMap<String, String> map) {
        mRxManage.add(mModel.getRecipesTabData(map).subscribe(
                new RxSubscriber<List<RecipesSummary.DataBean>>(mContext, false) {
                    @Override
                    protected void _onNext(List<RecipesSummary.DataBean> dataBeen) {
                        mView.returnRecipesTabData(dataBeen);
                        mView.stopLoading();
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showErrorTip(message);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        mView.showLoading(mContext.getString(R.string.loading));
                    }
                }
        ));
    }
}
