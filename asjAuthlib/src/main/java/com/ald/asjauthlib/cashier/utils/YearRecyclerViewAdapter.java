package com.ald.asjauthlib.cashier.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.List;

/**
 * Created by luckyliang on 2017/12/28.
 */

public class YearRecyclerViewAdapter extends RecyclerView.Adapter<YearRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Integer> years;
    int checkedPosition;
    OnItemClickListener listener;


    public YearRecyclerViewAdapter(Context context, List<Integer> years, int checkedPosition, OnItemClickListener listener) {
        this.context = context;
        this.years = years;
        this.checkedPosition = checkedPosition;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_calender_year, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtYear.setText(years.get(position) + "年");
        holder.txtYear.setTextColor(ContextCompat.getColor(AlaConfig.getContext(),checkedPosition == position ? R.color.white : R.color.color_646464));
        if (checkedPosition == position) {
            holder.txtYear.setBackgroundResource(R.drawable.btn_auth);
        } else
            holder.txtYear.setBackgroundColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.white));
        holder.txtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedPosition = position;
                notifyDataSetChanged();
                listener.onClick(checkedPosition);
            }
        });
    }


    @Override
    public int getItemCount() {
        return years.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtYear;

        public ViewHolder(View itemView) {
            super(itemView);
            txtYear = itemView.findViewById(R.id.txt_year);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
