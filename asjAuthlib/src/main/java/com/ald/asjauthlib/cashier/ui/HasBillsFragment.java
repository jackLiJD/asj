package com.ald.asjauthlib.cashier.ui;

import android.os.Bundle;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.viewmodel.HasBillsVM;
import com.ald.asjauthlib.databinding.FragmentHasBillBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;

/**
 * Created by wjy on 2017/12/14.
 */

public class HasBillsFragment extends AlaBaseFragment<FragmentHasBillBinding> {

    private HasBillsVM hasBillsVM;
    private int pageNo;

    public static HasBillsFragment newInstance(int pageNo) {
        HasBillsFragment hasBillsFragment= new HasBillsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.STAGE_BILL_ID, pageNo);
        hasBillsFragment.setArguments(bundle);
        return hasBillsFragment;
    }

    @Override
    public String getStatName() {
        return "全部账单之已出账单";
    }

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_has_bill;
    }

    @Override
    public void initVariables(Bundle bundle) {
        super.initVariables(bundle);
        pageNo = bundle.getInt(BundleKeys.STAGE_BILL_ID);
    }

    @Override
    protected void initViews() {
        hasBillsVM = new HasBillsVM(this, cvb);
        cvb.setViewModel(hasBillsVM);
        if (pageNo != 0) { // 如果一开始进入的时候不是当前页
            translationLatout();
        }
    }

    public void fillData(BillsModel billsModel) {
        if (null != hasBillsVM) hasBillsVM.fillData(billsModel);
    }

    public void recoverLayout() {
        if (null != cvb) cvb.recyclerView.setPadding(0, 0, 0, 0);
    }

    public void translationLatout() {
        if (null != cvb) cvb.recyclerView.setPadding(0, 0, DensityUtils.getPxByDip(9), 0);
    }
}
