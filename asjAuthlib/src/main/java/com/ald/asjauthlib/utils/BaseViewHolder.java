package com.ald.asjauthlib.utils;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zhuangdaoyuan on 2018/5/2.
 * 安静撸码，淡定做人
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> sparseArray;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.sparseArray = new SparseArray<>(10);
    }

    public <T extends View> T getView(int viewId) {
        View view = sparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            sparseArray.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
        return this;
    }

}
