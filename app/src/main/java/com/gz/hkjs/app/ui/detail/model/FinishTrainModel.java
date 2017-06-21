package com.gz.hkjs.app.ui.detail.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.SimpleBean;
import com.gz.hkjs.app.ui.detail.contract.FinishTrainingContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by husong on 2017/5/18.
 */

public class FinishTrainModel implements FinishTrainingContract.Model {
    @Override
    public Observable<String> finishTrainRequest(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .TrainAddRecord(Api.getCacheControl(), map)
                .map(new Func1<SimpleBean, String>() {
                    @Override
                    public String call(SimpleBean s) {
                        return s.getData().getStatus();
                    }
                }).compose(RxSchedulers.<String>io_main());
    }
}
