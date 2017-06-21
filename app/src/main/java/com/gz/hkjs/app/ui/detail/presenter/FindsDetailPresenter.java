package com.gz.hkjs.app.ui.detail.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.CollectAddData;
import com.gz.hkjs.app.bean.CollectDeleteData;
import com.gz.hkjs.app.bean.FindDetail;
import com.gz.hkjs.app.ui.detail.contract.FindDetailContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.ToastUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/21.
 */

public class FindsDetailPresenter extends FindDetailContract.Presenter {
    @Override
    public void getOneFindsDataRequest(HashMap<String, String> map) {
        mRxManage.add(
                mModel.getOneFindsData(map).subscribe(new RxSubscriber<FindDetail.DataBean>(mContext) {
                    @Override
                    protected void _onNext(FindDetail.DataBean newsDetail) {
                        mView.returnOneFindData(newsDetail);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtil.showToastWithImg(message, R.drawable.ic_wrong);
                    }
                }));
    }

    @Override
    public void collectionAdd(HashMap<String, String> map) {
        mRxManage.add(
                mModel.getCollectAddData(map).subscribe(
                        new RxSubscriber<CollectAddData.DataBean>(mContext) {
                            @Override
                            protected void _onNext(CollectAddData.DataBean dataBean) {
                                mView.returnCollectAddData(dataBean);
                            }

                            @Override
                            protected void _onError(String message) {
                                ToastUtil.showToastWithImg(message, R.drawable.ic_wrong);
                            }
                        }));
    }

    @Override
    public void collectionDelete(HashMap<String, String> map) {
        mRxManage.add(
                mModel.getCollectDeleteData(map).subscribe(
                        new RxSubscriber<CollectDeleteData.DataBean>(mContext) {
                            @Override
                            protected void _onNext(CollectDeleteData.DataBean dataBean) {
                                mView.returnDeleteAddData(dataBean);

                            }

                            @Override
                            protected void _onError(String message) {
                                ToastUtil.showToastWithImg(message, R.drawable.ic_wrong);
                            }
                        }));
    }
}
