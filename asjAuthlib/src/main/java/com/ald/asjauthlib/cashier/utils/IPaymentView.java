package com.ald.asjauthlib.cashier.utils;



/**
 * 支付试图
 * Created by sean yu on 2017/7/31.
 */
public interface IPaymentView<T, R> {

    /**
     * 创建视图
     */
    void createView(T viewParams);

    /**
     * 刷新视图
     */
    void notifyView(T viewParams);

    /**
     * 销毁视图
     */
    void destroyView();

    /**
     * 视图回调
     */
    void ObserverView(IViewResultCallBack<R> callBack);

    /**
     * 视图回调
     */
    void UnObserverView(IViewResultCallBack<R> callBack);

}
