package com.ald.asjauthlib.cashier;

import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zhuangdaoyuan on 2018/4/9.
 * 安静撸码，淡定做人
 */

public interface ConfirmOrderApi {
    @POST("/pay/confirmPayment")
    Call<WxOrAlaPayModel> confirmPayment(@Body JSONObject jsonObject);

    @POST("/pay/quickPaymentResendCode")
    Call<ApiResponse> quickPaymentResendCode(@Body JSONObject jsonObject);
}
