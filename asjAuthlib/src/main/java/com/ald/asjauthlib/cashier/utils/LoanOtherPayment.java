package com.ald.asjauthlib.cashier.utils;


import android.content.Context;


/**
 * 借钱还款其他支付方是
 * Created by sean yu on 2017/8/11.
 */

public class LoanOtherPayment extends OtherPayment {

    public LoanOtherPayment(Context context) {
        super(context);
    }

    @Override
    protected BankPayment generateBankPayment(Context context) {
        return new LoanBankPayment(context);
    }

    @Override
    protected WxPayment generateWxPayment(Context context) {
        return new LoanWxPayment(context);
    }

    @Override
    protected AliPayment generateAliPayment(Context context) {
        return new LoanAliPayment(context);
    }

    @Override
    protected OutLinePayment generateOutlinePayment(Context context) {
        return new LoanOutLinePayment(context);
    }
}
