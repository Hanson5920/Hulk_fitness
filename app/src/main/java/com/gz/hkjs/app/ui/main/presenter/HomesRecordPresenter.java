package com.gz.hkjs.app.ui.main.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.HomesRecordData;
import com.gz.hkjs.app.ui.main.contract.HomesRecordContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

import java.util.HashMap;


/**
 * Created by administrator on 2017/5/2.
 */

public class HomesRecordPresenter extends HomesRecordContract.Presenter {

    @Override
    public void getRecipesListDataRequest(HashMap<String, String> map) {
        mRxManage.add(
                mModel.getRecipesListData(map).
                        subscribe(new RxSubscriber<HomesRecordData.DataBeanX>(mContext, false) {
                            @Override
                            public void onStart() {
                                super.onStart();
                                mView.showLoading(mContext.getString(R.string.loading));
                            }

                            @Override
                            protected void _onNext(HomesRecordData.DataBeanX dataBeanX) {
                                mView.returnRecipesListData(dataBeanX);
                                mView.stopLoading();
                            }

                            @Override
                            protected void _onError(String message) {
                                mView.showErrorTip(message);
                                mView.stopLoading();
                            }

                        }));

    }
}
