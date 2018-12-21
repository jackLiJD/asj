package com.ald.asjauthlib.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/17 14:21
 * 描述：
 * 修订历史：
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<?> fragmentList;
    private List<String> titleList;

    /**
     * 普通，主页使用
     */
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    /**
     * 接收首页传递的标题
     */
    public MyFragmentPagerAdapter(FragmentManager fm, List<?> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     * 首页显示title
     * 若有问题，移到对应单独页面
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (titleList != null) {
            return titleList.get(position);
        } else {
            return "";
        }
    }

    public void addFragmentList(List<?> fragment) {
        this.fragmentList.clear();
        this.fragmentList = null;
        this.fragmentList = fragment;
        notifyDataSetChanged();
    }

}
