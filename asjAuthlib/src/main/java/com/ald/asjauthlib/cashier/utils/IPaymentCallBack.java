package com.ald.asjauthlib.cashier.utils;

/**
 * 支付回调
 * Created by sean yu on 2017/7/31.
 */

public interface IPaymentCallBack {

    /**
     * 开始支付
     */
    void onStart();

    /**
     * 支付处理
     */
    void onHandle();

    /**
     * 支付成功
     */
    <T> void onSuccess(T t);

    /**
     * 支付失败
     */
    void onCancel(Throwable t);
}
