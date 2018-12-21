package com.ald.asjauthlib.tatarka.bindingcollectionadapter.factories;

import android.support.v4.view.ViewPager;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.BindingViewPagerAdapter;
import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemViewArg;

public interface BindingViewPagerAdapterFactory {
    <T> BindingViewPagerAdapter<T> create(ViewPager viewPager, ItemViewArg<T> arg);

    BindingViewPagerAdapterFactory DEFAULT = new BindingViewPagerAdapterFactory() {
        @Override
        public <T> BindingViewPagerAdapter<T> create(ViewPager viewPager, ItemViewArg<T> arg) {
            return new BindingViewPagerAdapter<>(arg);
        }
    };
}
