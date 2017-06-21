package com.gz.hkjs.app.ui.main.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.HomesDelete;
import com.gz.hkjs.app.bean.UserHomeData;
import com.gz.hkjs.app.ui.main.contract.TrainingListContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.ToastUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/16.
 */

public class TrainingListPresenter extends TrainingListContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void getUserHomeDataRequest(HashMap<String, String> map) {
        mRxManage.add(
                mModel.getUserHomeDataListData(map).
                        subscribe(new RxSubscriber<UserHomeData.DataBean>(mContext, false) {
                            @Override
                            public void onStart() {
                                super.onStart();
                                mView.showLoading(mContext.getString(R.string.loading));
                            }

                            @Override
                            protected void _onNext(UserHomeData.DataBean videoDatas) {
                                mView.returnUserHomeDataListData(videoDatas);
                                mView.stopLoading();
                            }

                            @Override
                            protected void _onError(String message) {
                                mView.showErrorTip(message);
                                mView.stopLoading();
                             }

                        }));
    }

    @Override
    public void deleteHomesRequest(HashMap<String, String> map) {
        mRxManage.add(
                mModel.deleteHomesRequest(map).
                        subscribe(new RxSubscriber<HomesDelete.DataBean>(mContext, false) {

                            @Override
                            protected void _onNext(HomesDelete.DataBean videoDatas) {
                                mView.returnDeleteHomesRequestData(videoDatas);
                            }

                            @Override
                            protected void _onError(String message) {
                                mView.returnDeleteHomesRequestData(null);
                                ToastUtil.showToastWithImg(message, R.drawable.ic_wrong);
                            }
                        }));
    }
}
