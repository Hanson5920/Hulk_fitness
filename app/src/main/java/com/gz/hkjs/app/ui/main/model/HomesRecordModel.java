package com.gz.hkjs.app.ui.main.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.HomesRecordData;
import com.gz.hkjs.app.ui.main.contract.HomesRecordContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by administrator on 2017/5/2.
 */

public class HomesRecordModel implements HomesRecordContract.Model {
    @Override
    public Observable<HomesRecordData.DataBeanX> getRecipesListData(HashMap<String, String> map) {

        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getHomesRecordList(Api.getCacheControl(), map)
                .map(new Func1<HomesRecordData, HomesRecordData.DataBeanX>() {
                    @Override
                    public HomesRecordData.DataBeanX call(HomesRecordData userHomeData) {
                        return userHomeData.getData();
                    }
                })
                .compose(RxSchedulers.<HomesRecordData.DataBeanX>io_main());

    }

}
