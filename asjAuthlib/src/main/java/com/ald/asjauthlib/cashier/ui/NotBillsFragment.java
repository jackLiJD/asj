package com.ald.asjauthlib.cashier.ui;

import android.os.Bundle;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.BillsModel;
import com.ald.asjauthlib.cashier.viewmodel.NotBillVM;
import com.ald.asjauthlib.databinding.FragmentNotBillBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.ScreenMatchUtils;

/*
 * Created by wjy on 2017/12/14.
 */

public class NotBillsFragment extends AlaBaseFragment<FragmentNotBillBinding> {

    private NotBillVM notBillVM;
    private int pageNo;
    private int adItemWidth, adItemHeight;

    public static NotBillsFragment newInstance(int pageNo) {
        NotBillsFragment notBillsFragment = new NotBillsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BundleKeys.STAGE_BILL_ID, pageNo);
        notBillsFragment.setArguments(bundle);
        return notBillsFragment;
    }

    @Override
    public String getStatName() {
        return "全部账单之已出账单";
    }

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_not_bill;
    }

    @Override
    public void initVariables(Bundle bundle) {
        super.initVariables(bundle);
        pageNo = bundle.getInt(BundleKeys.STAGE_BILL_ID);
    }

    @Override
    protected void initViews() {
        notBillVM = new NotBillVM(this, cvb);
        cvb.setViewModel(notBillVM);
        if (pageNo != 1) { // 如果一开始进入的时候不是当前页
            translationLatout();
        }
        adItemWidth = getResources().getDisplayMetrics().widthPixels - DensityUtils.getPxByDip(18) * 2;
        adItemHeight = ScreenMatchUtils.notBillsItemADHeight(adItemWidth);
    }

    public void fillData(BillsModel billsModel) {
        if (null != notBillVM) notBillVM.fillData(billsModel, adItemWidth, adItemHeight);
    }

    public void recoverLayout() {
        if (null != cvb) cvb.recyclerView.setPadding(0, 0, 0, 0);
    }

    public void translationLatout() {
        if (null != cvb) cvb.recyclerView.setPadding(DensityUtils.getPxByDip(9), 0, 0, 0);
    }

}
