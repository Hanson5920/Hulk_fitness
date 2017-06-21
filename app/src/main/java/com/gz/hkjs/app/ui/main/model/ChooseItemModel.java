package com.gz.hkjs.app.ui.main.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.ChooseItem;
import com.gz.hkjs.app.ui.main.contract.ChooseItemContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ChooseItemModel implements ChooseItemContract.Model {
    @Override
    public Observable<List<ChooseItem.DataBeanX>> getChooseItemData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getChooseItemList(Api.getCacheControl(), map)
                .map(new Func1<ChooseItem, List<ChooseItem.DataBeanX>>() {
                    @Override
                    public List<ChooseItem.DataBeanX> call(ChooseItem chooseItem) {
                        return chooseItem.getData();
                    }
                })
                .compose(RxSchedulers.<List<ChooseItem.DataBeanX>>io_main());
    }
}
