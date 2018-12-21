package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.cashier.model.RefundModel;
import com.ald.asjauthlib.cashier.ui.ReimburseDtlActivity;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.TimeUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

/**
 * Created by luckyliang on 2017/12/7.
 * 退款记录Item详情
 */

public class ItemReimburseDtlVM extends BaseVM {

    public final ObservableField<String> displayTitleText = new ObservableField<>();
    public final ObservableField<String> displayAmount = new ObservableField<>();
    public final ObservableField<String> displayTime = new ObservableField<>();
    public final ObservableField<String> displayRemark = new ObservableField<>(); //商品名称
    public final ObservableField<String> displayRepayStatus = new ObservableField<>();//还款进度
    public final ObservableInt displayRepayStautsColor = new ObservableInt(0);

    private RefundModel.Amount amount;
    ItemDataPair itemDataPair;
    private Activity activity;


    /**
     * 退款记录
     */
    ItemReimburseDtlVM(ItemDataPair itemDataPair, Activity context) {
        this.itemDataPair = itemDataPair;
        this.activity = context;
        if (itemDataPair.getItemType() == ReimburseRecordVM.TYPE_MONTH_TITLE) {
            RefundModel.Month month = (RefundModel.Month) itemDataPair.getData();
            if (month.getAmountList().size() == 0) {
                displayTitleText.set(String.format(AlaConfig.getResources().getString(R.string.reimburse_record_item_title_null), month.getYear(), month.getMonth()));
            } else {
                displayTitleText.set(String.format(AlaConfig.getResources().getString(R.string.reimburse_record_item_title), month.getYear(),
                        month.getMonth(), month.getAmountList().size()));
            }

        } else if (itemDataPair.getItemType() == ReimburseRecordVM.TYPE_MONTH_DETAIL) {
            amount = (RefundModel.Amount) itemDataPair.getData();
            displayRemark.set(amount.getRemark());
            displayAmount.set(AppUtils.formatAmountKeepMinus(amount.getAmount()) + "元");
            setStatus(amount.getStatus(), context);
            displayTime.set(TimeUtils.longToBeijingTimeString(amount.getGmtModified()));
        }
    }

    public void setStatus(int status, Context context) {
        switch (status) {
            case 0:
                displayRepayStatus.set("新建");
                displayRepayStautsColor.set(ContextCompat.getColor(context, R.color.color_232323));
                break;
            case 1:
                displayRepayStatus.set("处理中");
                displayRepayStautsColor.set(ContextCompat.getColor(context, R.color.color_232323));
                break;
            case 2:
                displayRepayStatus.set("完成");
                displayRepayStautsColor.set(ContextCompat.getColor(context, R.color.color_646464));
                break;
            case 3:
                displayRepayStatus.set("失败");
                displayRepayStautsColor.set(ContextCompat.getColor(context, R.color.color_ff5546));
                break;
        }
    }


    public void onDtlClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_AMOUNT_ID, amount.getId());
        intent.putExtra(BundleKeys.STAGE_JUMP, activity.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(ReimburseDtlActivity.class, intent);

    }
}
