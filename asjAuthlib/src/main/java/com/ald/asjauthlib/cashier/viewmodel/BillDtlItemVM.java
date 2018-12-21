package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.bindingadapter.view.ViewBindingAdapter;
import com.ald.asjauthlib.cashier.model.BillMonthModel;
import com.ald.asjauthlib.cashier.ui.ConsumeDtlActivity;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.BannerClickUtils;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.DateFormatter;
import com.ald.asjauthlib.authframework.core.vm.ViewModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by wjy on 2017/12/18.
 */

public class BillDtlItemVM implements ViewModel {

    private int itemType;

    public final static int ITEM_HEAD = 0;
    public final ObservableField<String> headText = new ObservableField<>();

    public final static int ITEM_BODY_DTL = 1;
    public final static int ITEM_BODY_AD = 2;
    public final ObservableField<String> displayName = new ObservableField<>();
    public final ObservableField<String> displayPayDate = new ObservableField<>();
    public final ObservableField<String> displayBillAmount = new ObservableField<>();
    public final ObservableField<String> displayPerInfo = new ObservableField<>();
    public final ObservableList<BannerModel> bannerModelList = new ObservableArrayList<>();
    public final ObservableField<ViewBindingAdapter.BannerListener> bannerListener = new ObservableField<>();
    public final ObservableInt bannerHeight = new ObservableInt();

    private int rid = -1;

    private Activity act;

    BillDtlItemVM(String status, BillMonthModel model, Activity act) {
        this.itemType = ITEM_HEAD;
        this.act = act;
        String price = "";
        if (null != model.getMoney())
            price = AppUtils.formatPriceDot(model.getMoney().floatValue());
        switch (status) {
            case "out":
            case "notOut":
                headText.set("共计" + price + "元");
                break;
            case "overdue":
                int count = model.getOverdueBillCount();
                headText.set("共逾期" + count + "笔, 共计" + price + "元");
                break;
            case "noInBill_Tip":
                int amountTip = model.getNotInCount();
                headText.set("未入账" + amountTip + "笔, 上滑展开全部");
                break;
            case "noInBill":
                int amount = model.getNotInCount();
                headText.set("未确认订单共" + amount + "笔, 确认后计入账单, 共" + model.getNotInMoney().toString() + "元");
                break;
        }
    }

    BillDtlItemVM(BillMonthModel.BillListBean bean, Activity act) {
        this.itemType = ITEM_BODY_DTL;
        this.act = act;
        rid = bean.getRid();
        displayName.set(bean.getName());
        displayBillAmount.set(bean.getBillAmount() + "元");
        displayPerInfo.set(bean.getBillNper() + "/" + bean.getNper() + "期");
        displayPayDate.set(new SimpleDateFormat(DateFormatter.TT.getValue(), Locale.CHINA).format(bean.getPayDate()));
    }

    BillDtlItemVM(BillMonthModel.BorrowBean bean, Activity act) {
        this.itemType = ITEM_BODY_DTL;
        this.act = act;
        rid = bean.getRid();
        displayName.set(bean.getName());
        displayBillAmount.set(bean.getAmount() + "元");
        displayPerInfo.set("等待确认收货");
        displayPayDate.set(new SimpleDateFormat(DateFormatter.TT.getValue(), Locale.CHINA).format(bean.getPayDate()));
    }

    BillDtlItemVM(final Activity act) {
        this.itemType = ITEM_BODY_AD;
        this.act = act;
        bannerHeight.set(AlaConfig.getResources().getDisplayMetrics().widthPixels * 90 / 375 + DensityUtils.getPxByDip(30));
        BannerClickUtils.setBannerSource(act, BannerClickUtils.TYPE_BILLING_DETAIL, bannerModelList, bannerListener, BannerClickUtils.TYPE_BANNER, new BannerClickUtils.ADdataListener() {
            @Override
            public void adDataCallBack(List<BannerModel> models, int position) {
            }
        });
    }

    public int getItemType() {
        return itemType;
    }

    void setHeadTxt(String headTxt) {
        headText.set(headTxt);
    }

    public void itemClick(View view) {
        if (-1 == rid) return;
        if ("等待确认收货".equals(displayPerInfo.get())) {
            UIUtils.showToast("该账单尚未收货，收货后可看详情");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.STAGE_BILL_ID, rid);
        intent.putExtra(BundleKeys.STAGE_JUMP, act.getIntent().getStringExtra(BundleKeys.STAGE_JUMP));
        ActivityUtils.push(ConsumeDtlActivity.class, intent);
    }
}
