package com.gz.hkjs.app.ui.detail.model;

import com.gz.hkjs.app.api.Api;
import com.gz.hkjs.app.api.HostType;
import com.gz.hkjs.app.bean.SimpleBean;
import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.gz.hkjs.app.ui.detail.contract.TrainingVideoDetailContract;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.download.DownloadListener;
import com.jaydenxiao.common.download.FileDownloadUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/4/14.
 */

public class TrainVideoDetailModel implements TrainingVideoDetailContract.Model {
    @Override
    public Observable<TrainVideoDetail.DataBean> getTrainVideoDetailData(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .getTrainVedioDetail(Api.getCacheControl(), map)
                .map(new Func1<TrainVideoDetail, TrainVideoDetail.DataBean>() {
                    @Override
                    public TrainVideoDetail.DataBean call(TrainVideoDetail trainVideoDetail) {
                        return trainVideoDetail.getData();
                    }
                })
                .compose(RxSchedulers.<TrainVideoDetail.DataBean>io_main());
    }


    @Override
    public void getDownloadVideoData(String fileName, HashMap<String, String> hashMap, DownloadListener downloadListener) {
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            String filePath = FileDownloadUtil.getFilePathName(entry.getKey());
            String fileUrl = entry.getValue();
            tasks.add(FileDownloader.getImpl()
                    .create(fileUrl)
                    .setPath(filePath));
        }
        FileDownloadUtil.downLoadGroupVideo(fileName, tasks, downloadListener);
    }

    @Override
    public Observable<String> addTrainRequest(HashMap<String, String> map) {
        return Api.getDefault(HostType.NORMAL_HOST_TYPE)
                .addTrainHomesRequest(Api.getCacheControl(), map)
                .map(new Func1<SimpleBean, String>() {
                    @Override
                    public String call(SimpleBean simpleBean) {
                        return simpleBean.getData().getStatus();
                    }
                })
                .compose(RxSchedulers.<String>io_main());
    }

}
