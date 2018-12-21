package com.ald.asjauthlib.cashier.ui;

import android.os.Bundle;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.ConsumeDtlModel;
import com.ald.asjauthlib.cashier.viewmodel.ConsumDtlFrgVM;
import com.ald.asjauthlib.databinding.FragmentConsumeDtlBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;

/**
 * Created by luckyliang on 2017/12/11.
 */

public class ConsumeDtlFragment extends AlaBaseFragment<FragmentConsumeDtlBinding> {

    private ConsumDtlFrgVM consumDtlFrgVM;

    public static ConsumeDtlFragment newInstance() {
        return new ConsumeDtlFragment();
    }

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_consume_dtl;
    }

    @Override
    public String getStatName() {
        return "消费详情";
    }

    @Override
    public void initVariables(Bundle bundle) {
        super.initVariables(bundle);
    }

    @Override
    protected void initViews() {
        super.initViews();
        consumDtlFrgVM = new ConsumDtlFrgVM(this);
        cvb.setViewModel(consumDtlFrgVM);
    }

    public void fillData(ConsumeDtlModel model) {
        consumDtlFrgVM.fillData(model);
    }
}
