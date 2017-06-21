package com.gz.hkjs.app.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.MultiItemRecycleViewAdapter;
import com.aspsine.irecyclerview.universaladapter.recyclerview.MultiItemTypeSupport;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.app.AppApplication;
import com.gz.hkjs.app.bean.FindSummary;
import com.gz.hkjs.app.ui.detail.activity.FindDetailActivity;
import com.jaydenxiao.common.commonutils.DisplayUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class FindListAdapter extends MultiItemRecycleViewAdapter<FindSummary.DataBean> {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_PHOTO_ITEM = 1;
    public static final int TYPE_PHOTO_ITEM_RIGHT = 2;
    public static final int TYPE_PHOTO_ITEM_TOP = 3;

    public FindListAdapter(Context context, final List<FindSummary.DataBean> datas) {
        super(context, datas, new MultiItemTypeSupport<FindSummary.DataBean>() {
            @Override
            public int getItemViewType(int position, FindSummary.DataBean dataBean) {
                if ("105".equals(dataBean.getShow_type()) || "106".equals(dataBean.getShow_type())) {
                    return TYPE_PHOTO_ITEM;
                } else if ("104".equals(dataBean.getShow_type()) || "103".equals(dataBean.getShow_type())) {
                    return TYPE_PHOTO_ITEM_RIGHT;
                } else if ("107".equals(dataBean.getShow_type())) {
                    return TYPE_PHOTO_ITEM_TOP;
                }
                return TYPE_ITEM;
            }

            @Override
            public int getLayoutId(int type) {
                if (type == TYPE_PHOTO_ITEM) {
                    return R.layout.item_finds_photo;
                } else if (type == TYPE_PHOTO_ITEM_RIGHT) {
                    return R.layout.item_finds_pic_right;
                } else if (type == TYPE_PHOTO_ITEM_TOP) {
                    return R.layout.item_finds_photo_top;
                }
                return R.layout.item_finds;
            }
        });
    }

    @Override
    public void convert(ViewHolderHelper holder, FindSummary.DataBean findSummary) {
        switch (holder.getLayoutId()) {
            case R.layout.item_finds:
                setItemValues(holder, findSummary, getPosition(holder));
                break;
            case R.layout.item_finds_photo:
                setPhotoItemValues(holder, findSummary, getPosition(holder));
                break;
            case R.layout.item_finds_pic_right:
                setItemValues(holder, findSummary, getPosition(holder));
                break;
            case R.layout.item_finds_photo_top:
                setPhotoItemValues(holder, findSummary, getPosition(holder));
                break;
        }
    }

    /**
     * 普通样式
     *
     * @param holder
     * @param findSummary
     * @param position
     */
    private void setItemValues(final ViewHolderHelper holder, final FindSummary.DataBean findSummary, final int position) {
        String title = findSummary.getTitle();
        if (title == null) {
            title = findSummary.getTitle();
        }
        String ptime = findSummary.getCtime();
        String digest = findSummary.getTitle();
        String imgSrc = findSummary.getImg_url();

        holder.setText(R.id.news_summary_title_tv, title);
//        holder.setText(R.id.news_summary_ptime_tv, ptime);
        holder.setText(R.id.news_summary_digest_tv, ptime);
        holder.setImageUrl(R.id.news_summary_photo_iv, imgSrc);
        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindDetailActivity.startAction(mContext, holder.getView(R.id.news_summary_photo_iv), findSummary.getId(), findSummary.getImg_url());
            }
        });
    }

    /**
     * 图文样式
     *
     * @param holder
     * @param position
     */
    private void setPhotoItemValues(final ViewHolderHelper holder, final FindSummary.DataBean findSummary, int position) {
        String title = findSummary.getTitle();
        String ptime = findSummary.getCtime();

        holder.setText(R.id.news_summary_title_tv, title);
        setImageView(holder, findSummary);
        holder.setOnClickListener(R.id.ll_root, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FindDetailActivity.startAction(mContext, holder.getView(R.id.news_summary_photo_iv_group), findSummary.getId(), findSummary.getContent_logo_url());
            }
        });
    }

    private void setImageView(ViewHolderHelper holder, FindSummary.DataBean findSummary) {
        int PhotoThreeHeight = (int) DisplayUtil.dip2px(90);
        int PhotoTwoHeight = (int) DisplayUtil.dip2px(120);
        int PhotoOneHeight = (int) DisplayUtil.dip2px(150);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;
        LinearLayout news_summary_photo_iv_group = holder.getView(R.id.news_summary_photo_iv_group);
        ViewGroup.LayoutParams layoutParams = news_summary_photo_iv_group.getLayoutParams();

        if (findSummary.getImg_arr() != null) {
            int size = findSummary.getImg_arr().size();
            if (size >= 3) {
                imgSrcLeft = (String) findSummary.getImg_arr().get(0);
                imgSrcMiddle = (String) findSummary.getImg_arr().get(1);
                imgSrcRight = (String) findSummary.getImg_arr().get(2);
                layoutParams.height = PhotoThreeHeight;
                holder.setText(R.id.news_summary_title_tv, AppApplication.getAppContext()
                        .getString(R.string.photo_collections, findSummary.getTitle()));
            } else if (size >= 2) {
                imgSrcLeft = (String) findSummary.getImg_arr().get(0);
                imgSrcMiddle = (String) findSummary.getImg_arr().get(1);

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = (String) findSummary.getImg_arr().get(0);
                layoutParams.height = PhotoOneHeight;
            }
        }
        setPhotoImageView(holder, imgSrcLeft, imgSrcMiddle, imgSrcRight);
        news_summary_photo_iv_group.setLayoutParams(layoutParams);
    }

    private void setPhotoImageView(ViewHolderHelper holder, String imgSrcLeft, String imgSrcMiddle, String imgSrcRight) {
        if (imgSrcLeft != null) {
            holder.setVisible(R.id.news_summary_photo_iv_left, true);
            holder.setImageUrl(R.id.news_summary_photo_iv_left, imgSrcLeft);
        } else {
            holder.setVisible(R.id.news_summary_photo_iv_left, false);
        }
        if (imgSrcMiddle != null) {
            holder.setVisible(R.id.news_summary_photo_iv_middle, true);
            holder.setImageUrl(R.id.news_summary_photo_iv_middle, imgSrcMiddle);
        } else {
            holder.setVisible(R.id.news_summary_photo_iv_middle, false);
        }
        if (imgSrcRight != null) {
            holder.setVisible(R.id.news_summary_photo_iv_right, true);
            holder.setImageUrl(R.id.news_summary_photo_iv_right, imgSrcRight);
        } else {
            holder.setVisible(R.id.news_summary_photo_iv_right, false);
        }
    }

}
