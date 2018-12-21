package com.ald.asjauthlib.cashier.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.StageRefundVM;
import com.ald.asjauthlib.databinding.ActivityStageRefundBinding;
import com.ald.asjauthlib.utils.Utils;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/9
 * 描述：
 * 修订历史：
 */
public class StageRefundActivity extends AlaTopBarActivity<ActivityStageRefundBinding> {

    private StageRefundVM viewModel;

    @Override
    public String getStatName() {
        return "还款页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_stage_refund;
    }

    @Override
    protected void setViewModel() {
        viewModel = new StageRefundVM(this);
        cvb.setViewModel(viewModel);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle("还款");
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));
        int repaymentType = getIntent().getIntExtra(BundleKeys.REPAYMENT_TYPE, Constant.LOAN_REPAYMENT_TYPE_PETTY);
        //只有小贷显示其他还款方式
        if (Constant.LOAN_REPAYMENT_TYPE_PETTY == repaymentType) {
            setRightText(getResources().getString(R.string.cash_loan_other_repayment), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.jumpH5byPinH5(Constant.H5_OTHER_REPAYMENT);
                }
            }, ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cvb.getViewModel().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.unObserverPayInfo();
    }
}
