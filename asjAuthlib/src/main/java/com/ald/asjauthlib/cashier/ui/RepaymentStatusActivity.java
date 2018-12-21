package com.ald.asjauthlib.cashier.ui;


import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.RepayStatusVM;
import com.ald.asjauthlib.databinding.ActivityRepaymentStatusBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseActivity;

public class RepaymentStatusActivity extends AlaBaseActivity<ActivityRepaymentStatusBinding> {

    RepayStatusVM repayStatusVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_repayment_status;
    }

    @Override
    protected void setViewModel(ActivityRepaymentStatusBinding contentBinding) {
        repayStatusVM = new RepayStatusVM(this);
        cvb.setViewModel(repayStatusVM);
    }

    @Override
    public String getStatName() {
        return "还款结果页";
    }

    @Override
    public void onBackPressed() {
        repayStatusVM.onBackClick(null);
    }


}
