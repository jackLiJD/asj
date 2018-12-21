package com.ald.asjauthlib.cashier.ui;

import android.os.Bundle;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.RepaymentDtlVM;
import com.ald.asjauthlib.databinding.ActivityRepaymentDtlBinding;
import com.ald.asjauthlib.utils.UIHelper;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

public class RepaymentDtlActivity extends AlaTopBarActivity<ActivityRepaymentDtlBinding> {

    RepaymentDtlVM repaymentDtlVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_repayment_dtl;
    }

    @Override
    protected void setViewModel() {
        long amountId = getIntent().getLongExtra(BundleKeys.INTENT_AMOUNT_ID, 0);
        repaymentDtlVM = new RepaymentDtlVM(amountId, this);
        cvb.setViewModel(repaymentDtlVM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.repayment_detail_title));
        setRightText(getResources().getString(R.string.btn_custom_service), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.telService(v.getContext(), getResources().getString(R.string.service_phone));
            }
        });
    }

    @Override
    public String getStatName() {
        return getString(R.string.repayment_detail_title);
    }
}
