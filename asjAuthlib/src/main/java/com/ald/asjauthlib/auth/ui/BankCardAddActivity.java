package com.ald.asjauthlib.auth.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.BankCardAddVM;
import com.ald.asjauthlib.databinding.ActivityBankCardAddBinding;
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
public class BankCardAddActivity extends AlaTopBarActivity<ActivityBankCardAddBinding> {

    private BankCardAddVM viewModel;

    @Override
    public String getStatName() {
        return "添加银行卡页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_bank_card_add;
    }

    @Override
    protected void setViewModel() {
        View view = findViewById(getLayoutInflate());
        viewModel = new BankCardAddVM(this, cvb, view);
        cvb.setViewModel(viewModel);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle("绑定银行卡");
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null)
            viewModel.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }
}
