package com.gz.hkjs.app.ui.main.presenter;

import com.gz.hkjs.app.bean.FeedbackData;
import com.gz.hkjs.app.ui.main.contract.FeedBackModelContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lwy on 2017/4/24.
 */

public class FeedBackPresenter extends FeedBackModelContract.Presenter {
    @Override
    public void getFeedBackDataRequest(HashMap<String, String> map) {
        Logger.e("请求服务器");
        mRxManage.add(mModel.getFeedBackData(map).subscribe(new RxSubscriber<FeedbackData>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            protected void _onNext(FeedbackData feedbackDatas) {
                Logger.e("请求成功，数据 = "+feedbackDatas.toString());
                mView.returnFeedBackData(feedbackDatas);
            }

            @Override
            protected void _onError(String message) {

                Logger.e("请求失败，数据 = "+message);
            }
        }));
    }
}
