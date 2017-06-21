package com.gz.hkjs.app.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.VideoData;
import com.gz.hkjs.app.parameter.JMClassVideo;
import com.gz.hkjs.app.ui.detail.activity.TrainingVideoDetailActivity;
import com.gz.hkjs.app.ui.main.contract.AddTrainingPlanContract;
import com.gz.hkjs.app.ui.main.model.AddTrainingPlanModel;
import com.gz.hkjs.app.ui.main.presenter.AddTrainingPlanPresenter;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 自选课程
 * Created by Administrator on 2017/4/10.
 */

public class AddTraningPlanActivity extends BaseActivity<AddTrainingPlanPresenter, AddTrainingPlanModel>
        implements AddTrainingPlanContract.View, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.irc_traning_all)
    IRecyclerView ircTraningAll;
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;

    @BindView(R.id.id_back)
    ImageView idBack;
    @BindView(R.id.id_right_tv)
    TextView idAllvedioChoose;
    @BindView(R.id.id_total_toolbar_title)
    TextView idTotalToolbarTitle;

    private int code = 5;
    public static final int resultCode = 10;
    CommonRecycleViewAdapter<VideoData.DataBean> traningListAdapter;

    private int mStartPage = 1;
    private HashMap<String, String> wheremap;

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, AddTraningPlanActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_alltrainingvedio;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        idAllvedioChoose.setText("筛选");
        idAllvedioChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseItemActivity.startAction(AddTraningPlanActivity.this, code, wheremap);
            }
        });

        idTotalToolbarTitle.setText("自选课程");
        traningListAdapter = new CommonRecycleViewAdapter<VideoData.DataBean>(this, R.layout.item_train_all_vedio) {
            @Override
            public void convert(ViewHolderHelper helper, final VideoData.DataBean dataBean) {
                final ImageView bg = helper.getView(R.id.item_train_all_img);
                RelativeLayout fm = helper.getView(R.id.rl_root_train_all);
                TextView title = helper.getView(R.id.item_train_all_title);
                TextView time = helper.getView(R.id.item_train_all_time);
                TextView energy = helper.getView(R.id.item_train_all_energy);
                RelativeLayout difficultyLayout = helper.getView(R.id.item_train_difficulty_layout);
                TextView tvDifficulty = helper.getView(R.id.item_train_difficulty);

                Glide.with(mContext).load(dataBean.getLogo_url())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .placeholder(com.jaydenxiao.common.R.drawable.ic_image_loading)
                        .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                        .centerCrop().override(1090, 1090 * 3 / 4)
                        .fitCenter()
                        .crossFade().into(bg);

                difficultyLayout.setVisibility(View.VISIBLE);
                tvDifficulty.setText(dataBean.getExaggerate_name());
                title.setText(dataBean.getName());
                time.setText(String.valueOf(Double.valueOf(dataBean.getWtime()) / 60).substring(0, String.valueOf(Double.valueOf(dataBean.getWtime()) / 60).indexOf(".")) + "分钟");
                energy.setText(dataBean.getEnergy() + "千卡");

                fm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TrainingVideoDetailActivity.startAction(mContext, dataBean.getId(), dataBean.getLogo_url());
                    }
                });
            }
        };

        ircTraningAll.setAdapter(traningListAdapter);
        ircTraningAll.setLayoutManager(new LinearLayoutManager(this));
        ircTraningAll.setOnRefreshListener(this);
        ircTraningAll.setOnLoadMoreListener(this);


        //数据为空才重新发起请求
        if (traningListAdapter.getSize() <= 0) {
            //发起请求
            mStartPage = 1;
            mPresenter.getVideosScreenRequest(JMClassVideo.MyJMClass(String.valueOf(mStartPage), "1" ));
        }
    }

    @Override
    public void onRefresh() {
        mStartPage = 1;
        //发起请求
         traningListAdapter.getPageBean().setRefresh(true);
        mPresenter.getVideosScreenRequest(JMClassVideo.MyJMClass(String.valueOf(mStartPage), "1" ));
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        traningListAdapter.getPageBean().setRefresh(false);
        //发起请求
        ircTraningAll.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getVideosScreenRequest(JMClassVideo.MyJMClass(String.valueOf(mStartPage), "1" ));
    }

    @Override
    public void showLoading(String title) {
        if (traningListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    @Override
    public void stopLoading() {
        if (traningListAdapter.getItemCount() > 0) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        } else {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        }
    }

    @Override
    public void showErrorTip(String msg) {
        if (traningListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    @Override
    public void returnVideosScreenData(List<VideoData.DataBean> videoDatas) {
        if (videoDatas != null) {
            mStartPage += 1;
            if (traningListAdapter.getPageBean().isRefresh()) {
                ircTraningAll.setRefreshing(false);
                traningListAdapter.replaceAll(videoDatas);
            } else {
                if (videoDatas.size() > 0) {
                    ircTraningAll.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    traningListAdapter.addAll(videoDatas);
                } else {
                    ircTraningAll.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (code == requestCode && resultCode == this.resultCode) {
            //得到数据集
            Bundle bundle = data.getExtras();
            //获得自定义类
            wheremap = (HashMap<String, String>) bundle.get("whereMap");

//            if (serializableMap.size() < 1) return;
            mStartPage = 1;
            HashMap<String, String> map = JMClassVideo.MyJMClass(String.valueOf(mStartPage), "1" );
            Iterator iter3 = wheremap.entrySet().iterator();
            while (iter3.hasNext()) {
                Map.Entry entry = (Map.Entry) iter3.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                map.put(key, val);
            }
            //发起请求
            ircTraningAll.setRefreshing(true);
            mPresenter.getVideosScreenRequest(map);
        }
    }
}
