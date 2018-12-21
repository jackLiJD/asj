package com.ald.asjauthlib.cashier.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.SettleAdvancedModel;
import com.ald.asjauthlib.utils.AppUtils;

import java.util.List;

/**
 * Created by luckyliang on 2017/12/17.
 */

public class SettleBottomAdapter extends RecyclerView.Adapter<SettleBottomAdapter.ViewHolder> {


    List<SettleAdvancedModel.Detail> detailList;
    Context context;

    public SettleBottomAdapter(Context context, List<SettleAdvancedModel.Detail> detailList) {
        this.context = context;
        this.detailList = detailList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_settle_bottom_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SettleAdvancedModel.Detail detail = detailList.get(position);
        holder.txtStageIndex.setText(detail.getNper() + "期");
        holder.txtAmount.setText(AppUtils.formatAmount(detail.getAmount()) + "元");
        StringBuilder stringBuilder = new StringBuilder();
        if (!detail.getPoundAmount().toString().equals("0.00")) {
            stringBuilder.append("含手续费：" + AppUtils.formatAmount(detail.getPoundAmount()) + "元");
        }
        if (detail.getOverdue() == 1)//已逾期
        {
            stringBuilder.append("逾期利息" + detail.getInterest() + "元");
            holder.txtStatus.setText("已逾期");
        } else {
            if (detail.getFree() && !detail.getPoundAmount().toString().equals("0.00")) {
                holder.txtStatus.setText("可免手续费" + AppUtils.formatAmount(detail.getPoundAmount()) + "元");
            } else {
                //1已出账 0未出账
                holder.txtStatus.setText(detail.getStatus() == 1 ? "已出账" : "未出账");
            }
        }
        holder.txtServiceCharge.setText(stringBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtStageIndex;//期数
        TextView txtAmount;
        TextView txtServiceCharge;
        TextView txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            txtStageIndex = itemView.findViewById(R.id.txt_index);
            txtAmount = itemView.findViewById(R.id.txt_amount);
            txtServiceCharge = itemView.findViewById(R.id.txt_service_charge);
            txtStatus = itemView.findViewById(R.id.txt_status);
        }
    }
}
