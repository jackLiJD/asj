package com.ald.asjauthlib.cashier.ui;

import android.os.Bundle;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.model.ConsumeDtlModel;
import com.ald.asjauthlib.cashier.viewmodel.StageDtlFrgVM;
import com.ald.asjauthlib.databinding.FragmentConsumeStageBinding;
import com.ald.asjauthlib.authframework.core.config.AlaBaseFragment;

/**
 * Created by luckyliang on 2017/12/11.
 * 消费明细，分期详情tab
 */
public class StageDtlFragment extends AlaBaseFragment<FragmentConsumeStageBinding> {

    private StageDtlFrgVM stageDtlFrgVM;

    public static StageDtlFragment newInstance() {
        return new StageDtlFragment();
    }

    @Override
    public String getStatName() {
        return "分期详情";
    }

    @Override
    public int getLayoutInflate() {
        return R.layout.fragment_consume_stage;
    }

    @Override
    public void initVariables(Bundle bundle) {
        super.initVariables(bundle);
    }

    @Override
    protected void initViews() {
        super.initViews();
        stageDtlFrgVM = new StageDtlFrgVM(this, cvb);
        cvb.setViewModel(stageDtlFrgVM);
    }

    public void fillData(ConsumeDtlModel model) {
        stageDtlFrgVM.fillData(model);
    }

    public void playAnimation() {
        stageDtlFrgVM.playAnimation();
    }
}
