package com.ald.asjauthlib.utils;

/**
 * 魔蝎认证回调
 * Created by sean yu on 2017/7/10.
 */

public interface MxAuthCallBack {

    /**
     * 认证成功
     */
    void authSuccess(String authCode, String msg);

    /**
     * 认证失败
     */
    void authError(String authCode, String errorMsg);
}
