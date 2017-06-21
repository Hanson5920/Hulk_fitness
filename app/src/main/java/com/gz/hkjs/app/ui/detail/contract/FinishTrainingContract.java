package com.gz.hkjs.app.ui.detail.contract;

import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by husong on 2017/5/18.
 */

public interface FinishTrainingContract {
    interface Model extends BaseModel{
        Observable<String> finishTrainRequest(HashMap<String, String> map);
    }

    interface View extends BaseView {
        void returnFinishTrain(String state);
    }

    abstract class Presenter extends BasePresenter<FinishTrainingContract.View , FinishTrainingContract.Model>{
        public abstract void finishTrainRequest(HashMap<String, String> map);
    }
}
