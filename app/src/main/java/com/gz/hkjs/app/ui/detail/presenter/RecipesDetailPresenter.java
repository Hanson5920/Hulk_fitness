package com.gz.hkjs.app.ui.detail.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.CollectAddData;
import com.gz.hkjs.app.bean.CollectDeleteData;
import com.gz.hkjs.app.bean.RecipesDetail;
import com.gz.hkjs.app.ui.detail.contract.RecipesDetailContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.ToastUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/16.
 */

public class RecipesDetailPresenter extends RecipesDetailContract.Presenter {
    @Override
    public void getRecipesDetailDataRequest(HashMap<String, String> map) {
        mRxManage.add(
                mModel.getRecipesDetailData(map).subscribe(
                        new RxSubscriber<RecipesDetail.DataBean>(mContext) {
                            @Override
                            protected void _onNext(RecipesDetail.DataBean recipesDetail) {
                                mView.returnRecipesDetailData(recipesDetail);
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
