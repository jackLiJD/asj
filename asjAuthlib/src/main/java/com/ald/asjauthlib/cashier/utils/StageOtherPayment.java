package com.ald.asjauthlib.cashier.utils;


import android.content.Context;


/**
 * 分期还款其他支付方式
 * Created by sean yu on 2017/8/11.
 */

public class StageOtherPayment extends OtherPayment {

    public StageOtherPayment(Context context) {
        super(context);
    }

    @Override
    protected BankPayment generateBankPayment(Context context) {
        return new StageBankPayment(context);
    }

    @Override
    protected WxPayment generateWxPayment(Context context) {
        return new StageWxPayment(context);
    }

    @Override
    protected AliPayment generateAliPayment(Context context) {
        return new StageAliPayment(context);
    }

    @Override
    protected OutLinePayment generateOutlinePayment(Context context) {
        return new LoanOutLinePayment(context);
    }
}
