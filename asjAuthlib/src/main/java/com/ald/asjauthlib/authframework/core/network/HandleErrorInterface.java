package com.ald.asjauthlib.authframework.core.network;

import android.content.Intent;

import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.network.exception.ApiExceptionEnum;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.alibaba.fastjson.JSONException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

/**
 * Created by Yangyang on 2018/7/31.
 * desc:不管是BaseObserver还是RequestCallBack,统一处理错误码
 */

public interface HandleErrorInterface {

    default void commonHandleError(Throwable t) {
        if (t instanceof ApiException) {
            ApiException apiException = (ApiException) t;
            int code = apiException.getCode();
            //消息提醒
            if (code != ApiExceptionEnum.SUCCESS.getErrorCode()) {
                if (ApiExceptionEnum.REQUEST_INVALID_SIGN_ERROR.getErrorCode() == code) {
                    //兼容后台在1005时的用户不存在的错误
                    if (ApiExceptionEnum.USER_NOT_EXIST_ERROR.getDesc().equals(apiException.getMsg())) {
                        if (!errorHandle(apiException)) {
                            UIUtils.showToast(apiException.getMsg());
                        }
                        t.printStackTrace();
                    } else {
                        Intent broadcastIntent = new Intent(AlaConfig.ACTION_API_OPEN);
                        broadcastIntent.putExtra(AlaConfig.EXTRA_ERR_CODE, code);
                        AlaConfig.getLocalBroadcastManager().sendBroadcast(broadcastIntent);
                    }
                } else if (code == ApiExceptionEnum.EMPTY.getErrorCode()
                        || code == ApiExceptionEnum.DEALWITH_YOUDUN_NOTIFY_ERROR.getErrorCode()) {
                    //不做toast提醒
                    t.printStackTrace();
                } else if (code == ApiExceptionEnum.USER_PWD_INPUT_ERROR.getErrorCode()
                        || code == ApiExceptionEnum.USER_PWD_FORBID.getErrorCode()) {
                    Intent broadcastIntent = new Intent(AlaConfig.ACTION_API_OPEN);
                    broadcastIntent.putExtra(AlaConfig.EXTRA_ERR_CODE, code);
                    broadcastIntent.putExtra(AlaConfig.EXTRA_ERR_MSG, apiException.getMsg());
                    AlaConfig.getLocalBroadcastManager().sendBroadcast(broadcastIntent);
                } else if (code > ApiExceptionEnum.REQUEST_PARAM_METHOD_NOT_EXIST.getErrorCode()
                        && code != ApiExceptionEnum.REQUEST_INVALID_SIGN_ERROR.getErrorCode() && code != ApiExceptionEnum.REQUEST_PARAM_TOKEN_ERROR.getErrorCode()) {
                    if (code != 1130) {    // 1130 设备不可信，登录的时候去验证登录码
                        if (!errorHandle(apiException)) {
                            UIUtils.showToast(apiException.getMsg());
                        }
                    }
                } else if (code == ApiExceptionEnum.REQUEST_PARAM_TOKEN_ERROR.getErrorCode()) {
                    Intent broadcastIntent = new Intent(AlaConfig.ACTION_API_OPEN);
                    broadcastIntent.putExtra(AlaConfig.EXTRA_ERR_CODE, code);
                    AlaConfig.getLocalBroadcastManager().sendBroadcast(broadcastIntent);
                } else {
                    if (!errorHandle(apiException)) {
                        UIUtils.showToast(apiException.getMsg());
                    }
                }
            }
            onFailure(apiException);
        } else if (t instanceof HttpException) {
//            if (AlaConfig.isDebug())
//            UIUtils.showToast("服务器异常");

        } else if (t instanceof JSONException) {
            if (AlaConfig.isDebug())
                UIUtils.showToast("解析异常");
        } else if (t instanceof ConnectTimeoutException || t instanceof SocketTimeoutException) {
            UIUtils.showToast("连接超时");
        } else if (t instanceof UnknownServiceException) {
            if (AlaConfig.isDebug())
                UIUtils.showToast("无法解析该域名");
        } else if (t instanceof IOException) {
            //没网络会回调ConnectException或者UnknownHostException,取消加载框时取消网络会回调SocketException或者IOException,不用提示
            if (t instanceof ConnectException || t instanceof UnknownHostException)
                UIUtils.showToast("连接失败，请检查网络");
        } else {
            Logger.d("error", t.getMessage());
        }
        t.printStackTrace();
    }

    default void onFailure(ApiException apiException) {

    }


    /**
     * @param apiException
     * @return
     */
    default boolean errorHandle(ApiException apiException) {
        return false;
    }
}
