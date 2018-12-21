package com.ald.asjauthlib.cashier.utils;



/**
 * 支付相关模块
 * Created by sean yu on 2017/7/31.
 */
public interface IPayment {

    /**
     * 初始化支付数据
     */
    void initPayment(PaymentParams params);

    /**
     * 注册支付信息监听
     */
    void ObserverPayInfo(IPaymentCallBack callBack);

    /**
     * 提交支付请求
     */
    void submit();

    /**
     * 取消支付信息监听
     */
    void UnObserverPayInfo(IPaymentCallBack callBack);

    /**
     * 获取支付相关视图
     *
     * @return 支付相关视图可能为null
     */
    IPaymentView getPaymentView();
}
