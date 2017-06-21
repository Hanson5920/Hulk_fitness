package com.gz.hkjs.app.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.gz.hkjs.app.R;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.MineTitleBar;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xxq on 2016/10/19 0019.
 * 简单的公共的WebActivity,根据传入的值来显示
 */

public class PublicWebActivity extends BaseActivity {
    //ui


    //进度条
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.mtb_mine_xieyi)
    MineTitleBar mtbMineXieyi;
    @BindView(R.id.ll_web_content)
    LinearLayout llWebContent;

    private WebView mWebView;

    public PublicWebActivity() {
    }


    //Data
    private Intent intent;
    private String titleString = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        mtbMineXieyi.setLeftBack(true);
        mtbMineXieyi.setOnLeftImagListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mtbMineXieyi.setTitleText(getString(R.string.my_treaty));
        mtbMineXieyi.setTitleVisibility(true);
        initIntentData();
        initTitle();
        initData();
    }


    private void initTitle() {
        setTitle(titleString);
//        setImg_themeVisible(View.GONE);

    }

    private void initIntentData() {
        intent = getIntent();
        titleString = intent.getStringExtra("title");
    }

    /**
     * 通过头部去请求URL显示在web上面
     */
    private void initData() {

//        if (titleString.equals("用户协议")) {
        String url = "http://tapi.jianshen.1122.com/xieyi.html";
        intWeb(url);
//        }
//        else if (titleString.equals("广告栏")) {
//            String url = bundle.getString("head_url");
//            Logger.i(" url= " + url);
//            intWeb(url);
//        }
    }

    private void intWeb(final String URL) {
        Logger.i("URL=" + URL);
        progressBar.setMax(100);
        mWebView = new WebView(this);
        llWebContent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
//                TbsLog.d(TAG, "title: " + title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
//                Logger.i("cq====当前的进度=========" + newProgress);
                progressBar.setProgress(newProgress);
                if (progressBar != null && newProgress != 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);
        // long time = System.currentTimeMillis();

        if (mWebView != null && mWebView.getParent() == llWebContent) {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(URL);
                    mWebView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return super.shouldOverrideUrlLoading(view, url);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {//WebView 窗口的destroy
        // TODO Auto-generated method stub
        if (this.mWebView != null) {
            mWebView.getSettings().setBuiltInZoomControls(false);
            llWebContent.removeAllViews();
            if (mWebView != null) {
                mWebView.clearHistory();
                mWebView.clearCache(true);
                mWebView.loadUrl("about:blank");
                mWebView.freeMemory();
                mWebView.pauseTimers();
                mWebView.destroy();//- -
                mWebView = null;
            }
        }
        super.onDestroy();
    }
}
