package com.ald.asjauthlib.cashier.ui;


import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.CashierPayFailVM;
import com.ald.asjauthlib.databinding.ActivityCashierPayFailBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

public class CashierPayFailActivity extends AlaTopBarActivity<ActivityCashierPayFailBinding> {

    CashierPayFailVM cashierPayFailVM;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_cashier_pay_fail;
    }

    @Override
    protected void setViewModel() {
        cashierPayFailVM = new CashierPayFailVM(this);
        cvb.setViewModel(cashierPayFailVM);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(" ");
        showTopLine();
        setLeftText("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public String getStatName() {
        return "购买权限包";
    }

    @Override
    public void onBackPressed() {
        //返回订单详情或是订单列表
        cashierPayFailVM.onBackPressed();
    }
}
