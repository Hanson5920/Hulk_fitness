package com.gz.hkjs.app.ui.main.contract;

import com.gz.hkjs.app.bean.ChooseItem;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by Administrator on 2017/4/10.
 */

public interface ChooseItemContract {

    interface Model extends BaseModel {
        //请求获取视频
        Observable<List<ChooseItem.DataBeanX>> getChooseItemData(HashMap<String, String> map);
    }

    interface View extends BaseView {
        //返回获取的视频
        void returnChooseItemData(List<ChooseItem.DataBeanX> videoSummaries);
    }

    abstract static class Presenter extends BasePresenter<ChooseItemContract.View, ChooseItemContract.Model> {
        //发起获取视频请求
        public abstract void getChooseItemRequest(HashMap<String, String> map);
    }


}
