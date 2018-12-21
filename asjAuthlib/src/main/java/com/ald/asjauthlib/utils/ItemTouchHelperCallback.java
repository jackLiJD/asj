package com.ald.asjauthlib.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


import com.ald.asjauthlib.auth.model.WithholdBankCardModel;

import java.util.List;

/**
 * Created by ywd on 2017/11/14 00:19
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public static final float ALPHA_FULL = 1.0f;
    private final ItemTouchHelperListener listener;
    private List<WithholdBankCardModel> list;

    public ItemTouchHelperCallback(ItemTouchHelperListener listener,List<WithholdBankCardModel> list) {
        this.listener = listener;
        this.list=list;
    }

    /**
     * 用于设置是否处理拖拽事件和滑动事件
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //dragFlags 是拖拽标志，swipeFlags是滑动标志，我们把swipeFlags 都设置为0，表示不处理滑动操作。
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipFlags = 0;
            return makeMovementFlags(dragFlags,swipFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipFlags = 0;
            return makeMovementFlags(dragFlags,swipFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition=viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition=target.getAdapterPosition();//得到目标ViewHolder的position
        /*if(fromPosition<toPosition){
            for (int i=fromPosition;i<toPosition;i++){
                Collections.swap(list,i,i+1);
            }
        }else {
            for (int i = fromPosition; i >toPosition ; i--) {
                Collections.swap(list,i,i-1);
            }
        }*/
        listener.onItemMove(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
