package com.gz.hkjs.app.ui.main.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.FeedbackData;
import com.gz.hkjs.app.ui.main.contract.FeedBackModelContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by lwy on 2017/4/24.
 */

public class FeedBackModel implements FeedBackModelContract.Model {
    @Override
    public Observable<FeedbackData> getFeedBackData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getAdviceCreate(Api.getCacheControl(),map)
                .compose(RxSchedulers.<FeedbackData>io_main());
    }

}
