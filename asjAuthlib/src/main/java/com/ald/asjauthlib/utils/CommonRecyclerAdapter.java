package com.ald.asjauthlib.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuangdaoyuan on 2018/5/2.
 * 安静撸码，淡定做人
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> dataList = new ArrayList();
    protected int itemLayoutId;
    private Context mContext;
    private OnRecyclerClickListener onRecyclerClickListener;


    interface OnRecyclerClickListener {
        void onClick(View view, Object dataItem, int position);
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }

    public CommonRecyclerAdapter(RecyclerView recyclerView, @LayoutRes int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
        this.mContext = recyclerView.getContext();
    }

    public void setDataList(List<T> dataList) {
        if (dataList != null) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        bindViewData(holder, dataList.get(position), position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerClickListener != null && v != null) {
                    onRecyclerClickListener.onClick(v, dataList.get(position), position);
                }
            }
        });
    }

    public abstract void bindViewData(BaseViewHolder baseViewHolder, T dataItem, int position);


    @Override
    public int getItemCount() {
        return this.dataList.size();
    }
}
