package com.gz.hkjs.app.widget;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.HomesRecordData;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class SectionAdapter2 extends BaseSectionQuickAdapter<HomesRecordData.DataBeanX.ListBean.DataBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public SectionAdapter2(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    private int i = 1;

    @Override
    protected void convertHead(BaseViewHolder helper, final HomesRecordData.DataBeanX.ListBean.DataBean item) {
        i = 1;
        String[] date = item.getO().getTitle().split("-");
        helper.setText(R.id.text_header, date[0] + "年" + date[1] + "月" + date[2] + "日");

//        int t = (int) (Double.valueOf(item.getO().getCount_times()) / 60);

        Double b = Double.valueOf(item.getO().getCount_times());
        if (b < 60) {
            helper.setText(R.id.text_time, ("" + b / 60).substring(0, 3) + "分钟");
        } else {
            helper.setText(R.id.text_time, (int) (b / 60) + "分钟");
        }

//        helper.setText(R.id.text_time, ("" + b / 60).substring(0, 3) + "分钟");

        helper.setText(R.id.text_quantity, "" + item.getO().getCount_num() + "千卡");
    }

    @Override
    protected void convert(BaseViewHolder helper, HomesRecordData.DataBeanX.ListBean.DataBean item) {

        helper.setText(R.id.text_num, "" + i++);
        helper.setText(R.id.text_name, item.getName());
//        int t = (int) (Double.valueOf(item.getTimes()) / 60);

        Double b = Double.valueOf(item.getTimes());
        if (b < 60) {
            helper.setText(R.id.text_tag, ("" + b / 60).substring(0, 3) + "分钟，" + item.getEnergy() + "千卡");

        } else {
            helper.setText(R.id.text_tag, (int) (b / 60) + "分钟，" + item.getEnergy() + "千卡");
        }

//        helper.setText(R.id.text_tag,t + "分钟，" + item.getEnergy() + "千卡");
    }
}
