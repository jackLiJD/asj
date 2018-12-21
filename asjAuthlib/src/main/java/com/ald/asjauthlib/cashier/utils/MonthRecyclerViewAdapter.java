package com.ald.asjauthlib.cashier.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.CalendarModel;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/3.
 */

public class MonthRecyclerViewAdapter extends RecyclerView.Adapter<MonthRecyclerViewAdapter.MonthRVViewHolder> {

    List<String> listMonth = new ArrayList<>();
    Context mContext;
    private int checkPosition = -1;
    MonthOnClickListener mListener;
    CalendarModel calendarModel;

    public MonthRecyclerViewAdapter(Context context, MonthOnClickListener listener, CalendarModel calendarModel) {
        //选定当前月份
        listMonth = Arrays.asList(AlaConfig.getResources().getStringArray(R.array.monthList));
        mContext = context;
        mListener = listener;
        this.calendarModel = calendarModel;
    }


    @Override
    public MonthRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_calender_month, parent, false);
        return new MonthRecyclerViewAdapter.MonthRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MonthRVViewHolder holder, int position) {
        holder.txtMonth.setText(listMonth.get(position));
        holder.indicator.setVisibility(checkPosition == position ? View.VISIBLE : View.INVISIBLE);
        if (calendarModel.getMonthList().contains(position + 1)) {
            holder.txtMonth.setTextColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.color_232323));
            holder.txtMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPosition = checkPosition == holder.getAdapterPosition() ? -1 : holder.getAdapterPosition();
                    holder.indicator.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                    mListener.onMonthClickListener(checkPosition);
                }
            });
        } else
            holder.txtMonth.setTextColor(ContextCompat.getColor(AlaConfig.getContext(),calendarModel.getMonthList().contains(position) ?
                    R.color.color_232323 : R.color.fw_text_grey));

    }

    @Override
    public int getItemCount() {
        return listMonth.size();
    }

    static class MonthRVViewHolder extends RecyclerView.ViewHolder {

        TextView txtMonth;
        View indicator;

        private MonthRVViewHolder(View itemView) {
            super(itemView);
            txtMonth = (TextView) itemView.findViewById(R.id.txt_month);
            indicator = itemView.findViewById(R.id.indicator);
        }

    }

    public interface MonthOnClickListener {
        void onMonthClickListener(int leftPosition);
    }
}
