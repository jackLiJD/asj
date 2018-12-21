package com.ald.asjauthlib.authframework.core.ui.flowlayout;

import android.view.View;

import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class TagAdapter<T> {
    private List<T> tagDataList;
    private OnDataChangedListener mOnDataChangedListener;
    private HashSet<Integer> mCheckedPosList = new HashSet<Integer>();

    public TagAdapter(List<T> datas) {
        tagDataList = datas;
    }

    public TagAdapter(T[] datas) {
        tagDataList = new ArrayList<T>(Arrays.asList(datas));
    }

    public void setTagDataList(List<T> list) {
        tagDataList = list;
        notifyDataChanged();
    }

    void setOnDataChangedListener(OnDataChangedListener listener) {
        mOnDataChangedListener = listener;
    }

    public void setSelectedList(int... poses) {
        Set<Integer> set = new HashSet<>();
        for (int pos : poses) {
            set.add(pos);
        }
        setSelectedList(set);
    }

    public void setSelectedList(Set<Integer> set) {
        mCheckedPosList.clear();
        if (set != null) {
            mCheckedPosList.addAll(set);
        }
        notifyDataChanged();
    }

    public void setSelectedLists(Set<Integer> set) {
        mCheckedPosList.clear();
        if (set != null) {
            mCheckedPosList.addAll(set);
        }
    }

    HashSet<Integer> getPreCheckedList() {
        return mCheckedPosList;
    }

    public int getCount() {
        return tagDataList == null ? 0 : tagDataList.size();
    }

    public void notifyDataChanged() {
        AlaConfig.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mOnDataChangedListener != null) {
                    mOnDataChangedListener.onChanged();
                }
            }
        });
    }

    public T getItem(int position) {
        return tagDataList.get(position);
    }

    public abstract View getView(FlowLayout parent, int position, T t);

    public boolean setSelected(int position, T t) {
        return false;
    }

    interface OnDataChangedListener {
        void onChanged();
    }


}