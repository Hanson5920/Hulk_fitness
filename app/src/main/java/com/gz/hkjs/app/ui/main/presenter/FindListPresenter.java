package com.gz.hkjs.app.ui.main.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.app.AppConstant;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.ui.main.contract.FindListContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/3/16.
 */

public class FindListPresenter extends FindListContract.Presenter {
    @Override
    public void getFindListDataRequest(HashMap<String, String> map) {
        mRxManage.add(mModel.getFindListData(map).subscribe(new RxSubscriber<List<FindSummary.DataBean>>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(List<FindSummary.DataBean> newsSummaries) {
                mView.returnFindData(newsSummaries);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
