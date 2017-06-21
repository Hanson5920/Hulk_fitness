package com.gz.hkjs.app.ui.main.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.gz.hkjs.app.ui.main.contract.CollectListContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by lwy on 2017/4/24.
 */

public class CollectListModel implements CollectListContract.Model {

    @Override
    public Observable<List<RecipesSummary.DataBean>> getCollectRecipesListData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getCollectionRecipeList(Api.getCacheControl(), map)
                .map(new Func1<RecipesSummary, List<RecipesSummary.DataBean>>() {
                    @Override
                    public List<RecipesSummary.DataBean> call(RecipesSummary findSummary) {
                        return findSummary.getData();
                    }
                })
                .compose(RxSchedulers.<List<RecipesSummary.DataBean>>io_main());
    }

    @Override
    public Observable<List<FindSummary.DataBean>> getCollectFindListData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getCollectionFindList(Api.getCacheControl(), map)
                .map(new Func1<FindSummary, List<FindSummary.DataBean>>() {
                    @Override
                    public List<FindSummary.DataBean> call(FindSummary findSummary) {
                        return findSummary.getData();
                    }
                })
                .compose(RxSchedulers.<List<FindSummary.DataBean>>io_main());
    }
}
