package com.gz.hkjs.app.ui.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.TrainVideoDetail;
import com.gz.hkjs.app.parameter.JMClassDetail;
import com.gz.hkjs.app.ui.detail.adapter.TrainDetailAdapter;
import com.gz.hkjs.app.ui.detail.contract.TrainingVideoDetailContract;
import com.gz.hkjs.app.ui.detail.model.TrainVideoDetailModel;
import com.gz.hkjs.app.ui.detail.presenter.TrainVideoDetailPresenter;
import com.gz.hkjs.app.ui.main.activity.TrainStepActivity;
import com.gz.hkjs.app.ui.main.activity.TrainVedioPlayActivity;
import com.gz.hkjs.app.util.LoginUtil;
import com.gz.hkjs.app.util.XUtilNet;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ACache;
import com.jaydenxiao.common.commonutils.DisplayUtil;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonwidget.MoreTextView;
import com.jaydenxiao.common.download.FileDownloadUtil;
import com.liulishuo.filedownloader.FileDownloader;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.orhanobut.logger.Logger;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zrq.spanbuilder.Spans;
import com.zrq.spanbuilder.TextStyle;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

import static com.gz.hkjs.app.parameter.ParameterUtil.getAddTrainMap;
import static com.jaydenxiao.common.download.FileDownloadUtil.createFileName;
import static com.jaydenxiao.common.download.FileDownloadUtil.getFilePathName;
import static com.jaydenxiao.common.download.FileDownloadUtil.isHaveDownLoadGroup;

/**
 * Created by xxq on 2017/4/14.
 * 开始训练页面
 * 视频下载逻辑：点击视频 item ，判断视频是否已经下载，没有下载就去下载，显示现在进度，下载成功就播放
 * <p>
 * 所以视频下载逻辑： 同样先判断视频是否存在，存在就跳转播放界面，不存在就下载再跳转
 * 关于判断视频是否存在的方法：
 * 方法一：根据视频组的名字取文件夹查询是否存在 视频组.txt
 * 方法二：根据所有视频音频下载地址去查询 文件夹是否下载成功，该方法比较稳当，但是较第一种速度慢点，暂时采取这种，后期可以考虑添加数据库来解决
 */

public class TrainingVideoDetailActivity extends BaseActivity<TrainVideoDetailPresenter, TrainVideoDetailModel> implements TrainingVideoDetailContract.View {
    public static final String TAG = "TrainingVideoDetailActivity";
    //常量
    private final static String intent_train_video_Id = "TrainVideoId";//视频ID
    private final static String intent_train_video_ImageUrl = "TrainVideoImageUrl";//视频背景图

    //UI
    @BindView(R.id.id_train_detail_video_list)
    RecyclerView idTrainDetailVideoList;
    @BindView(R.id.tv_train_name)
    TextView tvTrainName;
    @BindView(R.id.tv_Level)
    TextView tvLevel;
    @BindView(R.id.tv_train_time)
    TextView tvTrainTime;
    @BindView(R.id.tv_train_calorie)
    TextView tvTrainCalorie;
    @BindView(R.id.lin_train_introduction)
    MoreTextView linTrainIntroduction;
    @BindView(R.id.tv_training_group_count)
    TextView tvTrainingGroupCount;
    @BindView(R.id.img_train_video)
    ImageView imgTrainVideo;
    @BindView(R.id.pb_train_download)
    ProgressBar pbTrainDownload;//下载进度
    @BindView(R.id.tv_star_train)
    TextView tvStarTrain;
    @BindView(R.id.abl_train_video)
    AppBarLayout ablTrainVideo;
    @BindView(R.id.header_training_detail)
    LinearLayout headLayout;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolBar;


    //Data
    private String trainVideoId;
    private String trainVideoImageUrl;
    private TrainDetailAdapter adapter;
    private List<TrainVideoDetail.DataBean.StepBean> stepList;
    private HashMap<String, String> downloadFileMap;// Map<文件路径，文件下载地址>
    private List<String> downloadFileList;//List<文件下载URL>
    private String fileTxtName;//  比如  下蹲.txt 存URL  和 路径关系的
    private TrainVideoDetail.DataBean returnDataBean;
    private List<TrainVideoDetail.DataBean.VideoListBean> videoList;
    private List<TrainVideoDetail.DataBean.AudioListBean> audioList;
    private ArrayList<String> videoUrlList;//视频URL
    private ArrayList<String> voiceUrlList;//旁白
    private ArrayList<String> bgmUrlList;//背景音乐
    private ArrayList<String> groupVideoUrlList;//分步的视频


    //记录ProgressBar的完成进度
    /**
     * 多任务并行下载，下载线程数默认为3，没有办法实时计算下载进度，使用模拟进度：第一阶段 ———— 第二阶段 ————第三阶段（有可能为2个阶段  匀速------快速）
     * 第一阶段：以默认速度跑到 95%；
     * 第二阶段：如果多任务下载还没有完成，将速度降到很低，基本停止状态，等待下载完成；
     * 第三阶段：在中途下载完成，直接将速度提高，快速跑完剩下的；
     * <p>
     * 注意：下面的速度实际是时间，时间越大，速度越小
     */
    private int stopPercent = 94;
    private int currentProgressStatus = 0;//当前进度
    private int currentSpeed = 0;//当前下载 progressBar 的速度
    private final int defaultSpeed = 800;//默认速度
    private final int stopSpeed = 100000;//停止速度 ,实际并没有停止，只是很慢
    private final int finallySpeed = 5;//下载完成剩余的速度
    private Runnable progressRunnable;


    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext, String trainId, String ImageUrl) {
        Intent intent = new Intent(mContext, TrainingVideoDetailActivity.class);
        intent.putExtra(intent_train_video_Id, trainId);
        intent.putExtra(intent_train_video_ImageUrl, ImageUrl);
        mContext.startActivity(intent);
    }


    MyInnerHandler myHandler = new MyInnerHandler(this);

    static class MyInnerHandler extends Handler {
        WeakReference<TrainingVideoDetailActivity> mFrag;

        MyInnerHandler(TrainingVideoDetailActivity aFragment) {
            mFrag = new WeakReference<>(aFragment);
        }


    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_training_video_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    private void initIntent() {
        trainVideoId = getIntent().getExtras().getString(intent_train_video_Id);
        trainVideoImageUrl = getIntent().getExtras().getString(intent_train_video_ImageUrl);
    }


    @Override
    public void initView() {
        initIntent();
        setSupportActionBar(toolBar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        SetTranslanteBar();
        ImageLoaderUtils.display(this, imgTrainVideo, trainVideoImageUrl);
        stepList = new ArrayList<>();
        adapter = new TrainDetailAdapter(this, stepList);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (groupVideoUrlList != null && groupVideoUrlList.size() != 0 && stepList.get(position).getStep_type().equals("1")) {
                    //TODO
                    Intent intent = new Intent(TrainingVideoDetailActivity.this, TrainStepActivity.class);
                    if (isHaveDownLoadGroup(groupVideoUrlList)) {//分步视频全部下载，传 path
                        intent.putExtra("videoPath", getPathList(groupVideoUrlList));
                    } else {//传URL
                        intent.putExtra("videoUrl", groupVideoUrlList);
                    }
                    intent.putExtra("position", getPosition(stepList.get(position).getVideo_url()));

                    ArrayList<String> ListActions = new ArrayList<String>();
                    for (TrainVideoDetail.DataBean.StepBean sb : stepList){
                        ListActions.add(sb.getDetail());
                    }
                    intent.putExtra("stepactions", ListActions);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        if (stepList.size() <= 0) {
            mPresenter.getTrainVideoDetailDataRequest(JMClassDetail.MyJMClass(trainVideoId));
        }
        idTrainDetailVideoList.setLayoutManager(new LinearLayoutManager(this));
        idTrainDetailVideoList.setAdapter(adapter);

    }

    private int getPosition(String url) {
        String lurl = FileDownloadUtil.createFileName(url);
        for (int i = 0; i < groupVideoUrlList.size(); i++) {
            String gurl = FileDownloadUtil.createFileName(groupVideoUrlList.get(i));
            if (TextUtils.equals(gurl, lurl))
                return i;
        }
        return 0;
    }


    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pauseAll();//暂停所有下载
        myHandler.removeCallbacks(progressRunnable);

    }

    @Override
    protected void onPause() {
        super.onPause();
        FileDownloader.getImpl().pauseAll();//暂停所有下载
        if (myHandler != null && progressRunnable != null) {//即使没有下载完也要暂停
            downloadCompleteState();
        }
    }

    @Override
    public void returnTrainVideoDetailData(TrainVideoDetail.DataBean dataBean) {
        returnDataBean = dataBean;
        List<TrainVideoDetail.DataBean.StepBean> trainGroup = dataBean.getStep();
        if (trainGroup != null) {
            if (trainGroup.size() > 0) {
                List<TrainVideoDetail.DataBean.StepBean> tg = new ArrayList<>();
                for (TrainVideoDetail.DataBean.StepBean sb : trainGroup){
                    if(TextUtils.equals(sb.getStep_type(), "1")){
                        tg.add(sb);
                    }
                }

                stepList.addAll(tg);
                adapter.notifyDataSetChanged();
            }
        }
        int groupNumber = stepList.size();
        //提示文字
        Spans introductionString = Spans.builder()
                .text(dataBean.getDsc()).size(14).color(getResources().getColor(R.color.title_text_color))
                .text("\n\n")
                .text(getString(R.string.train_apparatus)).size(16).style(TextStyle.BOLD).color(getResources().getColor(R.color.title_text_color))
                .text("\n")
                .text(dataBean.getApparatus_name()).size(14).color(getResources().getColor(R.color.gray_text))
                .text("\n\n")
                .text(getString(R.string.train_suggest)).size(16).style(TextStyle.BOLD).color(getResources().getColor(R.color.title_text_color))
                .text("\n")
                .text(dataBean.getProposal()).size(14).color(getResources().getColor(R.color.gray_text))
                .text("\n\n")
                .text(getString(R.string.train_people)).size(16).style(TextStyle.BOLD).color(getResources().getColor(R.color.title_text_color))
                .text("\n")
                .text(dataBean.getSuitable()).size(14).color(getResources().getColor(R.color.gray_text))
                .text("\n\n")
                .text(getString(R.string.train_attention)).size(16).style(TextStyle.BOLD).color(getResources().getColor(R.color.title_text_color))
                .text("\n")
                .text(dataBean.getAttention()).size(14).color(getResources().getColor(R.color.gray_text))
                .build();
        linTrainIntroduction.setText(introductionString);
        fileTxtName = dataBean.getName();
        tvTrainName.setText(dataBean.getName());
        tvLevel.setText(dataBean.getExaggerate_name());
        tvTrainCalorie.setText(dataBean.getEnergy());
        int time = Integer.parseInt(dataBean.getWtime()) / 60;
        tvTrainTime.setText(String.valueOf(time));
        tvTrainingGroupCount.setText(String.format(getString(R.string.group_number), groupNumber));

        ablTrainVideo.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -headLayout.getHeight() / 3) {
                    collapsingToolbarLayout.setTitle(fileTxtName);
                    collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(TrainingVideoDetailActivity.this, R.color.white));
                    collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(TrainingVideoDetailActivity.this, R.color.size_black_three));
                } else {
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });


        videoList = dataBean.getVideo_list();
        audioList = dataBean.getAudio_list();
        int videoSize = videoList.size();
        int audioSize = audioList.size();

        downloadFileMap = new HashMap<>();
        downloadFileList = new ArrayList<>();
        videoUrlList = new ArrayList<>();
        voiceUrlList = new ArrayList<>();
        bgmUrlList = new ArrayList<>();
        groupVideoUrlList = new ArrayList<>();

        //视频组
        for (int index1 = 0; index1 < videoSize; index1++) {
            String videoUrl = videoList.get(index1).getVideo_url();
            String audioUrl = videoList.get(index1).getAudio_url();
            if (!TextUtils.isEmpty(videoUrl)) {
                videoUrlList.add(videoUrl);
                downloadFileMap.put(createFileName(videoUrl), videoUrl);
                downloadFileList.add(videoUrl);
            }
            if (!TextUtils.isEmpty(audioUrl)) {
                bgmUrlList.add(audioUrl);
                downloadFileMap.put(createFileName(audioUrl), audioUrl);
                downloadFileList.add(audioUrl);
            }
        }

        //旁白组
        for (int index2 = 0; index2 < audioSize; index2++) {
            String audioUrl = audioList.get(index2).getAudio_url();
            if (!TextUtils.isEmpty(audioUrl)) {
                voiceUrlList.add(audioUrl);
                downloadFileMap.put(createFileName(audioUrl), audioUrl);
                downloadFileList.add(audioUrl);
            }
        }

        //几个步凑中的视频
        for (int index3 = 0; index3 < groupNumber; index3++) {
            String videoUrl = stepList.get(index3).getVideo_url();
            if (!TextUtils.isEmpty(videoUrl)) {
                groupVideoUrlList.add(videoUrl);
                downloadFileMap.put(createFileName(videoUrl), videoUrl);
                downloadFileList.add(videoUrl);
            }
        }
    }

    /**
     * 下载完成
     *
     * @param pathHashMap
     */
    @Override
    public void returnVideoData(HashMap<String, String> pathHashMap) {
        Logger.i("下载完成回调");
        if (currentSpeed == stopSpeed) {//到达了96%，直接设置为100
            downloadCompleteState();
        } else {//还没有到达96%
            currentSpeed = finallySpeed;
        }
    }

    @Override
    public void returnAddTrain(String state) {
        if (state.equals("1")) {
            ACache aCache = ACache.get(this);
            List<String> idList = (List<String>) aCache.getAsObject(UserCenter.getUid());
            idList.add(trainVideoId);
            aCache.put(UserCenter.getUid(), (Serializable) idList);
            mRxManager.post("TrainingVideoDetailActivity", "update");
        }

    }

    /**
     * 下载完成后设置相关状态
     */
    private void downloadCompleteState() {
        myHandler.removeCallbacks(progressRunnable);
        pbTrainDownload.setProgress(0);
        tvStarTrain.setText(R.string.start_train);
        Drawable drawable = getResources().getDrawable(R.drawable.home_ico_time);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvStarTrain.setCompoundDrawables(drawable, null, null, null);
        tvStarTrain.setCompoundDrawablePadding(DisplayUtil.dip2px(10));
        currentProgressStatus = 0;
//        startActivityToVideo();
    }


    @OnClick({R.id.pb_train_download})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pb_train_download:
                if (!LoginUtil.isLogin(this)) {
                    return;
                }
                if (downloadFileList == null || downloadFileList.size() == 0) {
                    return;
                }
                addTrainId();
                if (isHaveDownLoadGroup(downloadFileList)) {//已经下载
                    startActivityToVideo();
                } else {
                    prepareDownload();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 添加  训练ID
     */
    private void addTrainId() {
        ACache aCache = ACache.get(this);
        List<String> idList = (List<String>) aCache.getAsObject(UserCenter.getUid());//已经添加 训练项目的集合
        if (idList == null || idList.size() == 0) {
            return;
        }
        int listSize = idList.size();
        for (int index = 0; index < listSize; index++) {
            if (trainVideoId.equals(idList.get(index))) {
                return;
            }
        }
        mPresenter.addTrainRequest(getAddTrainMap(trainVideoId));//添加项目
    }

    /**
     * 准备下载
     */
    private void prepareDownload() {
        if (XUtilNet.isWifiConnected()) {
            startDownload();
        } else if (!XUtilNet.isWifiConnected() && XUtilNet.isNetConnected()) {//有网络但不是WIFI
            if (!DefaultSharePrefManager.getBoolean("wifi", false)) {//不需要WIFI也可以下载
                //添加提示语，表示当前不是wifi ,下载要流量
                final MaterialDialog dialog = new MaterialDialog(this);
                dialog.setMessage(R.string.flow_hint)
                        .setPositiveButton("继续下载", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                startDownload();
                            }
                        })
                        .setNegativeButton("取消下载", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        }).show();

            } else {//需要WIFI  才能下载，需要提示设置WIFI
                showWifiDialog();
            }
        }
    }

    /**
     * 下载文件
     */
    private void startDownload() {
        if (currentProgressStatus == 0) {
            mPresenter.getDownloadVideoRequest(fileTxtName, downloadFileMap);
            startProgress();
        }
    }

    private void showWifiDialog() {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setMessage(R.string.no_wifi_hint);
        materialDialog.setPositiveButton(R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();
    }


    /**
     * 模拟一个进度
     */
    private void startProgress() {
        if (currentProgressStatus != 0 && currentProgressStatus != 100) {
            return;
        }

        //初始
        currentProgressStatus = 0;
        currentSpeed = defaultSpeed;

        progressRunnable = new Runnable() {
            @Override
            public void run() {
                //获取耗时操作的完成百分比,到了 94 就开始减速，显示会从95减速
                if (currentProgressStatus == stopPercent && currentSpeed == defaultSpeed) {
                    currentSpeed = stopSpeed;
                }
                currentProgressStatus++;
                if (currentProgressStatus < 100) {
                    myHandler.postDelayed(this, currentSpeed);
                } else {
                    myHandler.removeCallbacks(this);
                }
                setProgressState(currentProgressStatus);
            }
        };
        myHandler.postDelayed(progressRunnable, currentSpeed);
    }

    private void setProgressState(int progress) {
        if (progress < 100) {//下载完成
            pbTrainDownload.setProgress(progress);
            String tvStatus = getResources().getString(R.string.have_download);
            tvStatus = String.format(tvStatus, progress, "%");
            tvStarTrain.setText(tvStatus);
            tvStarTrain.setCompoundDrawables(null, null, null, null);
        } else if (progress == 100) {
            downloadCompleteState();
        }


    }


    /**
     * 跳转到播放视频界面
     */
    private void startActivityToVideo() {
        //视频
        ArrayList<String> videoPathList = getPathList(videoUrlList);
        //旁白
        ArrayList<String> voicePathList = getPathList(voiceUrlList);
        //背景音乐
        ArrayList<String> bgmPathList = getPathList(bgmUrlList);
        //分步视频
        ArrayList<String> groupVideoPathList = getPathList(groupVideoUrlList);

        Intent intent = new Intent(this, TrainVedioPlayActivity.class);
        intent.putExtra("video_urls", videoPathList);//视频
        intent.putExtra("audio_urls", voicePathList);//旁白
        intent.putExtra("bgm_urls", bgmPathList);//背景音乐
        intent.putExtra("groupVideo_urls", groupVideoPathList);//分步音乐
        if (returnDataBean != null)
            intent.putExtra("trainingVideo", returnDataBean);
        startActivity(intent);
    }

    /**
     * URL 得到 path
     *
     * @param urlList
     * @return
     */
    private ArrayList<String> getPathList(List<String> urlList) {
        ArrayList<String> pathList = new ArrayList<>();
        int urlListSize = urlList.size();
        for (int index = 0; index < urlListSize; index++) {
            String bgmUrl = urlList.get(index);
            pathList.add(getFilePathName(createFileName(bgmUrl)));
        }
        return pathList;
    }


}
