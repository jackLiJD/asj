package com.ald.asjauthlib.cashier.ui;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.ConsumeDtlVM;
import com.ald.asjauthlib.databinding.ActivityConsumeDtlBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseActivity;

public class ConsumeDtlActivity extends AlaBaseActivity<ActivityConsumeDtlBinding> {

    private int rid = -1;
    private boolean fromHistoryBill = false;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_consume_dtl;
    }

    @Override
    protected void afterOnCreate() {
        rid = getIntent().getIntExtra(BundleKeys.STAGE_BILL_ID, -1);
        fromHistoryBill = getIntent().getBooleanExtra("history", false);
    }

    @Override
    protected void setViewModel(ActivityConsumeDtlBinding contentBinding) {
        ConsumeDtlVM consumeDtlVM = new ConsumeDtlVM(this, contentBinding, rid, !fromHistoryBill);
        cvb.setViewModel(consumeDtlVM);
    }

    @Override
    public String getStatName() {
        return getString(R.string.consume_dtl_title);
    }
}
