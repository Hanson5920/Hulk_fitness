package com.gz.hkjs.app.ui.detail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.app.AppConstant;
import com.gz.hkjs.app.bean.CollectAddData;
import com.gz.hkjs.app.bean.CollectDeleteData;
import com.gz.hkjs.app.bean.RecipesDetail;
import com.gz.hkjs.app.bean.RecipesSummary;
import com.gz.hkjs.app.parameter.JMClassDetail;
import com.gz.hkjs.app.ui.detail.contract.RecipesDetailContract;
import com.gz.hkjs.app.ui.detail.model.RecipesDetailModel;
import com.gz.hkjs.app.ui.detail.presenter.RecipesDetailPresenter;
import com.gz.hkjs.app.ui.main.activity.LoginActivity;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.baserx.RxSchedulers;
import com.jaydenxiao.common.commonutils.ToastUtil;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;

/**
 * * 食谱
 * Created by Administrator on 2017/3/16.
 */

public class RecipesDetailActivity extends BaseActivity<RecipesDetailPresenter, RecipesDetailModel> implements RecipesDetailContract.View {

    public final static String TAG = "RecipesDetailActivity";

    @BindView(R.id.news_detail_photo_iv)
    ImageView newsDetailPhotoIv;
    @BindView(R.id.mask_view)
    View maskView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.id_item_content_recipes_detail_recilist)
    LinearLayout mLyDetailRecilist;
    @BindView(R.id.id_item_content_recipes_detail_steps)
    LinearLayout mLyDetailSteps;
    @BindView(R.id.id_include_cook_stepnum)
    TextView idIncludeCookStepnum;
    @BindView(R.id.id_item_content_recipes_detail_level)
    TextView mRecipesDetailLevel;
    @BindView(R.id.id_item_content_recipes_detail_ctime)
    TextView mRecipesDetailCtime;
    @BindView(R.id.id_item_content_recipes_detail_energy)
    TextView mRecipesDetailEnergy;
    @BindView(R.id.image_collect)
    ImageView imageCollect;
    @BindView(R.id.ll_food_tag)
    LinearLayout llFoodTag;


    private String postId;
    private String mNewsTitle;
    private String mEnergy;
    private ArrayList<RecipesSummary.DataBean.FoodTagsBean> foodTags;
    private String ctime;
    private String exaggerate;
    private Boolean isOnclickCollect = false;
    private String uid = "";


    /**
     * 入口
     *
     * @param
     * @param mContext
     * @param postId
     * @param foodTags
     */
    public static void startAction(Context mContext, View view, String postId, String imgUrl
            , ArrayList<RecipesSummary.DataBean.FoodTagsBean> foodTags) {
        Intent intent = new Intent(mContext, RecipesDetailActivity.class);
        intent.putExtra(AppConstant.NEWS_POST_ID, postId);
        intent.putExtra(AppConstant.NEWS_IMG_RES, imgUrl);
        intent.putExtra(AppConstant.FOOD_IMG_TAG, foodTags);
        mContext.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recipes_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    @Override
    public void initView() {
        SetTranslanteBar();
        foodTags = (ArrayList<RecipesSummary.DataBean.FoodTagsBean>) getIntent()
                .getSerializableExtra(AppConstant.FOOD_IMG_TAG);

        System.out.println("foodTags====>" + foodTags.size());
        postId = getIntent().getStringExtra(AppConstant.NEWS_POST_ID);
        mPresenter.getRecipesDetailDataRequest(JMClassDetail.MyJMClass(postId));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        imageCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
                if (uid.equals("")) {//未登录
                    startActivity(new Intent(RecipesDetailActivity.this, LoginActivity.class));
                }
                if (!uid.equals("")) {//登录
                    isOnclickCollect = true;
                    v.setSelected(!v.isSelected());
                    if (v.isSelected()) {
                        HashMap<String, String> map = JMClassDetail.MyJMClass(postId);
                        map.put("type", "1");

                        //第二种
                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, String> entry = it.next();
                        }
                        mPresenter.collectionAdd(map);
                        ToastUtil.showShort("收藏成功");

                    } else {

                        HashMap<String, String> map = JMClassDetail.MyJMClass(postId);
                        map.put("type", "1");

                        //第二种
                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, String> entry = it.next();
                        }
                        mPresenter.collectionDelete(map);
                        ToastUtil.showShort("取消收藏");
                    }
                    v.setEnabled(false);

                }
            }


        });
        setRecipesListTag(foodTags);
    }

    @Override
    public void returnRecipesDetailData(RecipesDetail.DataBean recipesDetail) {
        idIncludeCookStepnum.setText("(" + recipesDetail.getStep().size() + "步)");
        mNewsTitle = recipesDetail.getName();
        ctime = recipesDetail.getUse_time();
        exaggerate = recipesDetail.getExaggerate();
        mEnergy = recipesDetail.getEnergy();


        if ("0".equals(exaggerate)) {
            mRecipesDetailLevel.setText("初级");
        } else if ("1".equals(exaggerate)) {
            mRecipesDetailLevel.setText("中级");
        } else {
            mRecipesDetailLevel.setText("高级");
        }

        mRecipesDetailCtime.setText(ctime + "分钟");
        mRecipesDetailEnergy.setText(mEnergy + "千卡");
        setNewsDetailPhotoIv(recipesDetail.getLogo_url());
        setToolBarLayout(mNewsTitle);
        setNewsDetailBodyTv(recipesDetail);

        if (recipesDetail.getIs_collection() == 2) {
            imageCollect.setSelected(true);
        } else {
            imageCollect.setSelected(false);
        }
    }

    @Override
    public void returnCollectAddData(CollectAddData.DataBean collectListData) {
        imageCollect.setEnabled(true);
    }

    @Override
    public void returnDeleteAddData(CollectDeleteData.DataBean collectListData) {
        imageCollect.setEnabled(true);
    }

    private void setNewsDetailPhotoIv(String imgSrc) {
        Glide.with(this).load(imgSrc)
                .fitCenter()
                .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                .crossFade().into(newsDetailPhotoIv);
    }

    private void setToolBarLayout(String newsTitle) {
        toolbarLayout.setTitle(newsTitle);
        toolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        toolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.size_black_three));
    }

    private void setNewsDetailBodyTv(final RecipesDetail.DataBean recipesDetail) {
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
                        setRecipesListBody(recipesDetail.getIngredients());
                        setRecipesStepsBody(recipesDetail.getStep());


                    }
                }));
    }


    private void setRecipesListTag(List<RecipesSummary.DataBean.FoodTagsBean> recipesListTag) {
        for (int i = 0; i < recipesListTag.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.include_food_tag, null);
            TextView tv1 = (TextView) view.findViewById(R.id.tv_food_tag);
            tv1.setText(recipesListTag.get(i).getId());
            tv1.setText(recipesListTag.get(i).getName());
            llFoodTag.addView(view);
        }
    }

    private void setRecipesListBody(List<RecipesDetail.DataBean.IngredientsBean> ingredients) {
        for (int i = 0; i < ingredients.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.include_reci_list, null);
            TextView tv1 = (TextView) view.findViewById(R.id.id_include_recilist_food);
            TextView tv2 = (TextView) view.findViewById(R.id.id_include_recilist_size);
            View line = view.findViewById(R.id.id_include_recilist_line);

            tv1.setText(ingredients.get(i).getName());
            tv2.setText(ingredients.get(i).getNum());


            if (i == ingredients.size() - 1) {
                line.setVisibility(View.INVISIBLE);
            }
            mLyDetailRecilist.addView(view);
        }
    }

    private void setRecipesStepsBody(List<RecipesDetail.DataBean.StepBean> stepBean) {
        for (int i = 0; i < stepBean.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.include_cook_step, null);
            TextView step = (TextView) view.findViewById(R.id.id_include_cook_step);
            TextView content = (TextView) view.findViewById(R.id.id_include_cook_content);
            ImageView img = (ImageView) view.findViewById(R.id.id_include_cook_img);
            View line = view.findViewById(R.id.id_include_cook_line);

            step.setText("步骤 " + stepBean.get(i).getPx_num() + ":");
            content.setText(stepBean.get(i).getDsc());
            Glide.with(this).load(stepBean.get(i).getLogo_url())
                    .fitCenter()
                    .centerCrop()
                    .error(com.jaydenxiao.common.R.drawable.ic_empty_picture)
                    .crossFade().into(img);

            if (i == stepBean.size() - 1) {
                line.setVisibility(View.INVISIBLE);
            }
            mLyDetailSteps.addView(view);
        }
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
        if (isOnclickCollect && !imageCollect.isSelected()) {
            mRxManager.post(TAG, postId);
        }
        super.onDestroy();
    }
}
