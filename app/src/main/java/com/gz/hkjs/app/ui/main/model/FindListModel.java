package com.gz.hkjs.app.ui.main.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.ui.main.contract.FindListContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/3/16.
 */

public class FindListModel implements FindListContract.Model {

    /**
     * 获取发现列表
     *
     * @param map (new Funcl)---将你的FindSummary 转换成List<>集合，详情RxJava
     * @return
     */
    @Override
    public Observable<List<FindSummary.DataBean>> getFindListData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE).getFindsList(Api.getCacheControl(), map)
                .map(new Func1<FindSummary, List<FindSummary.DataBean>>() {
                    @Override
                    public List<FindSummary.DataBean> call(FindSummary findSummary) {
                        return findSummary.getData();
                    }
                })
                .compose(RxSchedulers.<List<FindSummary.DataBean>>io_main());
    }
}
