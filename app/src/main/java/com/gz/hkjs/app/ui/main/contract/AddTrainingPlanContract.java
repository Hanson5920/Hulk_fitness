package com.gz.hkjs.app.ui.main.contract;

import com.gz.hkjs.app.bean.VideoData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface AddTrainingPlanContract {

    /**
     * des:视频列表contract
     */
    interface Model extends BaseModel {
        //请求获取视频
        Observable<List<VideoData.DataBean>> getVideosScreenData(HashMap<String, String> map);
    }

    interface View extends BaseView {
        //返回获取的视频
        void returnVideosScreenData(List<VideoData.DataBean> videoSummaries);
//
//        //返回顶部
//        void scrolltoTop();
    }

    abstract static class Presenter extends BasePresenter<AddTrainingPlanContract.View, AddTrainingPlanContract.Model> {
        //发起获取视频请求
        public abstract void getVideosScreenRequest(HashMap<String, String> map);
    }

}
