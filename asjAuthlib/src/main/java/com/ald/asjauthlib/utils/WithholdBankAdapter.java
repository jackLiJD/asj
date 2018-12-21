package com.ald.asjauthlib.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.listener.OnStartDragListener;
import com.ald.asjauthlib.auth.model.WithholdBankCardModel;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ywd on 2017/11/14 00:48
 */

public class WithholdBankAdapter extends RecyclerView.Adapter<WithholdBankAdapter.ItemViewHolder> implements ItemTouchHelperListener {
    private Context context;
    private List<WithholdBankCardModel> items = new ArrayList<>();
    private final OnStartDragListener onStartDragListener;
    private OnExchangeFinished onExchangeFinished;

    public WithholdBankAdapter(Context context, List<WithholdBankCardModel> items, OnStartDragListener onStartDragListener, OnExchangeFinished onExchangeFinished) {
        this.onStartDragListener = onStartDragListener;
        this.items = items;
        this.context = context;
        this.onExchangeFinished=onExchangeFinished;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_withhold_bank, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        if (items.get(position) != null) {
            if(ModelEnum.Y.getModel().equals(items.get(position).getIsMain())){
                holder.textView.setText(String.format(AlaConfig.getResources().getString(R.string.withhold_card_format_main),items.get(position).getCard()));
            }else {
                holder.textView.setText(items.get(position).getCard());
            }
            //主卡不显示拖动按钮
            /*if(ModelEnum.Y.getModel().equals(items.get(position).getIsMain())){
                holder.ivDrag.setVisibility(View.GONE);
            }else {
                holder.ivDrag.setVisibility(View.VISIBLE);
            }*/

            // Start a drag whenever the handle view it touched
            holder.ivDrag.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        onStartDragListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(items, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(items, i, i - 1);
            }
        }
        if(onExchangeFinished!=null){
            onExchangeFinished.onExchangeFinish(items);
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final ImageView ivDrag;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_bank_name);
            ivDrag = (ImageView) itemView.findViewById(R.id.iv_drag);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public interface OnExchangeFinished{
        void onExchangeFinish(List<WithholdBankCardModel> list);
    }
}
