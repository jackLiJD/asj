package com.ald.asjauthlib.cashier.ui;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.LoanRepaymentDetailVM;
import com.ald.asjauthlib.databinding.ActivityLoanRepaymentDetailBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/9
 * 描述：
 * 修订历史：
 */
public class LoanRepaymentDetailActivity extends AlaTopBarActivity<ActivityLoanRepaymentDetailBinding> {
    @Override
    public String getStatName() {
        return "借钱还款详情页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_loan_repayment_detail;
    }

    @Override
    protected void setViewModel() {
        cvb.setViewModel(new LoanRepaymentDetailVM(this));
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle("还款详情");
    }

}
