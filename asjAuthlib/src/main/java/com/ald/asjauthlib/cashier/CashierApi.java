package com.ald.asjauthlib.cashier;


import com.ald.asjauthlib.cashier.model.CashierModel;
import com.ald.asjauthlib.cashier.model.CashierNperListModel;
import com.ald.asjauthlib.cashier.model.CashierSubmitResponseModel;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 收银台相关接口
 * Created by ywd on 2017/10/30.
 */

public interface CashierApi {

    /**
     * 收银台下单
     */
    @POST("/cashier/pre")
    Observable<CashierSubmitResponseModel> pre(@Body JSONObject reqObj);


    /**
     * 收银台发起
     */
    @POST("/cashier/start")
    Observable<CashierModel> startRx(@Body JSONObject reqObj);

    /**
     * 收银台发起
     */
    @POST("/cashier/start")
    Call<CashierModel> start(@Body JSONObject reqObj);


    /**
     * 支付宝支付状态查询
     */
    @POST("/pay/alipayQueryOrder")
    Call<WxOrAlaPayModel> alipayQueryOrder(@Body JSONObject reqObj);

    @POST("/cashier/getInstallmentInfo")
    Observable<CashierNperListModel> getInstallmentInfo(@Body JSONObject reqObj);

    @POST("/order/pushTemplateMessages")
    Observable<ApiResponse> pushTemplateMessages(@Body JSONObject reqObj);
}
