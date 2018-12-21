package com.ald.asjauthlib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.MyRepaymentDtlModel;
import com.ald.asjauthlib.cashier.utils.RepayBottomDialogAdapter;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.DataUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/8.
 * 还款，点击item 底部弹框层
 */
public class RepaymentBottomDialog extends Dialog {

    private final static int DIALOG_MAX_DP_HEIGHT = 438;


    RepaymentBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {

        RepaymentBottomDialog dialog;
        Context context;
        RecyclerView recyclerView;
        RepayBottomDialogAdapter adapter;
        String strDescribe;
        TextView txtAmount;
        MyRepaymentDtlModel myRepaymentDtlModel;
        CheckBox checkBox;
        boolean isAllSelected;//记录全选状态 isChecked不准
        OnCheckedCompleteListener onCheckedCompleteListener;
        List<Long> checkedItems;

        public RepaymentBottomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final RepaymentBottomDialog dialog = new RepaymentBottomDialog(context, R.style.BottomSelectDialog);
            View layout = inflater.inflate(R.layout.dialog_bottom_repayment, null);
            txtAmount = layout.findViewById(R.id.txt_amount);

            TextView txtDescribe = layout.findViewById(R.id.txt_describe);
            txtDescribe.setText(strDescribe);

            checkBox = layout.findViewById(R.id.checker);

            if (checkedItems.size() == 0) {
                //全部不选显示账单总计
                txtAmount.setText(String.format(AlaConfig.getResources().getString(R.string.repayment_dialog_total),
                        AppUtils.formatAmountKeepMinus(myRepaymentDtlModel.getMoneny())));
            } else {
                txtAmount.setText(String.format(AlaConfig.getResources().getString(R.string.repayment_dialog_sum),
                        AppUtils.formatAmountKeepMinus(getAmountChecked())));
            }

            isAllSelected = checkedItems.size() == myRepaymentDtlModel.getBillList().size();
            checkBox.setChecked(isAllSelected);
            checkBox.setChecked(checkedItems.size() == myRepaymentDtlModel.getBillList().size());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAllSelected = !isAllSelected;
                    if (isAllSelected) {
                        txtAmount.setText(String.format(AlaConfig.getResources().getString(R.string.repayment_dialog_total),
                                myRepaymentDtlModel.getMoneny()));
                    } else {

                        txtAmount.setText(String.format(AlaConfig.getResources().getString(R.string.repayment_dialog_sum),
                                AppUtils.formatAmountKeepMinus(myRepaymentDtlModel.getMoneny())));
                    }
                    adapter.setAllSelected(isAllSelected);
                }
            });
            recyclerView = layout.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);

            TextView btnSure = layout.findViewById(R.id.btn_sure);
            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCheckedCompleteListener.onCheckedComplete(myRepaymentDtlModel.getStatus(), adapter.getCheckedItem(),
                            getAmountChecked());
                    dialog.dismiss();
                }
            });

            Window window = dialog.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.Animation_BottomSelect);
                dialog.setContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DensityUtils.getPxByDip(DIALOG_MAX_DP_HEIGHT)));
                dialog.setCancelable(true);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.gravity = Gravity.BOTTOM;
                lp.width = DataUtils.getCurrentDisplayMetrics().widthPixels;
                lp.dimAmount = 0.5f;
            }
            return dialog;

        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setAdapter(final MyRepaymentDtlModel myRepaymentDtlModel) {
            this.myRepaymentDtlModel = myRepaymentDtlModel;
            isAllSelected = checkedItems.size() == myRepaymentDtlModel.getBillList().size();
            adapter = new RepayBottomDialogAdapter(context, myRepaymentDtlModel, checkedItems, new RepayBottomDialogAdapter.onCheckedChangeListener() {
                @Override
                public void onCheckedChange(BigDecimal amout) {
                    txtAmount.setText(String.format(AlaConfig.getResources().getString(R.string.repayment_dialog_total),
                            AppUtils.formatAmount(amout)));
                    checkBox.setChecked(checkedItems.size() == myRepaymentDtlModel.getBillList().size());
                    isAllSelected = checkedItems.size() == myRepaymentDtlModel.getBillList().size();
                    if (checkedItems.size() == 0) {
                        txtAmount.setText(String.format(AlaConfig.getResources().getString(R.string.repayment_dialog_sum),
                                AppUtils.formatAmount(myRepaymentDtlModel.getMoneny())));
                    }
                }

                @Override
                public void onSelectedAll(boolean isSelectedAll) {
                    if (isSelectedAll) {
                        checkBox.setChecked(true);
                        isAllSelected = true;
                    }
                }
            });

            String status = myRepaymentDtlModel.getStatus();
//            账单类型 overdue 逾期 out 某月已出 notOut 未出账单
            switch (status) {
                case "overdue":
                    strDescribe = "逾期未还账单";
                    break;
                case "out":
                    strDescribe = myRepaymentDtlModel.getMonth() + "月份已出账单";
                    break;
                case "notOut":
                    strDescribe = myRepaymentDtlModel.getMonth() + "月份未出已入账单";
                    break;
            }
            return this;
        }

        public Builder setCheckedItems(List<Long> bills) {
            this.checkedItems = bills;
            return this;
        }

        public Builder setListener(OnCheckedCompleteListener onCheckedCompleteListener) {
            this.onCheckedCompleteListener = onCheckedCompleteListener;
            return this;
        }

        public interface OnCheckedCompleteListener {
            /**
             * @param checkedBillIds 已选账单号
             */
            void onCheckedComplete(String type, List<Long> checkedBillIds, BigDecimal checkedAmout);
        }

        /**
         * 计算当前选中总额
         */
        BigDecimal getAmountChecked() {
            BigDecimal amountChecked = new BigDecimal(0);
            for (MyRepaymentDtlModel.Bill bill : myRepaymentDtlModel.getBillList()) {
                if (checkedItems.contains(bill.getRid()))
                    amountChecked = amountChecked.add(bill.getBillAmount());
            }
            return amountChecked;
        }

    }
}
