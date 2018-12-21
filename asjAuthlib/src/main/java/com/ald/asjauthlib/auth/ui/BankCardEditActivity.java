package com.ald.asjauthlib.auth.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.BankCardEditVM;
import com.ald.asjauthlib.databinding.ActivityEditBankCardBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 更换主副卡页面
 * Created by aladin on 2018/3/12.
 */

public class BankCardEditActivity extends AlaTopBarActivity<ActivityEditBankCardBinding> {


    @Override
    public String getStatName() {
        return "更换主副卡页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_edit_bank_card;
    }

    @Override
    protected void setViewModel() {
        Context context = this;
        cvb.setViewModel(new BankCardEditVM(context));
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.edit_main_card_title));
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.text_important_color));
    }
}
