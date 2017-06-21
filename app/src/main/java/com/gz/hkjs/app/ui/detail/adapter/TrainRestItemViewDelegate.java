package com.gz.hkjs.app.ui.detail.adapter;

import android.content.Context;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by xxq on 2017/5/12 0012.
 * 休息 Item
 */

public class TrainRestItemViewDelegate implements ItemViewDelegate<TrainVideoDetail.DataBean.StepBean> {
    private Context context;

    public TrainRestItemViewDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_train_rest;
    }

    @Override
    public boolean isForViewType(TrainVideoDetail.DataBean.StepBean item, int position) {
        return item.getStep_type().equals("2");
    }

    @Override
    public void convert(ViewHolder holder, TrainVideoDetail.DataBean.StepBean stepBean, int position) {
        holder.setText(R.id.item_tv_train_state,String.format(context.getString(R.string.train_rest),stepBean.getNum_time()) );
    }
}
