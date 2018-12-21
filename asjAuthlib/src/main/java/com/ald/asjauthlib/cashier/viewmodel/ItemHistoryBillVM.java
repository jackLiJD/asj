package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.HistoryBillListModel;
import com.ald.asjauthlib.cashier.model.ItemDataPair;
import com.ald.asjauthlib.cashier.ui.HistoryBillMonthStatusActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

/**
 * Created by luckyliang on 2017/12/7.
 */

public class ItemHistoryBillVM implements ViewModel {

    //月份 8月账单
    public final ObservableField<String> displayMonth = new ObservableField<>();

    //月账单总额
    public final ObservableField<String> displayAmount = new ObservableField<>();

    //月账单状态
    public final ObservableField<String> displayStatus = new ObservableField<>();

    //header 年份
    public final ObservableField<String> displayYear = new ObservableField<>();

    public Activity mContext;

    public final ItemDataPair mItemDataPair;

    public int year;

    public ItemDataPair getmItemDataPair() {
        return mItemDataPair;
    }

    public ItemHistoryBillVM(Activity context, ItemDataPair itemDataPair, int year) {

        mContext = context;
        this.mItemDataPair = itemDataPair;
        this.year = year;

        if (mItemDataPair.getItemType() == HistoryBillListVM.TYPE_HEAD) {
            displayYear.set(mItemDataPair.getData().toString() + "年");
        } else {
            HistoryBillListModel.Bill model = (HistoryBillListModel.Bill) mItemDataPair.getData();
            displayMonth.set(Integer.toString(model.getBillMonth()) + "月账单");
            displayAmount.set(Double.toString(model.getBillAmount().doubleValue()) + "元");

            if (model.getOverdueDays() == 0) {
                displayStatus.set(AlaConfig.getResources().getString(R.string.history_bill_settle));
            } else {
                if (model.getOverdueStatus() == null || model.getOverdueStatus().equals("N")) {
                    //未逾期
                    displayStatus.set(String.format(mContext.getString(R.string.history_bill_settle_overdue), model.getOverdueDays()));
                } else
                    displayStatus.set(String.format(mContext.getString(R.string.history_bill_not_settle_overdue), model.getOverdueDays()));
            }
        }

    }

    /**
     * 点击进入月份详情
     */
    public void onMonthClick(View view) {
        HistoryBillListModel.Bill model = (HistoryBillListModel.Bill) mItemDataPair.getData();
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_KEY_HISTORY_BILL_MONTH, Integer.toString(model.getBillMonth()));
        intent.putExtra(BundleKeys.INTENT_KEY_HISTORY_BILL_YEAR, Integer.toString(year));
        intent.putExtra(BundleKeys.STAGE_JUMP, mContext.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(HistoryBillMonthStatusActivity.class, intent);

    }
}
