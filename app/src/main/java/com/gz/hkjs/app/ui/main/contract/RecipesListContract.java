package com.gz.hkjs.app.ui.main.contract;

import com.gz.hkjs.app.bean.RecipesSummary;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface RecipesListContract {

    interface Model extends BaseModel {
        Observable<List<RecipesSummary.DataBean>> getRecipesListData(HashMap<String,String> map);

        //获取食谱标签数据
        Observable<List<RecipesSummary.DataBean>> getRecipesTabData(HashMap<String,String> map);
    }

    interface View extends BaseView {
        //返回获取的新闻
        void returnRecipesListData(List<RecipesSummary.DataBean> dataBeen);

        void returnRecipesTabData(List<RecipesSummary.DataBean> dataBeen);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取新闻请求
        public abstract void getRecipesListDataRequest(HashMap<String,String> map);

        //
        public abstract void getRecipesTabDataRequest(HashMap<String,String> map);
    }

}
