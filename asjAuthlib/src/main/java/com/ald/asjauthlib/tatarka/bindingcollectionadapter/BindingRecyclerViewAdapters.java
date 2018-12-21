package com.ald.asjauthlib.tatarka.bindingcollectionadapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.factories.BindingRecyclerViewAdapterFactory;

/**
 * @see {@link BindingCollectionAdapters}
 */
public class BindingRecyclerViewAdapters {
    // RecyclerView
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"itemView", "items", "adapter", "itemIds", "viewHolder","notifyAdapter", "isRebound"}, requireAll = false)
    public static <T> void setAdapter(RecyclerView recyclerView, ItemViewArg<T> arg, List<T> items,
            BindingRecyclerViewAdapterFactory factory, BindingRecyclerViewAdapter.ItemIds<T> itemIds, BindingRecyclerViewAdapter.ViewHolderFactory viewHolderFactory,final RecyclerViewAdapterListener listener, boolean isRebound) {
        if (arg == null) {
            throw new IllegalArgumentException("itemView must not be null");
        }
        if (factory == null) {
            factory = BindingRecyclerViewAdapterFactory.DEFAULT;
        }
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter == null) {
            adapter = factory.create(recyclerView, arg);
            adapter.setItems(items);
            adapter.setItemIds(itemIds);
            adapter.setViewHolderFactory(viewHolderFactory);
            recyclerView.setAdapter(adapter);
            if(listener != null){
                listener.adapterListener(adapter);
            }
        } else {
            adapter.setItems(items);
        }
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView recyclerView, LayoutManagers.LayoutManagerFactory layoutManagerFactory) {
        recyclerView.setLayoutManager(layoutManagerFactory.create(recyclerView));
    }

    @BindingConversion
    public static BindingRecyclerViewAdapterFactory toRecyclerViewAdapterFactory(final String className) {
        return new BindingRecyclerViewAdapterFactory() {
            @Override
            public <T> BindingRecyclerViewAdapter<T> create(RecyclerView recyclerView, ItemViewArg<T> arg) {
                return Utils.createClass(className, arg);
            }
        };
    }



}
