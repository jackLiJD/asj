package com.ald.asjauthlib.cashier;


import com.ald.asjauthlib.cashier.model.CouponListModel;
import com.ald.asjauthlib.cashier.model.LoanRepaymentDetailModel;
import com.ald.asjauthlib.cashier.model.RefundResponse;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by sean yu on 2017/3/27.
 */
public interface LoanApi {


    /**
     * 提交借款申请
     */
    @POST("/borrowCash/applyBorrowCashV1")
    Call<ApiResponse> applyBorrowCash(@Body JSONObject jsonObject);

    /**
     * 提交借款申请-合规V1
     */
    @POST("/legalborrow/applyLegalBorrowCash")
    Call<ApiResponse> applyLegalBorrowCash(@Body JSONObject reqObj);

    /**
     * 提交借款申请-合规V2
     */
    @POST("/legalborrowV2/applyLegalBorrowCash")
    Call<ApiResponse> applyLegalBorrowCashV2(@Body JSONObject reqObj);



    /**
     * 借钱还款
     */
    @POST("/repayCash/getConfirmRepayInfo")
    Call<WxOrAlaPayModel> getConfirmRepayInfo(@Body JSONObject object);

    /**
     * 借钱还款
     */
    @POST("/repayCash/getConfirmRepayInfoV1")
    Call<WxOrAlaPayModel> getConfirmRepayInfoNew(@Body JSONObject object);

    @GET
    Call<String> redirectUri(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 续期确认支付
     *
     * @param requestObj
     * @return
     */
    @POST("/borrowCash/confirmRenewalPay")
//    Call<RenewalConfirmModel> confirmRenewalPay(@Body JSONObject requestObj);
    Call<WxOrAlaPayModel> confirmRenewalPay(@Body JSONObject requestObj);

    /**
     * 续期确认支付-合规
     *
     * @param requestObj
     * @return
     */
    @POST("/legalborrow/confirmLegalRenewalPay")
    Call<WxOrAlaPayModel> confirmLegalRenewalPay(@Body JSONObject requestObj);

    /**
     * 续期确认支付-合规
     *
     * @param requestObj
     * @return
     */
    @POST("/legalborrowV2/confirmLegalRenewalPay")
    Call<WxOrAlaPayModel> confirmLegalRenewalPayV2(@Body JSONObject requestObj);

    /**
     * 立即借款提交时借款协议
     */
    @POST("/fanbei-web/protocolCashLoan")
    Call<ApiResponse> protocolCashLoan(@Body JSONObject jsonObject);

    /**
     * 还款-合规
     *
     * @param requestObj
     * @return
     */
    @POST("/legalborrow/repayDo")
    Call<WxOrAlaPayModel> repayDo(@Body JSONObject requestObj);

    /**
     * 还款-合规V2
     *
     * @param requestObj
     * @return
     */
    @POST("/legalborrowV2/repayDo")
    Call<WxOrAlaPayModel> repayDoV2(@Body JSONObject requestObj);

    /**

    /**
     * 白领贷按期还款
     *
     * @param reqObj
     * @return
     */
    @POST("/loan/loanRepayDo")
    Call<WxOrAlaPayModel> loanRepayDo(@Body JSONObject reqObj);

    /**
     * 白领贷提前还款
     *
     * @param reqObj
     * @return
     */
    @POST("/loan/loanAllRepayDo")
    Call<WxOrAlaPayModel> loanAllRepayDo(@Body JSONObject reqObj);

    /**
     * 极速贷还款方式--支付宝，微信，线下配置
     */
    @POST("/repayment/getPayTypeStatus")
    Call<RefundResponse> getPayTypeStatus(@Body JSONObject reqObj);

    @POST("/user/getUserCounponListType")
    Call<CouponListModel> getUserCounponListType(@Body JSONObject object);

    /**
     * 借款模块-还款详情页面
     *
     * @param object
     * @return
     */
    @POST("/repayCash/getRepayCashInfo")
    Call<LoanRepaymentDetailModel> getRepayCashInfo(@Body JSONObject object);
}
