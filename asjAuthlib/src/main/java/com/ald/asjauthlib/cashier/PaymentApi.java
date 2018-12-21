package com.ald.asjauthlib.cashier;

import com.ald.asjauthlib.cashier.model.OrderStatusModel;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.alibaba.fastjson.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 支付
 * Created by sean yu on 2017/9/7.
 */

public interface PaymentApi {

    /**
     * 借款还款支付
     */
    @POST("/repayCash/getConfirmRepayInfoV1")
    Call<PaymentModel> getConfirmRepayInfoV1(@Body JSONObject jsonObject);

    /**
     * 还款-合规
     *
     * @param requestObj
     * @return
     */
    @POST("/legalborrow/repayDo")
    Call<PaymentModel> repayDo(@Body JSONObject requestObj);

    /**
     * 还款-合规V2
     *
     * @param requestObj
     * @return
     */
    @POST("/legalborrowV2/repayDo")
    Call<PaymentModel> repayDoV2(@Body JSONObject requestObj);

    /**
     * 续期支付
     *
     * @param jsonObject
     * @return
     */
    @POST("/borrowCash/confirmRenewalPayV1")
    Call<PaymentModel> confirmRenewalPayV1(@Body JSONObject jsonObject);

    /**
     * 续期确认支付-合规
     *
     * @param requestObj
     * @return
     */
    @POST("/legalborrow/confirmLegalRenewalPay")
    Call<PaymentModel> confirmLegalRenewalPay(@Body JSONObject requestObj);

    /**
     * 续期确认支付-合规V2
     *
     * @param requestObj
     * @return
     */
    @POST("/legalborrowV2/confirmLegalRenewalPay")
    Call<PaymentModel> confirmLegalRenewalPayV2(@Body JSONObject requestObj);



    /**
     * 分期还款支付(易宝)
     */
    @POST("/borrow/submitRepaymentByYiBao")
    Call<PaymentModel> submitRepaymentByYiBao(@Body JSONObject jsonObject);

    /**
     * 提交提前结清(4.0.4)
     */
    @POST("/borrow/submitClear")
    Call<PaymentModel> submitClear(@Body JSONObject jsonObject);

    /**
     * 白领贷按期还款
     *
     * @param reqObj
     * @return
     */
    @POST("/loan/loanRepayDo")
    Call<PaymentModel> loanRepayDo(@Body JSONObject reqObj);

    /**
     * 白领贷提前还款
     *
     * @param reqObj
     * @return
     */
    @POST("/loan/loanAllRepayDo")
    Call<PaymentModel> loanAllRepayDo(@Body JSONObject reqObj);

    /**
     * 根据订单状态获取订单信息
     *
     * @param jsonObject
     * @return
     */
    @POST("/repayCash/getRepayCashByOrderId")
    Call<OrderStatusModel> getRepayCashByOrderId(@Body JSONObject jsonObject);
}
