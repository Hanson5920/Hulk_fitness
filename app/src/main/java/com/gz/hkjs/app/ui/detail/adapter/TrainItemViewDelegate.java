package com.gz.hkjs.app.ui.detail.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * Created by xxq on 2017/5/12 0012.
 * 训练 item
 */

public class TrainItemViewDelegate implements ItemViewDelegate<TrainVideoDetail.DataBean.StepBean> {
    private Context context;

    public TrainItemViewDelegate(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_train_vedio_detail;
    }

    @Override
    public boolean isForViewType(TrainVideoDetail.DataBean.StepBean item, int position) {
        return item.getStep_type().equals("1");
    }

    @Override
    public void convert(ViewHolder holder, TrainVideoDetail.DataBean.StepBean stepBean, int position) {
        holder.setText(R.id.item_tv_train_detail_num, stepBean.getPx_num());
        ImageView imageView = holder.getView(R.id.item_img_train);
        ImageLoaderUtils.display(context, imageView, stepBean.getLogo_url());
        holder.setText(R.id.item_tv_train_detail_title, stepBean.getVideo_name());
        String stepString;
        if (TextUtils.isEmpty(stepBean.getFrequency()) || stepBean.getFrequency().equals("0")) {//表示需要用秒表示了
            stepString = context.getString(R.string.train_step_time);
            stepString = String.format(stepString, stepBean.getNum(), stepBean.getNum_time());
        } else {
            stepString = context.getString(R.string.train_step_number);
            stepString = String.format(stepString, stepBean.getNum(), stepBean.getFrequency());
        }

        holder.setText(R.id.item_tv_train_detail_content, stepString);
    }
}
