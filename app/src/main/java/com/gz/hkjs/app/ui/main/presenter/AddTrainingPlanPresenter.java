package com.gz.hkjs.app.ui.main.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.app.AppConstant;
import com.gz.hkjs.app.bean.VideoData;
import com.gz.hkjs.app.ui.main.contract.AddTrainingPlanContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

import java.util.HashMap;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Administrator on 2017/4/10.
 */

public class AddTrainingPlanPresenter extends AddTrainingPlanContract.Presenter {

    @Override
    public void onStart() {
        super.onStart();
//        //监听返回顶部动作
//        mRxManage.on(AppConstant.NEWS_LIST_TO_TOP, new Action1<Object>() {
//            @Override
//            public void call(Object o) {
//                mView.scrolltoTop();
//            }
//        });
    }

    @Override
    public void getVideosScreenRequest(HashMap<String, String> map) {
        mRxManage.add(mModel.getVideosScreenData(map).subscribe(new RxSubscriber<List<VideoData.DataBean>>(mContext, false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(List<VideoData.DataBean> videoDatas) {
                mView.returnVideosScreenData(videoDatas);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
