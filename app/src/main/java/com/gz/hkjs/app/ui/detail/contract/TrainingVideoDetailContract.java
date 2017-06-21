package com.gz.hkjs.app.ui.detail.contract;

import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;
import com.jaydenxiao.common.download.DownloadListener;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface TrainingVideoDetailContract {


    interface Model extends BaseModel {
        //请求获取发现
        Observable<TrainVideoDetail.DataBean> getTrainVideoDetailData(HashMap<String, String> map);

        void getDownloadVideoData(String fileName, HashMap<String, String> hashMap, DownloadListener downloadListener);

        Observable<String> addTrainRequest(HashMap<String, String> map);

    }

    interface View extends BaseView {
        void returnTrainVideoDetailData(TrainVideoDetail.DataBean TrainVideoStepBean);

        void returnVideoData(HashMap<String, String> pathHashMap);

        void returnAddTrain(String state);

    }

    abstract class Presenter extends BasePresenter<TrainingVideoDetailContract.View, TrainingVideoDetailContract.Model> {

        //发起发现请求
        public abstract void getTrainVideoDetailDataRequest(HashMap<String, String> map);

        //下载全部视频

        /**
         * @param fileName fileName.txt 将保存到内存卡中
         * @param hashMap
         */
        public abstract void getDownloadVideoRequest(String fileName, HashMap<String, String> hashMap);

        //添加训练项目
        public abstract void addTrainRequest(HashMap<String, String> map);

    }
}
