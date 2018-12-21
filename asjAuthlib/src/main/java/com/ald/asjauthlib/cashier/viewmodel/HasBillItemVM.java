package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.ui.BillDtlActivity;
import com.ald.asjauthlib.cashier.ui.HistoryBillListActivity;
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.log.DateFormatter;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by wjy on 2017/12/14.
 */

public class HasBillItemVM extends BillsVM implements ViewModel {

    public final static int ITEM_HEAD_YEAR = 0;
    public final ObservableField<String> year = new ObservableField<>();

    public final static int ITEM_BODY_BILL = 1;
    public static final int ITEM_BODY_AD = 2;
    public final ObservableField<String> month = new ObservableField<>();
    public final ObservableField<String> billPrice = new ObservableField<>();
    public final ObservableField<String> overdue = new ObservableField<>();

    public final static int ITEM_FOOT = 4;

    private int itemType;
    private Activity context;
    private int index;

    private BillsModel.BillListBean.BillsBean billsBean;

    private BannerModel model;

    public HasBillItemVM(int itemType, Activity context) { // 底部
        this.context = context;
        this.itemType = itemType;
    }

    public HasBillItemVM(BillsModel.BillListBean bean, int itemType, Activity context) { // 年份头
        this.context = context;
        this.itemType = itemType;
        year.set(bean.getYear() + "");
    }

    public HasBillItemVM(BillsModel.BillListBean.BillsBean billsBean, int itemType, Activity context) { // 该年的月份出账信息
        this.context = context;
        this.billsBean = billsBean;
        this.itemType = itemType;
        month.set(billsBean.getBillMonth() + "月");
        billPrice.set(billsBean.getBillAmount() + "");
    }

    public HasBillItemVM(Activity context, int index, BannerModel model, int itemType, int adWidth, int adHeight, int paddingBottom, int paddingRight, int paddingLeft) { // 广告位
        this.context = context;
        this.index = index;
        this.itemType = itemType;
        this.model = model;
        adImage.set(model.getImageUrl());
        this.adWidth.set(adWidth);
        this.adHeight.set(adHeight);
        this.paddingRight.set(paddingRight);
        this.paddingBottom.set(paddingBottom);
        this.paddingLeft.set(paddingLeft);
    }

    public Drawable imageStatus() {
        Context context = ActivityUtils.peek();
        if (ModelEnum.Y.getModel().equals(billsBean.getOverdueStatus())) { // 已逾期
            overdue.set("包含逾期利息 " + billsBean.getOverdueInterestAmount() + "元");
            return ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.overdue);
        }
        overdue.set("最后还款日" + new SimpleDateFormat(DateFormatter.AA.getValue(), Locale.CHINA).format(billsBean.getGmtPayTime()));
        return ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.has_bill); // 已出账
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void historyClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_JUMP, context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(HistoryBillListActivity.class);
    }

    public void hasBillsItemClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_KEY_HISTORY_BILL_YEAR, billsBean);
        intent.putExtra(BundleKeys.STAGE_JUMP, context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(BillDtlActivity.class, intent);
    }

    @Override
    public void adClickListener(View view) {
        if (null == model) return;
        BannerClickUtils.unifySkip(model);
    }
}
