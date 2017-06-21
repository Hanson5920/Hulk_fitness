package com.gz.hkjs.app.ui.detail.presenter;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.gz.hkjs.app.ui.detail.contract.TrainingVideoDetailContract;
import com.jaydenxiao.common.baserx.RxSubscriber;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.jaydenxiao.common.download.DownloadListener;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/14.
 */

public class TrainVideoDetailPresenter extends TrainingVideoDetailContract.Presenter {

    @Override
    public void getTrainVideoDetailDataRequest(HashMap<String, String> map) {
        mRxManage.add(mModel.getTrainVideoDetailData(map).subscribe(new RxSubscriber<TrainVideoDetail.DataBean>(mContext) {
            @Override
            protected void _onNext(TrainVideoDetail.DataBean trainVideoDetail) {
                mView.returnTrainVideoDetailData(trainVideoDetail);
            }

            @Override
            protected void _onError(String message) {
                ToastUtil.showToastWithImg(message, R.drawable.ic_wrong);
            }
        }));
    }

    @Override
    public void getDownloadVideoRequest(String fileName, HashMap<String, String> hashMap) {
        mModel.getDownloadVideoData(fileName, hashMap, new DownloadListener() {
            @Override
            public void success(HashMap<String, String> FilePathMap) {
                mView.returnVideoData(FilePathMap);
            }

            @Override
            public void success(String filePath) {

            }

            @Override
            public void progress(int soFarBytes, int totalBytes) {

            }
        });

    }

    @Override
    public void addTrainRequest(HashMap<String, String> map) {
        mRxManage.add(
                mModel.addTrainRequest(map).
                        subscribe(new RxSubscriber<String>(mContext, false) {
                            @Override
                            protected void _onNext(String state) {
                                mView.returnAddTrain(state);
                            }

                            @Override
                            protected void _onError(String message) {
                                ToastUtil.showToastWithImg(message, R.drawable.ic_wrong);
                            }
                        }));
    }


}
