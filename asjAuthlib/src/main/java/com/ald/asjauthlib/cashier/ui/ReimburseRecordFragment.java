package com.ald.asjauthlib.cashier.ui;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.ReimburseRecordVM;
import com.ald.asjauthlib.databinding.FragmentReimburseRecordBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;

/**
 * Created by luckyliang on 2017/12/3.
 * 退款记录Fragment
 */

public class ReimburseRecordFragment extends AlaBaseFragment<FragmentReimburseRecordBinding> {

    ReimburseRecordVM reimburseRecordVM;

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_reimburse_record;
    }

    @Override
    public String getStatName() {
        return "退款记录片段";
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (reimburseRecordVM == null)
            reimburseRecordVM = new ReimburseRecordVM(this, cvb);
        cvb.setViewModel(reimburseRecordVM);
    }
}
