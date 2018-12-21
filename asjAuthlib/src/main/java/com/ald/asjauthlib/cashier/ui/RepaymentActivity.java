package com.ald.asjauthlib.cashier.ui;

import android.content.Intent;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.RepaymentVM;
import com.ald.asjauthlib.databinding.ActivityRepaymentBinding;
import com.ald.asjauthlib.utils.UIHelper;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

public class RepaymentActivity extends AlaTopBarActivity<ActivityRepaymentBinding> {

    RepaymentVM repaymentVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_repayment;
    }

    @Override
    protected void setViewModel() {
        repaymentVM = new RepaymentVM(this, cvb);
        cvb.setViewModel(repaymentVM);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.repayment_title));
        setRightText(getResources().getString(R.string.btn_custom_service), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.telService(v.getContext(), v.getContext().getResources().getString(R.string.service_phone));
            }
        });

    }

    @Override
    public String getStatName() {
        return getResources().getString(R.string.repayment_title);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        repaymentVM.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repaymentVM.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (repaymentVM != null) {
            repaymentVM.onActivityResult(requestCode, resultCode, data);
        }
    }
}



