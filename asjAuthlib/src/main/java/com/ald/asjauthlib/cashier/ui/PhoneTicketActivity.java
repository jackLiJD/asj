package com.ald.asjauthlib.cashier.ui;

import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.viewmodel.PhoneTicketVM;
import com.ald.asjauthlib.databinding.ActivityPhoneTicketBinding;
import com.ald.asjauthlib.authframework.core.AlaTopBarActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;


/**
 * 选择手机充值优惠券
 * Created by sean on 2017/3/7.
 */
public class PhoneTicketActivity extends AlaTopBarActivity<ActivityPhoneTicketBinding> {

    private PhoneTicketVM viewModel;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_phone_ticket;
    }

    @Override
    protected void setViewModel() {
        cvb.setViewModel(viewModel);
    }

    @Override
    public String getStatName() {
        return "手机充值还款优惠券";
    }

    @Override
    protected void afterOnCreate() {
        super.afterOnCreate();
        viewModel = new PhoneTicketVM(this,cvb);

        setTitle("选择优惠券");
        setTitleColor(ContextCompat.getColor(AlaConfig.getContext(),R.color.text_important_color));
    }
}
