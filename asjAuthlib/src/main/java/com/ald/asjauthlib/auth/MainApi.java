package com.ald.asjauthlib.auth;

import com.ald.asjauthlib.auth.model.BannerListModel;
import com.ald.asjauthlib.auth.model.BrandUrlModel;
import com.ald.asjauthlib.auth.model.RedPacketModel;
import com.ald.asjauthlib.cashier.model.CashPageTypeModel;
import com.ald.asjauthlib.cashier.model.CouponCountModel;
import com.alibaba.fastjson.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/3/1 17:11
 * 描述：
 * 修订历史：
 */
public interface MainApi {


    /**
     * 获取品牌URL
     */
    @POST("/brand/getBrandUrl")
    Call<BrandUrlModel> getBrandUrl(@Body JSONObject requestObj);

    /**
     * 引流banner
     */
    @POST("/drainage/getBannerList")
    Call<BannerListModel> getBannerList(@Body JSONObject requestObj);

    /**
     * 拆红包
     */
    @POST("/borrowCash/tearPacket")
    Call<RedPacketModel> tearPacket(@Body JSONObject requestObj);
    /**
     * 新老借钱页面跳转判断V2
     */
    @POST("/legalborrowV2/getCashPageType")
    Call<CashPageTypeModel> getCashPageTypeV2();

    /**
     * 获取我的抵用券数量
     */
    @POST("/user/getMineCouponCount")
    Call<CouponCountModel> getMineCouponCount();
}
