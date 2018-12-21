package com.ald.asjauthlib.tatarka.bindingcollectionadapter.factories;

import android.support.v7.widget.RecyclerView;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.BindingRecyclerViewAdapter;
import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemViewArg;

public interface BindingRecyclerViewAdapterFactory {
    <T> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ItemViewArg<T> arg);

    BindingRecyclerViewAdapterFactory DEFAULT = new BindingRecyclerViewAdapterFactory() {
        @Override
        public <T> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ItemViewArg<T> arg) {
            return new BindingRecyclerViewAdapter<>(arg);
        }
    };
}
