package com.gz.hkjs.app.ui.main.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.VideoData;
import com.gz.hkjs.app.ui.main.contract.AddTrainingPlanContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/4/10.
 */

public class AddTrainingPlanModel implements AddTrainingPlanContract.Model {
    @Override
    public Observable<List<VideoData.DataBean>> getVideosScreenData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE).getVideoList(Api.getCacheControl(), map)
                .map(new Func1<VideoData, List<VideoData.DataBean>>() {
                    @Override
                    public List<VideoData.DataBean> call(VideoData videoData) {
                        return videoData.getData();
                    }
                })
                .compose(RxSchedulers.<List<VideoData.DataBean>>io_main());
    }
}
