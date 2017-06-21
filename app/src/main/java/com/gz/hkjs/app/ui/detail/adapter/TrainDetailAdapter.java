package com.gz.hkjs.app.ui.detail.adapter;

import android.content.Context;

import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by xxq on 2017/5/12 0012.
 * 视频详情 adapter
 */

public class TrainDetailAdapter extends MultiItemTypeAdapter<TrainVideoDetail.DataBean.StepBean> {
    public TrainDetailAdapter(Context context, List<TrainVideoDetail.DataBean.StepBean> datas) {
        super(context, datas);
        addItemViewDelegate(new TrainItemViewDelegate(context));
        addItemViewDelegate(new TrainRestItemViewDelegate(context));
    }
}
