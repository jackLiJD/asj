package com.ald.asjauthlib.cashier;

import com.ald.asjauthlib.cashier.model.BrandOrderDetailUrlModel;
import com.ald.asjauthlib.cashier.model.MyTicketListModel;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 品牌相关接口API
 * Created by yaowenda on 17/3/27.
 */

public interface BrandApi {

    /**
     * 获取菠萝觅订单详情接口
     */
    @POST("/brand/getOrderDetailUrl")
    Call<BrandOrderDetailUrlModel> getOrderDetailUrl(@Body JSONObject requestObj);

    /**
     * 支付品牌订单
     */
    @POST("/brand/payOrderV1")
    Call<WxOrAlaPayModel> payOrder(@Body JSONObject requestObj);


    /**
     * 支付品牌订单(New)
     */
    @POST("/brand/payOrderV1")
    Observable<WxOrAlaPayModel> payOrderNew(@Body JSONObject requestObj);

    /**
     * 获取用户菠萝觅优惠券列表
     */
    @POST("/brand/getBrandCouponList")
    Call<MyTicketListModel> getBrandCouponList(@Body JSONObject requestObj);
}
