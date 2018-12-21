package com.ald.asjauthlib.auth.ui;

import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.BankCardTypeVM;
import com.ald.asjauthlib.databinding.ActivityBankCardTypeBinding;
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
public class BankCardTypeActivity extends AlaTopBarActivity<ActivityBankCardTypeBinding> {

    @Override
    public String getStatName() {
        return "选择银行卡类型页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_bank_card_type;
    }

    @Override
    protected void setViewModel() {
        cvb.setViewModel( new BankCardTypeVM(this,cvb));
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle("选择银行卡类型");
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.text_important_color));
    }


}
