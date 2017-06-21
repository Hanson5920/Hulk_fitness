package com.gz.hkjs.app.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.HomesRecordData;
import com.gz.hkjs.app.bean.TrainData;
import com.gz.hkjs.app.parameter.JMClassUser;
import com.gz.hkjs.app.ui.main.contract.HomesRecordContract;
import com.gz.hkjs.app.ui.main.linechart.BrokenLineCusVisitView;
import com.gz.hkjs.app.ui.main.model.HomesRecordModel;
import com.gz.hkjs.app.ui.main.presenter.HomesRecordPresenter;
import com.gz.hkjs.app.widget.SectionAdapter2;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 训练统计
 * Company: chuangxinmengxiang-chongqingwapushidai
 * User: HongBin(Hongbin1011@gmail.com)
 * Date: 2017-04-21
 * Time: 11:08
 */

public class TrainDataChartViewActivity extends BaseActivity<HomesRecordPresenter, HomesRecordModel> implements HomesRecordContract.View {

    @BindView(R.id.id_back)
    ImageView idBack;

    @BindView(R.id.id_total_toolbar_title)
    TextView idTotalToolbarTitle;
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;
    private TextView idHomeDataWancheng;
    private TextView idHomeDataLeijiDays;
    private TextView idHomeDataQianka;
    private TextView txtTimes;

    private BrokenLineCusVisitView brokenline;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    private List<TrainData> mdata = new ArrayList<>();

    private View view;
    private SectionAdapter2 sectionAdapter;
    List<HomesRecordData.DataBeanX.ListBean.DataBean> mDataList;

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, TrainDataChartViewActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_trainning_tongji_data;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        idTotalToolbarTitle.setText("训练统计");

        view = getLayoutInflater().inflate(R.layout.item_train_head_view, null, false);

        brokenline = ButterKnife.findById(view, R.id.brokenline);
        idHomeDataWancheng = ButterKnife.findById(view, R.id.id_home_data_wancheng);
        idHomeDataLeijiDays = ButterKnife.findById(view, R.id.id_home_data_leiji_days);
        idHomeDataQianka = ButterKnife.findById(view, R.id.id_home_data_qianka);
        txtTimes = ButterKnife.findById(view, R.id.txt_times);

        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //发起请求
        mPresenter.getRecipesListDataRequest(
                JMClassUser.MyJMClass());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDataList = new ArrayList<>();
        sectionAdapter = new SectionAdapter2(R.layout.item_train_section_content, R.layout.item_train_def_section_head, mDataList);
        mRecyclerView.setAdapter(sectionAdapter);
    }

    @Override
    public void showLoading(String title) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public void stopLoading() {
        if (sectionAdapter != null && sectionAdapter.getItemCount() > 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        } else {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        }
    }

    @Override
    public void showErrorTip(String msg) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public void returnRecipesListData(HomesRecordData.DataBeanX dataBeen) {
        Double b = Double.valueOf(dataBeen.getTimes());
        if (b < 60 && b > 0) {
            txtTimes.setText(("" + b / 60).substring(0, 3));
        } else {
            txtTimes.setText("" + (int) (b / 60));
        }

        for (int i = 0; i < dataBeen.getList().size(); i++) {
            HomesRecordData.DataBeanX.ListBean listBean = dataBeen.getList().get(i);
            TrainData tb = new TrainData(listBean.getCount_time(), listBean.getCount_num(), listBean.getCount_num());
            brokenline.setMaxHeight(listBean.getCount_num());
            mdata.add(tb);
        }
        brokenline.setMdata(mdata);

        idHomeDataWancheng.setText(dataBeen.getNum());
        idHomeDataLeijiDays.setText(dataBeen.getDay());
        idHomeDataQianka.setText(dataBeen.getEnergy());

        List<HomesRecordData.DataBeanX.ListBean.DataBean> list = new ArrayList<>();

        for (HomesRecordData.DataBeanX.ListBean bean : dataBeen.getList()) {
            list.add(new HomesRecordData.DataBeanX.ListBean.DataBean(true, bean));
            list.addAll(bean.getData());
        }

//        for (int i = 0; i < dataBeen.getList().size(); i++) {
//            HomesRecordData.DataBeanX.ListBean bean = dataBeen.getList().get(dataBeen.getList().size() - i - 1);
//            list.add(new HomesRecordData.DataBeanX.ListBean.DataBean(true, bean));
//            list.addAll(bean.getData());
//        }

        sectionAdapter.addHeaderView(view);
        mDataList.addAll(list);
        sectionAdapter.notifyDataSetChanged();
    }


}
