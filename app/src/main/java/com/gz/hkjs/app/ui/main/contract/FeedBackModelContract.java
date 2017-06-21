package com.gz.hkjs.app.ui.main.contract;

import com.gz.hkjs.app.bean.FeedbackData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by lwy on 2017/4/24.
 */

public interface FeedBackModelContract {
    interface Model extends BaseModel{

        Observable<FeedbackData> getFeedBackData(HashMap<String, String> map);
    }
    interface View extends BaseView {
        //返回获取的新闻
        void returnFeedBackData(FeedbackData dataBeen);

    }
    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取新闻请求
        public abstract void getFeedBackDataRequest(HashMap<String,String> map);
    }
}
