package com.ald.asjauthlib.cashier.ui;

import android.util.TypedValue;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.AllBillsVM;
import com.ald.asjauthlib.databinding.ActivityAllBillsBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

/*
 * Created by wjy on 2017/12/13.
 */

public class AllBillsActivity extends AlaTopBarActivity<ActivityAllBillsBinding> implements View.OnClickListener{

    private AllBillsVM allBillsVM;
    private int pageNo;

    @Override
    public String getStatName() {
        return "全部账单";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_all_bills;
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        getAppTopBar().setTitle("全部账单").setTitleSize(TypedValue.COMPLEX_UNIT_DIP, 17f);
        getAppTopBar().setRightTextOption("退还款记录", this).setRightTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
        pageNo = getIntent().getIntExtra(BundleKeys.STAGE_BILL_ID, 0);
    }

    @Override
    protected void setViewModel() {
        cvb.setViewModel(allBillsVM);
        allBillsVM = new AllBillsVM(this, cvb, pageNo);
    }

    @Override
    public void onClick(View v) {
        allBillsVM.topBarRightClick(v);
    }

}
