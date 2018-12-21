package com.ald.asjauthlib.tatarka.bindingcollectionadapter.factories;

import android.widget.AdapterView;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.BindingListViewAdapter;
import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemViewArg;

public interface BindingAdapterViewFactory {
    <T> BindingListViewAdapter<T> create(AdapterView adapterView, ItemViewArg<T> arg);

    BindingAdapterViewFactory DEFAULT = new BindingAdapterViewFactory() {
        @Override
        public <T> BindingListViewAdapter<T> create(AdapterView adapterView, ItemViewArg<T> arg) {
            return new BindingListViewAdapter<>(arg);
        }
    };
}
