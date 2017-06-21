package com.gz.hkjs.app.ui.main.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.app.AppApplication;
import com.gz.hkjs.app.app.AppConstant;
import com.gz.hkjs.app.bean.HomesDelete;
import com.gz.hkjs.app.bean.UserHomeData;
import com.gz.hkjs.app.jpush.ExampleUtil;
import com.gz.hkjs.app.parameter.JMClassUser;
import com.gz.hkjs.app.ui.detail.activity.TrainingVideoDetailActivity;
import com.gz.hkjs.app.ui.main.activity.AddTraningPlanActivity;
import com.gz.hkjs.app.ui.main.activity.TrainDataChartViewActivity;
import com.gz.hkjs.app.ui.main.contract.TrainingListContract;
import com.gz.hkjs.app.ui.main.model.TrainingListModel;
import com.gz.hkjs.app.ui.main.presenter.TrainingListPresenter;
import com.gz.hkjs.app.util.AppUtil;
import com.gz.hkjs.app.util.LoginUtil;
import com.gz.hkjs.app.widget.BGASwipeItemLayout;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.ACache;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.lxh.userlibrary.UserCenter;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * 训练
 * Created by Administrator on 2017/3/15.
 */

public class TrainingMainFragment extends BaseFragment<TrainingListPresenter, TrainingListModel> implements TrainingListContract.View, OnRefreshListener {

    //UI
    @BindView(R.id.irc_traning)
    IRecyclerView ircTran;
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;
    @BindView(R.id.id_back)
    ImageView idBack;
    @BindView(R.id.id_training_toolbar_title)
    ImageView idTrainingToolbarTitle;
    @BindView(R.id.id_training_right_tv)
    ImageView idTrainingRightTv;

    public UserHomeData.DataBean.ListBean deleteDataBean;
    TextView idHomeDataLeiji;
    TextView idHomeDataWancheng;
    TextView idHomeDataLeijiDays;
    TextView idHomeDataQianka;
    LinearLayout line_tonjii;

    public static String strLogin = "Login";
    public static String strLogout = "Logout";

    //普通类型适配器
    private CommonRecycleViewAdapter<UserHomeData.DataBean.ListBean> trainingListAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_traning;
    }

    //初始化
    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    @Override
    protected void initView() {
        Logger.e("进入了训练刷新");
//        UserCenter.getInstance().getMessagePump().
//                broadcastMessage(USER_DATA_STATE);
        trainingListAdapter = new CommonRecycleViewAdapter<UserHomeData.DataBean.ListBean>(getContext(), R.layout.item_train_all_vedio) {
            @Override
            public void convert(final ViewHolderHelper helper, final UserHomeData.DataBean.ListBean dataBean) {
                RelativeLayout fm = helper.getView(R.id.rl_root_train_all);
                final ImageView img = helper.getView(R.id.item_train_all_img);
                TextView title = helper.getView(R.id.item_train_all_title);
                TextView time = helper.getView(R.id.item_train_all_time);
                TextView energy = helper.getView(R.id.item_train_all_energy);
                BGASwipeItemLayout bgaswipe_root = helper.getView(R.id.sil_item_bgaswipe_root);
                RelativeLayout difficultyLayout = helper.getView(R.id.item_train_difficulty_layout);
                TextView tvDifficulty = helper.getView(R.id.item_train_difficulty);

//                Log.e("husong", "----------->>>dataBean=" + (dataBean == null) + " " + dataBean.getId() + "  " + dataBean.getName() + "  " + dataBean.getEnergy()
//                        + " " + dataBean.getLogo_url() + "  " + dataBean.getWtime() + "  uid=" + UserCenter.getUid() + "  " + AppUtil.getVersionName(AppApplication.getContext()) +
//                        "  " + AppConstant.OS+"  imei="+ ExampleUtil.getImei(mContext));
                if (!TextUtils.isEmpty(dataBean.getId())) {
                    energy.setVisibility(View.VISIBLE);
                    difficultyLayout.setVisibility(View.VISIBLE);
                    try {
                        tvDifficulty.setText(dataBean.getExaggerate_name());
                        title.setText(dataBean.getName());
                        energy.setText(dataBean.getEnergy() + "千卡");

                        time.setText(String.valueOf(Double.valueOf(dataBean.getWtime()) / 60).substring(0, String.valueOf(Double.valueOf(dataBean.getWtime()) / 60).indexOf(".")) + "分钟");
                        Glide.with(mContext).load(dataBean.getLogo_url())
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                                .centerCrop().override(1090, 1090 * 3 / 4)
                                .fitCenter()
                                .crossFade().into(img);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    bgaswipe_root.setSwipeAble(true);
                    TextView delete = helper.getView(R.id.tv_item_bgaswipe_delete);
                    delete.setTag(dataBean);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startProgressDialog();
                            closeOpenedSwipeItemLayoutWithAnim();
                            mPresenter.deleteHomesRequest(JMClassUser.MyHomesDelete(dataBean.getId()));
                            deleteDataBean = (UserHomeData.DataBean.ListBean) v.getTag();
                        }
                    });
                } else {
                    energy.setVisibility(View.GONE);
                    difficultyLayout.setVisibility(View.GONE);
                    bgaswipe_root.setSwipeAble(false);
                    title.setText("这里有全面有效的训练视频");
                    time.setText("资深教练+详细讲解\n你可以根据自身情况挑选课程");
                    energy.setText("");
                    img.setImageDrawable(getResources().getDrawable(R.drawable.test));
                }

                fm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(dataBean.getId()))
                            TrainingVideoDetailActivity.startAction(mActivity, dataBean.getId(), dataBean.getLogo_url());
                        else
                            AddTraningPlanActivity.startAction(getContext());
                    }
                });
            }

            @Override
            public ViewHolderHelper onCreateViewHolder(ViewGroup parent, int viewType) {
                final ViewHolderHelper viewHolderHelper = super.onCreateViewHolder(parent, viewType);
                BGASwipeItemLayout swipeItemLayout = viewHolderHelper.getView(R.id.sil_item_bgaswipe_root);
                swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
                    @Override
                    public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                        closeOpenedSwipeItemLayoutWithAnim();
                        mOpenedSil.add(swipeItemLayout);
                    }

                    @Override
                    public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
                        mOpenedSil.remove(swipeItemLayout);
                    }

                    @Override
                    public void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout swipeItemLayout) {
                        closeOpenedSwipeItemLayoutWithAnim();
                    }
                });
                return viewHolderHelper;
            }
        };
        ircTran.setAdapter(trainingListAdapter);
        ircTran.setLayoutManager(new LinearLayoutManager(getContext()));
        View head = LayoutInflater.from(getContext()).inflate(R.layout.layout_headerview_train, null);
        View foot = LayoutInflater.from(getContext()).inflate(R.layout.layout_footbutton_train, null);
        idHomeDataLeiji = (TextView) head.findViewById(R.id.id_home_data_leiji);
        line_tonjii = (LinearLayout) head.findViewById(R.id.line_tonjii);

        idHomeDataWancheng = (TextView) head.findViewById(R.id.id_home_data_wancheng);
        idHomeDataLeijiDays = (TextView) head.findViewById(R.id.id_home_data_leiji_days);
        idHomeDataQianka = (TextView) head.findViewById(R.id.id_home_data_qianka);

        ircTran.addHeaderView(head);
        ircTran.addFooterView(foot);
        ircTran.setOnRefreshListener(this);

        foot.findViewById(R.id.id_add_vedio_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTraningPlanActivity.startAction(getContext());
            }
        });
        head.findViewById(R.id.ll_statistic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityToTrainData();
            }
        });
        line_tonjii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityToTrainData();
            }
        });

        Log.e("husong", "----------->>> "
                 + "  uid=" + UserCenter.getUid() + "  " + AppUtil.getVersionName(AppApplication.getContext()) +
                "  " + AppConstant.OS+"  imei="+ ExampleUtil.getImei(mActivity));
        //数据为空才重新发起请求
        if (trainingListAdapter.getSize() <= 0 && !("").equals(UserCenter.getUid())) {
            //发起请求
            mPresenter.getUserHomeDataRequest(
                    JMClassUser.MyJMClass());
        } else {
            UserHomeData.DataBean.ListBean l = new UserHomeData.DataBean.ListBean();
            trainingListAdapter.add(l);
        }

        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainDataChartViewActivity.startAction(getContext());
            }
        });

        idTrainingRightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到另外页面
//                FinishTrainingActivity.startAction(getActivity());
            }
        });

        ircTran.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    closeOpenedSwipeItemLayoutWithAnim();
                }
            }
        });

        // 登录
        mRxManager.on(strLogin, new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("husong", "------------>>>login onRefresh "+UserCenter.getUid());
                if (!TextUtils.isEmpty(UserCenter.getUid())) {
                    //发起请求
                    mPresenter.getUserHomeDataRequest(
                            JMClassUser.MyJMClass());
                }
            }
        });

        // 退出
        mRxManager.on(strLogout, new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("husong", "------------>>>loginout onRefresh");
                idHomeDataLeiji.setText("0");
                idHomeDataWancheng.setText("0");
                idHomeDataLeijiDays.setText("0");
                idHomeDataQianka.setText("0");

                trainingListAdapter.clear();
                if (trainingListAdapter.getItemCount() == 0) {
                    UserHomeData.DataBean.ListBean l = new UserHomeData.DataBean.ListBean();
                    trainingListAdapter.add(l);
                }
                onRefresh();
            }
        });

        //添加了新的项目
        mRxManager.on(TrainingVideoDetailActivity.TAG, new Action1<String>() {
            @Override
            public void call(String postId) {
                onRefresh();
            }
        });
    }

    /**
     * 跳转到统计界面
     */
    private void startActivityToTrainData() {
        if (LoginUtil.isLogin(mActivity)) {
            TrainDataChartViewActivity.startAction(mActivity);
        }
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        //发起请求
        ircTran.setRefreshing(true);
        mPresenter.getUserHomeDataRequest(JMClassUser.MyJMClass());
//        mPresenter.getUserHomeDataRequest(JMClassUser.MyJMClass("13", AppConstant.OS, AppConstant.VERSION_NUM, ));
    }

    @Override
    public void showLoading(String title) {
        if (trainingListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    //停止刷新
    @Override
    public void stopLoading() {
        ircTran.setRefreshing(false);
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);

        if (trainingListAdapter.getItemCount() == 0) {
            UserHomeData.DataBean.ListBean l = new UserHomeData.DataBean.ListBean();
            trainingListAdapter.add(l);
        }
    }

    //请求失败后显示错误界面
    @Override
    public void showErrorTip(String msg) {
        Log.e("husong","----->>>showErrorTip = "+msg);
        if (trainingListAdapter.getPageBean().isRefresh()) {
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
        }
    }

    //请求回调
    @Override
    public void returnUserHomeDataListData(UserHomeData.DataBean homeDataSummaries) {
        Log.e("husong","----->>>returnUserHomeDataListData = "+homeDataSummaries.getEnergy());
        if (homeDataSummaries != null) {

            Double b = Double.valueOf(homeDataSummaries.getTimes());
            if (b < 60) {
                idHomeDataLeiji.setText(("" + b / 60).substring(0, 3));
            } else {
                idHomeDataLeiji.setText("" + (int) (b / 60));
            }
            idHomeDataWancheng.setText(homeDataSummaries.getNum());
            idHomeDataLeijiDays.setText(homeDataSummaries.getDay());
            idHomeDataQianka.setText(homeDataSummaries.getEnergy());
            List<UserHomeData.DataBean.ListBean> listBeans = homeDataSummaries.getList();
            if (trainingListAdapter.getPageBean().isRefresh()) {
                ircTran.setRefreshing(false);
                trainingListAdapter.replaceAll(listBeans);
            } else {
                if (listBeans.size() > 0) {
                    ircTran.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    trainingListAdapter.addAll(listBeans);
                } else {
                    ircTran.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
            saveTrainId(listBeans);
        }
    }

    /**
     * 保存 已经添加的训练ID
     */
    private void saveTrainId(List<UserHomeData.DataBean.ListBean> listBeans) {
        List<String> idList = new ArrayList<>();
        int listSize = listBeans.size();
        for (int index = 0; index < listSize; index++) {
            idList.add(listBeans.get(index).getId());
        }
        ACache aCache = ACache.get(mActivity);
        //存
        aCache.put(UserCenter.getUid(), (Serializable) idList);


    }

    /**
     * 移除
     *
     * @param deleteDataBean
     */
    private void reMoveTrainId(UserHomeData.DataBean.ListBean deleteDataBean) {
        ACache aCache = ACache.get(mActivity);
        List<String> idList = (List<String>) aCache.getAsObject(UserCenter.getUid());
        int listSize = idList.size();
        int deletePosition = -1;
        for (int index = 0; index < listSize; index++) {
            if (deleteDataBean.getId().equals(idList.get(index))) {
                deletePosition = index;
            }
        }
        if (deletePosition != -1) {
            idList.remove(deletePosition);
        }
        aCache.put(UserCenter.getUid(), (Serializable) idList);
    }

    @Override
    public void returnDeleteHomesRequestData(HomesDelete.DataBean homeDataSummaries) {
        stopProgressDialog();
        if (homeDataSummaries == null) return;

        if ("1".equals(homeDataSummaries.getStatus())) {
            try {
                trainingListAdapter.remove(deleteDataBean);
                reMoveTrainId(deleteDataBean);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ToastUtil.showShort("移除失败");
        }
        if (trainingListAdapter.getItemCount() == 0) {
            UserHomeData.DataBean.ListBean l = new UserHomeData.DataBean.ListBean();
            trainingListAdapter.add(l);
        }
    }




}
