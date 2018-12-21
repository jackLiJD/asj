package com.ald.asjauthlib.cashier.ui;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.RepaymentRecordVM;
import com.ald.asjauthlib.databinding.FragmentRepaymentRecordBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;

/**
 * Created by luckyliang on 2017/12/3.
 * 还款Fragment
 */

public class RepaymentRecordFragment extends AlaBaseFragment<FragmentRepaymentRecordBinding> {

    RepaymentRecordVM repaymentRecordVM;


    @Override
    protected void initViews() {
        super.initViews();
        if (repaymentRecordVM == null)
            repaymentRecordVM = new RepaymentRecordVM(this, cvb);
        cvb.setViewModel(repaymentRecordVM);
    }

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_repayment_record;
    }

    @Override
    public String getStatName() {
        return null;
    }
}
