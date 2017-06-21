package com.gz.hkjs.app.ui.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.app.AppConstant;
import com.gz.hkjs.app.bean.CollectAddData;
import com.gz.hkjs.app.bean.CollectDeleteData;
import com.gz.hkjs.app.bean.FindDetail;
import com.gz.hkjs.app.parameter.JMClassDetail;
import com.gz.hkjs.app.ui.detail.contract.FindDetailContract;
import com.gz.hkjs.app.ui.detail.model.FindsDetailModel;
import com.gz.hkjs.app.ui.detail.presenter.FindsDetailPresenter;
import com.gz.hkjs.app.ui.main.activity.LoginActivity;
import com.gz.hkjs.app.widget.Util;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/3/16.
 */

public class FindDetailActivity extends BaseActivity<FindsDetailPresenter, FindsDetailModel> implements FindDetailContract.View {

    public final static String TAG = "FindDetailActivity";

//    @BindView(R.id.news_detail_photo_iv)
//    ImageView newsDetailPhotoIv;
//    @BindView(R.id.mask_view)
//    View maskView;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.toolbar_layout)
//    CollapsingToolbarLayout toolbarLayout;
//    @BindView(R.id.app_bar)
//    AppBarLayout appBar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.id_webview)
    WebView idWebview;
//    @BindView(R.id.id_item_find_ctime)
//    TextView idItemFindCtime;
//    @BindView(R.id.id_item_find_title)
//    TextView idItemFindTitle;
    @BindView(R.id.bottom_layout)
    RelativeLayout mBottomLayout;
    @BindView(R.id.nestedscrollview)
    NestedScrollView mNestedScrollview;
    @BindView(R.id.collect)
    ImageView imgCollect;

//    @BindView(R.id.image_collect)
//    ImageView imageCollect;

    private String uid = "";
    private String postId;
    private String mNewsTitle;
    private Boolean isCollect = false;
    private boolean bottomVisible = false;

    /**
     * 入口
     *
     * @param mContext
     * @param postId
     */
    public static void startAction(Context mContext, View view, String postId, String imgUrl) {
        Intent intent = new Intent(mContext, FindDetailActivity.class);
        intent.putExtra(AppConstant.NEWS_POST_ID, postId);
        intent.putExtra(AppConstant.NEWS_IMG_RES, imgUrl);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_find_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        SetTranslanteBar();
        postId = getIntent().getStringExtra(AppConstant.NEWS_POST_ID);
        mPresenter.getOneFindsDataRequest(JMClassDetail.MyJMClass(postId));
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    finishAfterTransition();
//                } else {
//                    finish();
//                }
//            }
//        });

//        imageCollect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
//                if (uid.equals("")) {//未登录
//                    startActivity(new Intent(FindDetailActivity.this, LoginActivity.class));
//                } else {
//
//                    isOnclickCollect = true;
//                    v.setSelected(!v.isSelected());
//                    if (v.isSelected()) {
//                        HashMap<String, String> map = JMClassDetail.MyJMClass(postId);
//                        map.put("type", "2");
//
//                        //第二种
//                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//                        while (it.hasNext()) {
//                            Map.Entry<String, String> entry = it.next();
//                        }
//                        mPresenter.collectionAdd(map);
//                        ToastUtil.showShort("收藏成功");
//                    } else {
//
//                        HashMap<String, String> map = JMClassDetail.MyJMClass(postId);
//                        map.put("type", "2");
//
//                        //第二种
//                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
//                        while (it.hasNext()) {
//                            Map.Entry<String, String> entry = it.next();
//                        }
//                        mPresenter.collectionDelete(map);
//                        ToastUtil.showShort("取消收藏");
//                    }
//                    v.setEnabled(false);
//                }
//
//            }
//        });
        initScrollView();
    }

    @OnClick({R.id.to_top, R.id.collect, R.id.back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.to_top:
                mNestedScrollview.scrollTo(0, 0);
                break;

            case R.id.collect:
                uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
                if (uid.equals("")) {//未登录
                    startActivity(new Intent(FindDetailActivity.this, LoginActivity.class));
                } else {
                    if (!isCollect) {
                        HashMap<String, String> map = JMClassDetail.MyJMClass(postId);
                        map.put("type", "2");

                        //第二种
                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, String> entry = it.next();
                        }
                        mPresenter.collectionAdd(map);
                        isCollect = true;
                        imgCollect.setImageResource(R.mipmap.home_discover_btn_collect_s);
                        ToastUtil.showShort("收藏成功");
                    } else {
                        HashMap<String, String> map = JMClassDetail.MyJMClass(postId);
                        map.put("type", "2");

                        //第二种
                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, String> entry = it.next();
                        }
                        mPresenter.collectionDelete(map);
                        isCollect = false;
                        imgCollect.setImageResource(R.mipmap.home_discover_btn_collect);
                        ToastUtil.showShort("取消收藏");
                    }
//                    v.setEnabled(false);
                }
                break;

            case R.id.back:
                finish();
                break;
        }
    }

    private void initScrollView(){
        mNestedScrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(!bottomVisible && scrollY > 200)
                    showBottomLayout();

                if(scrollY==0 && bottomVisible)
                    hideBottomLayout();
            }
        });
    }

    private void showBottomLayout(){
        bottomVisible = true;
        Animation inAinmation = AnimationUtils.loadAnimation(mContext, R.anim.in_downorup);
        mBottomLayout.setVisibility(View.VISIBLE);
        mBottomLayout.startAnimation(inAinmation);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT);
//        lp.leftMargin = Util.dip2px(mContext, 10f);
//        lp.rightMargin = Util.dip2px(mContext, 10f);
        lp.bottomMargin = Util.dip2px(mContext, 40f);
        mNestedScrollview.setLayoutParams(lp);
    }

    private void hideBottomLayout(){
        bottomVisible = false;
        Animation outAinmation = AnimationUtils.loadAnimation(mContext, R.anim.out_uptodown);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT);
//        lp.leftMargin = Util.dip2px(mContext, 10f);
//        lp.rightMargin = Util.dip2px(mContext, 10f);
        lp.bottomMargin = Util.dip2px(mContext, 0f);
        mNestedScrollview.setLayoutParams(lp);
        outAinmation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBottomLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBottomLayout.startAnimation(outAinmation);
    }

    @Override
    public void returnOneFindData(final FindDetail.DataBean findDetail) {
//        mNewsTitle = findDetail.getTitle();
//        String newsTime = TimeUtil.formatDate2(findDetail.getCtime());
//        setToolBarLayout(mNewsTitle);
//        setNewsDetailPhotoIv(getIntent().getStringExtra(AppConstant.NEWS_IMG_RES));
//        idItemFindCtime.setText(getResources().getString(R.string.find_detail_time, newsTime));
//        idItemFindTitle.setText(mNewsTitle);
        sinitWebView(idWebview);
        isCollect = (findDetail.getIs_collection() == 2);
        imgCollect.setImageResource(isCollect ? R.mipmap.home_discover_btn_collect_s : R.mipmap.home_discover_btn_collect);

        mRxManager.add(Observable.timer(500, TimeUnit.MILLISECONDS)
                .compose(RxSchedulers.<Long>io_main())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        idWebview.loadData(getHtmlData(findDetail.getContent()), "text/html; charset=UTF-8", null);
                    }
                }));
//        if (findDetail.getIs_collection() == 2) {
//            imageCollect.setSelected(true);
//        } else {
//            imageCollect.setSelected(false);
//        }
    }

    @Override
    public void returnCollectAddData(CollectAddData.DataBean collectListData) {
//        imageCollect.setEnabled(true);
    }

    @Override
    public void returnDeleteAddData(CollectDeleteData.DataBean collectListData) {
//        imageCollect.setEnabled(true);
    }

    private void setToolBarLayout(String newsTitle) {
//        toolbarLayout.setTitle(newsTitle);
//        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
//        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.size_black_three));
    }

    private void setNewsDetailPhotoIv(String imgSrc) {
//        Glide.with(this).load(imgSrc)
//                .fitCenter()
//                .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
//                .crossFade().into(newsDetailPhotoIv);
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{width:100% !important; min-width:100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body><div style='width:100%;over-flow:hidden;'>" + bodyHTML + "</div></body></html>";
    }

    /**
     * 初始化webView
     *
     * @param webView
     */
    private void sinitWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAppCacheEnabled(true);
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
        if (!isCollect) {
            mRxManager.post(TAG, postId);
        }
        super.onDestroy();
    }
}
