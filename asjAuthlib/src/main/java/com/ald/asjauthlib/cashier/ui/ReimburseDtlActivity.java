package com.ald.asjauthlib.cashier.ui;

import android.os.Bundle;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.ReimburseDtlVM;
import com.ald.asjauthlib.databinding.ActivityReimburseDtlBinding;
import com.ald.asjauthlib.utils.UIHelper;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;


public class ReimburseDtlActivity extends AlaTopBarActivity<ActivityReimburseDtlBinding> {

    ReimburseDtlVM reimburseDtlVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_reimburse_dtl;
    }

    @Override
    protected void setViewModel() {
        reimburseDtlVM = new ReimburseDtlVM(getIntent().getLongExtra(BundleKeys.INTENT_AMOUNT_ID, 0), this);
        cvb.setViewModel(reimburseDtlVM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.reimburse_detail_title));
        setRightText(getResources().getString(R.string.btn_custom_service), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.telService(v.getContext(), getResources().getString(R.string.service_phone));
            }
        });

    }

    @Override
    public String getStatName() {
        return getResources().getString(R.string.reimburse_detail_title);
    }
}
