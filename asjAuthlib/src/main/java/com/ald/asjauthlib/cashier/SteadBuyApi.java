package com.ald.asjauthlib.cashier;

import com.ald.asjauthlib.cashier.model.SteadBuyModel;
import com.alibaba.fastjson.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 代买接口API
 * Created by yaowenda on 17/4/13.
 */

public interface SteadBuyApi {



    /**
     * 请求分期信息
     */
    @POST("/agencyBuy/getAgencyNperInfo")
    Call<SteadBuyModel> getAgencyNperInfo(@Body JSONObject reqObj);

}
