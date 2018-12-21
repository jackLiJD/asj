package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

/**
 * Created by zhuangdaoyuan on 2018/3/22.
 * 安静撸码，淡定做人
 */

public abstract class OutLinePayment implements IPayment {
    private Context context;

    public OutLinePayment(Context context) {
        this.context = context;
    }

    @Override
    public void initPayment(PaymentParams params) {

    }

    @Override
    public void ObserverPayInfo(IPaymentCallBack callBack) {

    }

    @Override
    public void submit() {

    }

    @Override
    public void UnObserverPayInfo(IPaymentCallBack callBack) {

    }

    @Override
    public IPaymentView getPaymentView() {
        return null;
    }
}
