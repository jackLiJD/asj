package com.ald.asjauthlib.cashier.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.cashier.model.HistoryBillMonthDtlModel;
import com.ald.asjauthlib.cashier.ui.ConsumeDtlActivity;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.TimeUtils;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

/*
 * Created by luckyliang on 2017/12/11.
 */

public class ItemHistoryBillMonthVM extends BaseVM {

    public final ObservableField<String> displayName = new ObservableField<>();
    public final ObservableField<String> displayBillAmount = new ObservableField<>(); //子账单金额
    public final ObservableField<String> displayPayDate = new ObservableField<>();//支付时间
    public final ObservableField<String> displayPerInfo = new ObservableField<>();//分期相关信息 1/12期

    private int rid = 0;
    private String stage;


    public ItemHistoryBillMonthVM(HistoryBillMonthDtlModel.SubBill subBill, String stage) {
        displayName.set(subBill.getName());
        displayBillAmount.set(AppUtils.formatAmount(subBill.getBillAmount()) + "元");
        displayPayDate.set(TimeUtils.dateToBeijingTimeString(subBill.getPayDate()));
        displayPerInfo.set(subBill.getBillNper() + "/" + subBill.getNper() + "期");
        rid = (int) subBill.getRid();
        this.stage = stage;

    }

    public void onItemClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_BILL_ID, rid);
        intent.putExtra("history", true);
        intent.putExtra(BundleKeys.STAGE_JUMP, stage);
        ActivityUtils.push(ConsumeDtlActivity.class, intent);

    }
}
