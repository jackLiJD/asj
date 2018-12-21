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
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

/*
 * Created by wjy on 2017/12/14.
 */

public class NotBillItemVM extends BillsVM implements ViewModel {

    private int itemType;
    private int index;
    private Activity context;

    public static final int ITEM_HEAD_YEAR = 0;
    public static final int ITEM_HEAD_AD = 1;
    public final ObservableField<String> year = new ObservableField<>();
    public final ObservableField<String> adTitle = new ObservableField<>();

    public static final int ITEM_BODY_Bill = 2;
    public static final int ITEM_BODY_AD = 3;
    public final ObservableField<String> month = new ObservableField<>();
    public final ObservableField<String> billPrice = new ObservableField<>();

    private BillsModel.BillListBean.BillsBean billsBean;
    private BannerModel model;

    public NotBillItemVM(BillsModel.BillListBean bean, Activity activity) {
        this.itemType = ITEM_HEAD_YEAR;
        this.context = activity;
        year.set(bean.getYear() + "");
    }

    public NotBillItemVM(BillsModel.BillListBean.BillsBean billsBean, Activity activity) {
        this.itemType = ITEM_BODY_Bill;
        this.billsBean = billsBean;
        this.context = activity;
        month.set(billsBean.getBillMonth() + "月");
        billPrice.set(billsBean.getBillAmount() + "");
    }

    public NotBillItemVM(Activity activity) {
        this.itemType = ITEM_HEAD_AD;
        this.context = activity;
        adTitle.set("看看这些精品推荐");
    }

    public NotBillItemVM(Activity context, BannerModel model, int adItemWidth, int adItemHeight, int index) {
        this.context = context;
        this.itemType = ITEM_BODY_AD;
        this.model = model;
        this.index = index;
        adImage.set(model.getImageUrl());
        adWidth.set(adItemWidth);
        adHeight.set(adItemHeight);
    }

    public Drawable imageStatus() {
        Context context = ActivityUtils.peek();
        return ContextCompat.getDrawable(AlaConfig.getContext(), R.drawable.not_bill); // 已出账
    }

    public void noBillsItemClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_KEY_HISTORY_BILL_YEAR, billsBean);
        intent.putExtra(BundleKeys.STAGE_JUMP, context.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(BillDtlActivity.class, intent);
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public void adClickListener(View view) {
        if (null == model) return;
        BannerClickUtils.unifySkip(model);
    }
}
