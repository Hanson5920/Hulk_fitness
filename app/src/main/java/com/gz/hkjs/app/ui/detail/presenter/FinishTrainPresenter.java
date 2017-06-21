package com.gz.hkjs.app.ui.detail.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.ui.detail.contract.FinishTrainingContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.ToastUtil;

import java.util.HashMap;

/**
 * Created by husong on 2017/5/18.
 */

public class FinishTrainPresenter extends FinishTrainingContract.Presenter {

    @Override
    public void finishTrainRequest(HashMap<String, String> map) {
        mRxManage.add(mModel.finishTrainRequest(map)
        .subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.returnFinishTrain(s);
            }

            @Override
            protected void _onError(String message) {
                ToastUtil.showToastWithImg(message, R.drawable.ic_wrong);
            }
        }));
    }
}
