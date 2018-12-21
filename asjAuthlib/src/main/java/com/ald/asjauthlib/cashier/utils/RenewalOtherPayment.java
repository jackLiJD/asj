package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

/**
 * 续期其他支付方式
 * Created by sean yu on 2017/8/25.
 */
public class RenewalOtherPayment extends OtherPayment {
    public RenewalOtherPayment(Context context) {
        super(context);
    }

    @Override
    protected BankPayment generateBankPayment(Context context) {
        return new RenewalBankPayment(context);
    }

    @Override
    protected WxPayment generateWxPayment(Context context) {
        return new RenewalWxPayment(context);
    }

    @Override
    protected AliPayment generateAliPayment(Context context) {
        return new RenewalAliPayment(context);
    }

    @Override
    protected OutLinePayment generateOutlinePayment(Context context) {
        return new LoanOutLinePayment(context);
    }
}
