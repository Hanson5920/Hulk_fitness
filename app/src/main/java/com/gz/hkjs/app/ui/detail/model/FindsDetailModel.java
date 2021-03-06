package com.gz.hkjs.app.ui.detail.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.CollectAddData;
import com.gz.hkjs.app.bean.CollectDeleteData;
import com.gz.hkjs.app.bean.FindDetail;
import com.gz.hkjs.app.ui.detail.contract.FindDetailContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/3/21.
 */

public class FindsDetailModel implements FindDetailContract.Model {
    @Override
    public Observable<FindDetail.DataBean> getOneFindsData(HashMap<String,String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE).getNewDetail(Api.getCacheControl(), map)
                .map(new Func1<FindDetail, FindDetail.DataBean>() {
                    @Override
                    public FindDetail.DataBean call(FindDetail findDetail) {
                        return findDetail.getData();
                    }
                })
                .compose(RxSchedulers.<FindDetail.DataBean>io_main());
    }

    @Override
    public Observable<CollectAddData.DataBean> getCollectAddData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getCollectionAdd(Api.getCacheControl(), map)
                .map(new Func1<CollectAddData, CollectAddData.DataBean>() {
                    @Override
                    public CollectAddData.DataBean call(CollectAddData collectAddData) {
                        return collectAddData.getData();
                    }
                })
                .compose(RxSchedulers.<CollectAddData.DataBean>io_main());
    }

    @Override
    public Observable<CollectDeleteData.DataBean> getCollectDeleteData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getCollectionDelete(Api.getCacheControl(), map)
                .map(new Func1<CollectDeleteData, CollectDeleteData.DataBean>() {
                    @Override
                    public CollectDeleteData.DataBean call(CollectDeleteData collectDeleteData) {
                        return collectDeleteData.getData();
                    }
                })
                .compose(RxSchedulers.<CollectDeleteData.DataBean>io_main());
    }
}
