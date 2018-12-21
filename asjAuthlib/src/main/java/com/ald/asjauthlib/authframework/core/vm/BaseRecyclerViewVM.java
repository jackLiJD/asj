package com.ald.asjauthlib.authframework.core.vm;

import android.support.v7.widget.RecyclerView;

import com.ald.asjauthlib.authframework.core.ui.DividerLine;

import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemView;
import com.ald.asjauthlib.tatarka.bindingcollectionadapter.ItemViewSelector;
import com.ald.asjauthlib.tatarka.bindingcollectionadapter.RecyclerViewAdapterListener;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/24 16:34
 * 描述：抽象的共用RecyclerView
 * 修订历史：
 */
public abstract class BaseRecyclerViewVM<T> extends BaseVM{
    /**
     * 设置布局
     */
    protected abstract void selectView(ItemView itemView, int position, T item);

    /**
     * tips栏目
     */
//    public String[] tips = null;
    /**
     * 分割线类型
     * -1 - 没有分割线
     * 0 - 水平分割线
     * 1 - 垂直分割线
     */
    public int type = DividerLine.HORIZONTAL;
    private RecyclerView.Adapter recyclerViewAdapter;
    /**
     * 数据源
     */
    public AlaObservableArrayList<T> items = new AlaObservableArrayList<>();
    public final ItemViewSelector<T> itemView = new ItemViewSelector<T>() {
        @Override
        public void select(ItemView itemView, int position, T item) {
            selectView(itemView, position, item);
        }

        @Override
        public int viewTypeCount() {
            return getViewTypeCount();
        }
    };

    protected int getViewTypeCount() {
        return 0;
    }

    public final RecyclerViewAdapterListener adapterListener = new RecyclerViewAdapterListener() {
        @Override
        public void adapterListener(RecyclerView.Adapter adapter) {
            setRecyclerViewAdapter(adapter);
        }
    };

    /**
     * 刷新控件回调
     */
//    public final ObservableField<PtrFrameListener> listener = new ObservableField<>();
    /**
     * 刷新控件对象
     */
//    protected PtrClassicFrameLayout ptrFrame;

//    public PtrClassicFrameLayout getPtrFrame() {
//        return ptrFrame;
//    }

//    protected void setPtrFrame(PtrClassicFrameLayout ptrFrame) {
//        this.ptrFrame = ptrFrame;
//    }

    public RecyclerView.Adapter getRecyclerViewAdapter() {
        return recyclerViewAdapter;
    }

    public void setRecyclerViewAdapter(RecyclerView.Adapter adapter) {
        this.recyclerViewAdapter = adapter;
    }

    /**
     * 熟悉数据
     */
    public void itemsClear(){
        if(recyclerViewAdapter != null){
            items.AlaClear();
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
