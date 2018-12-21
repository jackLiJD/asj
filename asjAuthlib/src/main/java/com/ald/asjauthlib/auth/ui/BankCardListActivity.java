package com.ald.asjauthlib.auth.ui;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.BankCardListVM;
import com.ald.asjauthlib.databinding.ActivityBankCardListBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/9
 * 描述：
 * 修订历史：
 */
public class BankCardListActivity extends AlaTopBarActivity<ActivityBankCardListBinding> {

    private BankCardListVM viewModel;

    @Override
    public String getStatName() {
        return " 我的银行卡列表页面";
    }

    @Override
    protected int getLayoutInflate() {

        return R.layout.activity_bank_card_list;
    }

    @Override
    protected void setViewModel() {
        viewModel = new BankCardListVM(this);
        cvb.setViewModel(viewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (viewModel != null) {
            viewModel.load();
        } else {
            viewModel = new BankCardListVM(this);
            cvb.setViewModel(viewModel);
            viewModel.load();
        }
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle("银行卡管理", ContextCompat.getColor(this,R.color.text_white_color));
        setAppBarColor(ContextCompat.getColor(this, R.color.color_bg_manage_bankcard_title));

        setLeftImageNeedPadding(R.drawable.ic_white_arrow, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setImmersionBar(getAppBarLayout(),true);
    }


}
