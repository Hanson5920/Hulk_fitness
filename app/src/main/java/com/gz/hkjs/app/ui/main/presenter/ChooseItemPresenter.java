package com.gz.hkjs.app.ui.main.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.ChooseItem;
import com.gz.hkjs.app.ui.main.contract.ChooseItemContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ChooseItemPresenter extends ChooseItemContract.Presenter {
    @Override
    public void getChooseItemRequest(HashMap<String, String> map) {
        mRxManage.add(
                mModel.getChooseItemData(map).subscribe(new RxSubscriber<List<ChooseItem.DataBeanX>>(mContext, false) {

                    @Override
                    public void onStart() {
                        super.onStart();
                        mView.showLoading(mContext.getString(R.string.loading));
                    }

                    @Override
                    protected void _onNext(List<ChooseItem.DataBeanX> videoDatas) {
                        mView.returnChooseItemData(videoDatas);
                        mView.stopLoading();
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.stopLoading();
                    }
                }));
    }
}
