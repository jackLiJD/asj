package com.ald.asjauthlib.cashier.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;

/**
 * Created by zhuangdaoyuan on 2018/5/8.
 * 安静撸码，淡定做人
 */
public class ButtonItemVM extends BaseModel {

    public final ObservableBoolean btnEnable = new ObservableBoolean();
    private Activity mContext;

    public ButtonItemVM(Activity context, MyTicketModel myTicketModel) {
        mContext = context;
        btnEnable.set(myTicketModel != null);
    }

    /**
     * 取消使用优惠券
     *
     * @param view
     */
    public void cancelSelectCoupon(View view) {
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.COUPON_LIST_DATA_CANCEL, true);
        intent.putExtra(BundleKeys.COUPON_LIST_DATA_RESULT, "");
        ActivityUtils.pop(mContext, intent);
    }

}
