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
import com.ald.asjauthlib.cashier.ui.RepaymentDtlActivity;
import com.ald.asjauthlib.utils.TimeUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

/*
 * Created by luckyliang on 2017/12/22.
 */

public class ItemRepayDtlVM extends BaseVM {

    public final ObservableField<String> displayTitleText = new ObservableField<>();

    public final ObservableField<String> displayAmount = new ObservableField<>();

    public final ObservableField<String> displayTime = new ObservableField<>();

    public final ObservableField<String> displayRemark = new ObservableField<>(); //商品名称

    public final ObservableField<String> displayRepayStatus = new ObservableField<>();//还款进度

    public final ObservableInt displayRepayStautsColor = new ObservableInt(0);

    ItemDataPair itemDataPair;
    RefundModel.Amount amount;
    Activity activity;

    /**
     * 还款记录
     */
    ItemRepayDtlVM(ItemDataPair itemDataPair, Activity context) {

        this.itemDataPair = itemDataPair;
        this.activity = context;
        if (itemDataPair.getItemType() == ReimburseRecordVM.TYPE_MONTH_TITLE) {
            RefundModel.Month month = (RefundModel.Month) itemDataPair.getData();

            String title = String.format(AlaConfig.getResources().getString(R.string.repay_record_item_title), month.getYear(),
                    month.getMonth(), month.getAmountList().size());
            displayTitleText.set(title.replace("共0条", "无"));

        } else if (itemDataPair.getItemType() == ReimburseRecordVM.TYPE_MONTH_DETAIL) {
            amount = (RefundModel.Amount) itemDataPair.getData();
            displayRemark.set(amount.getRemark());
            displayAmount.set(amount.getAmount() + "元");
            displayTime.set(TimeUtils.longToBeijingTimeString(amount.getGmtModified()));
            setStatus(amount.getStatus(), context);
            displayTime.set(TimeUtils.longToBeijingTimeString(amount.getGmtModified()));
        }
    }

    public void setStatus(int status, Context context) {
        switch (status) {
            case 0:
                displayRepayStatus.set("新建");
                displayRepayStautsColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_232323));
                break;
            case 1:
                displayRepayStatus.set("处理中");
                displayRepayStautsColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_232323));
                break;
            case 2:
                displayRepayStatus.set("完成");
                displayRepayStautsColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_646464));
                break;
            case 3:
                displayRepayStatus.set("失败");
                displayRepayStautsColor.set(ContextCompat.getColor(AlaConfig.getContext(), R.color.color_ff5546));
                break;
        }
    }

    public void onDtlClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_AMOUNT_ID, amount.getId());
        intent.putExtra(BundleKeys.STAGE_JUMP, activity.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(RepaymentDtlActivity.class, intent);

    }


}
