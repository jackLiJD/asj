package com.ald.asjauthlib.cashier.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.RefundRecordVM;
import com.ald.asjauthlib.databinding.ActivityRefundRecordBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

//退还款记录
public class RefundRecordActivity extends AlaBaseActivity<ActivityRefundRecordBinding> {

    RefundRecordVM refundRecordVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_refund_record;
    }

    @Override
    protected void setViewModel(ActivityRefundRecordBinding contentBinding) {
        refundRecordVM = new RefundRecordVM(this, getSupportFragmentManager(), cvb);
        cvb.setViewModel(refundRecordVM);

    }


    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return super.getLayoutInflater();
    }

    @Override
    public String getStatName() {
        return getResources().getString(R.string.refund_crecord_title);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.refund_crecord_title));
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BundleKeys.REQUEST_CODE_CALENDER) {
            int year = data.getIntExtra(BundleKeys.INTENT_KEY_REFUND_YEAR, 0);
            int month = data.getIntExtra(BundleKeys.INTENT_KEY_REFUND_MONTH, 0);
            refundRecordVM.reloadRecord(year, month);
        }
    }

}
