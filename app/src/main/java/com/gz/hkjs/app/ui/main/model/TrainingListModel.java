package com.gz.hkjs.app.ui.main.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.HomesDelete;
import com.gz.hkjs.app.bean.UserHomeData;
import com.gz.hkjs.app.ui.main.contract.TrainingListContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/3/16.
 */

public class TrainingListModel implements TrainingListContract.Model {

    @Override
    public Observable<UserHomeData.DataBean> getUserHomeDataListData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getHomeDataList(Api.getCacheControl(), map)
                .map(new Func1<UserHomeData, UserHomeData.DataBean>() {
                    @Override
                    public UserHomeData.DataBean call(UserHomeData userHomeData) {
                        return userHomeData.getData();
                    }
                }).compose(RxSchedulers.<UserHomeData.DataBean>io_main());
    }

    @Override
    public Observable<HomesDelete.DataBean> deleteHomesRequest(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .deleteHomesRequest(Api.getCacheControl(), map)
                .map(new Func1<HomesDelete, HomesDelete.DataBean>() {
                    @Override
                    public HomesDelete.DataBean call(HomesDelete userHomeData) {
                        return userHomeData.getData();
                    }
                }).compose(RxSchedulers.<HomesDelete.DataBean>io_main());
    }
}
