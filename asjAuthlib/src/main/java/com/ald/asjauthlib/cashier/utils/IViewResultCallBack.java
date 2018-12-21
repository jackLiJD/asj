package com.ald.asjauthlib.cashier.utils;

/**
 * 视图回调
 * Created by sean yu on 2017/7/31.
 */
public interface IViewResultCallBack<T> {
    /**
     * 视图返回内容
     *
     * @param resultData 视图回调内容
     */
    void onViewResult(T resultData);
}

