package com.gz.hkjs.app.ui.main.contract;

import com.gz.hkjs.app.bean.FindSummary;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface FindListContract {

    interface Model extends BaseModel {
        //请求获取发现
        Observable<List<FindSummary.DataBean>> getFindListData(HashMap<String, String> map);
    }

    interface View extends BaseView {
        void returnFindData(List<FindSummary.DataBean> findSummaries);

//        void scrolltoTop();
    }

    abstract static class Presenter extends BasePresenter<View, Model> {

        //发起发现请求
        public abstract void getFindListDataRequest(HashMap<String, String> map);
    }

}
