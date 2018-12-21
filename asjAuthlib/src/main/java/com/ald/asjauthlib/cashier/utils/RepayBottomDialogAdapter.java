package com.ald.asjauthlib.cashier.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.MyRepaymentDtlModel;
import com.ald.asjauthlib.utils.TimeUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/16.
 * 还款弹窗
 */

public class RepayBottomDialogAdapter extends RecyclerView.Adapter<RepayBottomDialogAdapter.ViewHolder> {

    MyRepaymentDtlModel myRepaymentDtlModel;
    List<MyRepaymentDtlModel.Bill> bills;
    Context context;
    BigDecimal amount;//已选总金额;
    onCheckedChangeListener listener;
    boolean allSelected = true;
    int selectedCount;//已选个数
    String status;//逾期、已出、或未出
    List<Long> mBillsChecked;

    public RepayBottomDialogAdapter(Context context, MyRepaymentDtlModel myRepaymentDtlModel, List<Long> billsChecked, onCheckedChangeListener listener) {
        this.context = context;
        this.listener = listener;
        this.myRepaymentDtlModel = myRepaymentDtlModel;
        this.mBillsChecked = billsChecked;
        bills = myRepaymentDtlModel.getBillList();
        getAmount();
        selectedCount = bills.size();
        status = myRepaymentDtlModel.getStatus();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dialog_bottom_repayment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MyRepaymentDtlModel.Bill bill = bills.get(position);
        holder.txtName.setText(bill.getName());
        //账单类型 overdue 逾期 out 某月已出 notOut 未出账单
        holder.txtTime.setText(TimeUtils.longToBeijingTimeString(bill.getPayDate()));
        holder.txtStageInfo.setText(String.format(AlaConfig.getResources().getString(R.string.repay_bottom_dialog_item_stage_info),
                bill.getBillNper(), bill.getNper())); //01/12期
        holder.txtAmount.setText(bill.getBillAmount().toString() + "元");

        holder.checkBox.setChecked(mBillsChecked.contains(bill.getRid()));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBillsChecked.contains(bill.getRid())) {
                    mBillsChecked.remove(bill.getRid());
                    selectedCount--;
                    amount = amount.subtract(bill.getBillAmount());
                } else {
                    selectedCount++;
                    amount = amount.add(bill.getBillAmount());
                    mBillsChecked.add(bill.getRid());
                    if (mBillsChecked.size() == bills.size()) {
                        listener.onSelectedAll(true);
                    }
                }
                listener.onCheckedChange(amount);

            }
        });

    }

    public void addAll(List<MyRepaymentDtlModel.Bill> bills) {
        mBillsChecked.clear();
        for (MyRepaymentDtlModel.Bill bill : bills) {
            mBillsChecked.add(bill.getRid());
        }
    }

    public void getAmount() {
        amount = new BigDecimal(0);
        for (MyRepaymentDtlModel.Bill bill :
                myRepaymentDtlModel.getBillList()) {
            if (mBillsChecked.contains(bill.getRid()))
                amount = amount.add(bill.getBillAmount());
        }
    }

    @Override
    public int getItemCount() {
        return myRepaymentDtlModel.getBillList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtAmount;
        TextView txtTime;
        TextView txtStageInfo;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_goods_name);
            txtAmount = itemView.findViewById(R.id.txt_amount);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtStageInfo = itemView.findViewById(R.id.txt_stage_info);

            checkBox = itemView.findViewById(R.id.checker);
        }
    }

    public interface onCheckedChangeListener {

        void onCheckedChange(BigDecimal amount);

        void onSelectedAll(boolean isSelectedAll);
    }

    public void setAllSelected(boolean selected) {
        allSelected = selected;
        if (selected)
            addAll(myRepaymentDtlModel.getBillList());
        else {
            mBillsChecked.clear();
            amount = new BigDecimal(0);
        }
        notifyDataSetChanged();
    }

    /**
     * 获取选中billid
     */
    public List<Long> getCheckedItem() {
        return mBillsChecked;
    }
}
