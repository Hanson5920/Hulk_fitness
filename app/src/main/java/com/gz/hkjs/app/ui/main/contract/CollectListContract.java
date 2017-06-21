package com.gz.hkjs.app.ui.main.contract;

import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by lwy on 2017/4/24.
 */

public interface CollectListContract {

    interface Model extends BaseModel {
        Observable<List<RecipesSummary.DataBean>> getCollectRecipesListData(HashMap<String, String> map);

        Observable<List<FindSummary.DataBean>> getCollectFindListData(HashMap<String, String> map);

    }

    interface View extends BaseView {
        //返回的收藏列表
        void returnCollectRecipesSummaryListData(List<RecipesSummary.DataBean> collectListData);

        //返回的收藏列表
        void returnCollectFindSummaryListData(List<FindSummary.DataBean> collectListData);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        //请求的收藏列表
        public abstract void getCollectRecipesListDataRequest(HashMap<String, String> map);

        //请求的收藏列表
        public abstract void getCollectFindListDataRequest(HashMap<String, String> map);

    }

}
