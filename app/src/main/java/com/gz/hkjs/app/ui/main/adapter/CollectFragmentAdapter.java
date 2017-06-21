package com.gz.hkjs.app.ui.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lwy on 2017/4/25.
 */

public class CollectFragmentAdapter extends FragmentPagerAdapter {

    private String[] titles = new String[]{"食谱", "文章"};
    private List<Fragment> fragmentList;

    public CollectFragmentAdapter(FragmentManager fm, Context context,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return    fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
