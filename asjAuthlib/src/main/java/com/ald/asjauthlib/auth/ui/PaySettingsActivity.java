package com.ald.asjauthlib.auth.ui;

import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.viewmodel.PaySettingsVM;
import com.ald.asjauthlib.databinding.ActivityPaySettingsBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * 支付设置页面
 * Created by ywd on 2017/11/13.
 */

public class PaySettingsActivity extends AlaTopBarActivity<ActivityPaySettingsBinding> {
    PaySettingsVM paySettingsVM;
    @Override
    public String getStatName() {
        return "支付设置页面";
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_pay_settings;
    }

    @Override
    protected void setViewModel() {
        paySettingsVM=new PaySettingsVM(this,cvb);
        cvb.setViewModel(paySettingsVM);
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        setTitle(getResources().getString(R.string.pay_settings_title));
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.text_important_color));
    }
}
