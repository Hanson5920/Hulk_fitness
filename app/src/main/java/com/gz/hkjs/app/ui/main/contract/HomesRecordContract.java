package com.gz.hkjs.app.ui.main.contract;

import com.gz.hkjs.app.bean.HomesRecordData;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by administrator on 2017/5/2.
 */

public interface HomesRecordContract {

    interface Model extends BaseModel {
        Observable<HomesRecordData.DataBeanX> getRecipesListData(HashMap<String,String> map);
    }

    interface View extends BaseView {

        //返回获取的新闻
        void returnRecipesListData(HomesRecordData.DataBeanX dataBeen);

    }

    abstract static class Presenter extends BasePresenter<HomesRecordContract.View, HomesRecordContract.Model> {
        //发起获取新闻请求
        public abstract void getRecipesListDataRequest(HashMap<String,String> map);
    }

}
