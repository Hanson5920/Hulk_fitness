package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.ChooseItem;
import com.gz.hkjs.app.parameter.JMClassChoose;
import com.gz.hkjs.app.ui.main.contract.ChooseItemContract;
import com.gz.hkjs.app.ui.main.model.ChooseItemModel;
import com.gz.hkjs.app.ui.main.presenter.ChooseItemPresenter;
import com.gz.hkjs.app.widget.BubbleSeekBar;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.LoadingTip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 自选 课程 筛选
 * Created by Administrator on 2017/4/10.
 */

public class ChooseItemActivity extends BaseActivity<ChooseItemPresenter, ChooseItemModel>
        implements ChooseItemContract.View {

    @BindView(R.id.id_back)
    ImageView idBack;

    @BindView(R.id.id_total_toolbar_title)
    TextView idTotalToolbarTitle;

    @BindView(R.id.id_right_tv)
    TextView idRightTv;
    @BindView(R.id.button_confirm)

    Button button;

    @BindView(R.id.seekBar)
    BubbleSeekBar seekBar;

    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;

    @BindView(R.id.recyclerView2)
    RecyclerView recyclerView2;

    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;

    private CommonRecycleViewAdapter adapter1, adapter2;

    private View oldView1 = null;
    private View oldView2 = null;


    private List<ChooseItem.DataBeanX.DataBean> dataBeanList;
    private List<String> dataStrList = new ArrayList<>();

    HashMap<String, String> where = new HashMap<>();
    HashMap<String, String> where1 = new HashMap<>();
    HashMap<String, String> where2 = new HashMap<>();
    HashMap<String, String> where3 = new HashMap<>();
    HashMap<String, String> wheremap;

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity, int code, HashMap<String, String> wheremap) {
//        activity.startActivity(intent);
        Intent intent = new Intent(activity, ChooseItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("whereMap", wheremap);
        intent.putExtra("wheremapBundle", bundle);
        activity.startActivityForResult(intent, code);
        activity.overridePendingTransition(R.anim.fade_in_choose,
                R.anim.fade_out_choose);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chooseitem;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getBundleExtra("wheremapBundle");
        if(bundle!=null)
            wheremap = (HashMap<String, String>) bundle.get("whereMap");
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        idTotalToolbarTitle.setText("筛选");
        mPresenter.getChooseItemRequest(JMClassChoose.MyJMClass());

        adapter1 = new CommonRecycleViewAdapter<ChooseItem.DataBeanX.DataBean>(this,
                R.layout.item_choose_screen) {
            @Override
            public void convert(final ViewHolderHelper helper, final
            ChooseItem.DataBeanX.DataBean dataBean) {
                /**add by hs**/
                if(isSelected(dataBean)){
                    helper.getConvertView().setSelected(true);
                    helper.setTextColor(R.id.text_name, getResources().getColor(R.color.white));
                    where1.clear();
                    where1.put(dataBean.getParameter(), dataBean.getId());
                    oldView1 = helper.getConvertView();
                }else {
                    helper.getConvertView().setSelected(false);
                    helper.setTextColor(R.id.text_name, getResources().getColor(R.color.alpha_65_black));
                }

                helper.setText(R.id.text_name, dataBean.getTitle());
                helper.setOnClickListener(R.id.line_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        where1.clear();
                        v.setSelected(!v.isSelected());
                        if (v.isSelected()) {
                            helper.setTextColor(R.id.text_name, getResources().getColor(R.color.white));

                        } else {
                            helper.setTextColor(R.id.text_name, getResources().getColor(R.color.alpha_65_black));
                        }
                        if (oldView1 != null && oldView1 != v) {
                            oldView1.setSelected(false);
                            TextView textView = (TextView) oldView1.findViewById(R.id.text_name);
                            textView.setTextColor(getResources().getColor(R.color.alpha_65_black));
                        }
                        oldView1 = v;
                        if (oldView1.isSelected()) {
                            where1.put(dataBean.getParameter(), dataBean.getId());
                        }
                    }
                });
            }
        };

        adapter2 = new CommonRecycleViewAdapter<ChooseItem.DataBeanX.DataBean>(this,
                R.layout.item_choose_screen) {
            @Override
            public void convert(final ViewHolderHelper helper, final
            ChooseItem.DataBeanX.DataBean dataBean) {

                /**add by hs**/
                if(isSelected(dataBean)){
                    helper.getConvertView().setSelected(true);
                    helper.setTextColor(R.id.text_name, getResources().getColor(R.color.white));
                    where2.clear();
                    where2.put(dataBean.getParameter(), dataBean.getId());
                    oldView2 = helper.getConvertView();
                }else {
                    helper.getConvertView().setSelected(false);
                    helper.setTextColor(R.id.text_name, getResources().getColor(R.color.alpha_65_black));
                }

                helper.setText(R.id.text_name, dataBean.getTitle());
                helper.setOnClickListener(R.id.line_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        where2.clear();
                        v.setSelected(!v.isSelected());
                        if (v.isSelected()) {
                            helper.setTextColor(R.id.text_name, getResources().getColor(R.color.white));
                        } else {
                            helper.setTextColor(R.id.text_name, getResources().getColor(R.color.alpha_65_black));
                        }
                        if (oldView2 != null && oldView2 != v) {
                            oldView2.setSelected(false);
                            TextView textView = (TextView) oldView2.findViewById(R.id.text_name);
                            textView.setTextColor(getResources().getColor(R.color.alpha_65_black));
                        }

                        oldView2 = v;
                        if (oldView2.isSelected()) {
                            where2.put(dataBean.getParameter(), dataBean.getId());
                        }
                    }
                });

            }
        };

        //
        GridLayoutManager manager1 = new GridLayoutManager(this, 4);
        recyclerView1.setLayoutManager(manager1);
        recyclerView1.setAdapter(adapter1);

        //
        GridLayoutManager manager2 = new GridLayoutManager(this, 4);
        recyclerView2.setLayoutManager(manager2);
        recyclerView2.setAdapter(adapter2);

        //
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (seekBar.getSelID() != -1) {
                    ChooseItem.DataBeanX.DataBean dataBean = dataBeanList.get(seekBar.getSelID());
                    where3.put(dataBean.getParameter(), dataBean.getId());
                }

                Iterator iter1 = where1.entrySet().iterator();
                while (iter1.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter1.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    where.put(key, val);
                }

                Iterator iter2 = where2.entrySet().iterator();
                while (iter2.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter2.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    where.put(key, val);
                }
                Iterator iter3 = where3.entrySet().iterator();
                while (iter3.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter3.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    where.put(key, val);
                }

                Intent mIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("whereMap", where);
                mIntent.putExtras(bundle);

                // 设置结果，并进行传送
                ChooseItemActivity.this.setResult(AddTraningPlanActivity.resultCode, mIntent);
                finish();
            }
        });
    }

    @Override
    public void showLoading(String title) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
    }

    @Override
    public void stopLoading() {
        if (adapter1 != null && adapter1.getItemCount() > 0) {
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
    public void returnChooseItemData(List<ChooseItem.DataBeanX> videoSummaries) {
        if (videoSummaries != null) {

            if (videoSummaries.size() > 0 && videoSummaries.get(0).getData() != null)
                adapter1.addAll((List) videoSummaries.get(0).getData());

            if (videoSummaries.size() > 1 && videoSummaries.get(1).getData() != null)
                adapter2.addAll((List) videoSummaries.get(1).getData());

            if (videoSummaries.size() > 2 && videoSummaries.get(2).getData() != null) {

                dataBeanList = videoSummaries.get(2).getData();
                for (ChooseItem.DataBeanX.DataBean dataBean : dataBeanList) {
                    dataStrList.add(dataBean.getTitle());
                }
                seekBar.setData(dataStrList);
                int sel = getSeekbarSel();
                seekBar.setSel(sel);
            }
        }
    }

    /**
     * Description 记忆上次筛选
     * UpdateUser husong.
     * UpdateDate 2017/6/5 16:41.
     * Version 1.0
     */
    private boolean isSelected(ChooseItem.DataBeanX.DataBean dataBean){
        if(wheremap!=null) {
            Iterator iter3 = wheremap.entrySet().iterator();
            while (iter3.hasNext()) {
                Map.Entry entry = (Map.Entry) iter3.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if(TextUtils.equals(key, dataBean.getParameter()) && TextUtils.equals(val, dataBean.getId())){
                    return true;
                }
            }
        }
        return false;
    }

    private int getSeekbarSel(){
        if(wheremap!=null){
            Iterator iter3 = wheremap.entrySet().iterator();
            while (iter3.hasNext()) {
                Map.Entry entry = (Map.Entry) iter3.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                for (int i=0; i<dataBeanList.size(); i++){
                    ChooseItem.DataBeanX.DataBean databean = dataBeanList.get(i);
                    if(TextUtils.equals(key, databean.getParameter()) && TextUtils.equals(val, databean.getId())){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

}
