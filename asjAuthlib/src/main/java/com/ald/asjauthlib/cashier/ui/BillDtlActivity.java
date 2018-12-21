package com.ald.asjauthlib.cashier.ui;

import android.util.TypedValue;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.viewmodel.BillDtlVM;
import com.ald.asjauthlib.databinding.ActivityBillDtlBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

/**
 * 已出账单、未出账单滑动 账单详情页
 */
public class BillDtlActivity extends AlaTopBarActivity<ActivityBillDtlBinding> {

    private BillsModel.BillListBean.BillsBean billsBean;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_bill_dtl;
    }

    @Override
    protected void afterOnCreate() {
        billsBean = getIntent().getParcelableExtra(BundleKeys.INTENT_KEY_HISTORY_BILL_YEAR);
    }

    @Override
    protected void setViewModel() {
        BillDtlVM billDtlVM = new BillDtlVM(this, billsBean, cvb);
        cvb.setViewModel(billDtlVM);
        getAppTopBar().setTitle("账单详情").setTitleSize(TypedValue.COMPLEX_UNIT_DIP, 17f);
    }


    @Override
    public String getStatName() {
        return "账单详情页";
    }
}
