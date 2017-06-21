package com.gz.hkjs.app.ui.main.contract;

import com.gz.hkjs.app.bean.HomesDelete;
import com.gz.hkjs.app.bean.UserHomeData;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by Administrator on 2017/3/16.
 */

public interface TrainingListContract {

    /**
     * des:获取首页用户数据的contract
     */
    interface Model extends BaseModel {
        Observable<UserHomeData.DataBean> getUserHomeDataListData(HashMap<String, String> map);

        Observable<HomesDelete.DataBean> deleteHomesRequest(HashMap<String, String> map);

    }

    interface View extends BaseView {
        void returnUserHomeDataListData(UserHomeData.DataBean homeDataSummaries);
        void returnDeleteHomesRequestData(HomesDelete.DataBean homeDataSummaries);

    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getUserHomeDataRequest(HashMap<String, String> map);
        public abstract void deleteHomesRequest(HashMap<String, String> map);

    }
}
