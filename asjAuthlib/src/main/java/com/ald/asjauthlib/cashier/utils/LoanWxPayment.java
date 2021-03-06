package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.cashier.LoanApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.FinancePayParams;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;

import retrofit2.Call;

/**
 * 借钱还款微信支付模块
 * Created by sean yu on 2017/8/11.
 */

public class LoanWxPayment extends WxPayment {
    private FinancePayParams paymentParams;

    /**
     * @param context
     */
    public LoanWxPayment(Context context) {
        super(context);
    }

    @Override
    protected void setPaymentParams(PaymentParams paymentParams) {
        this.paymentParams = (FinancePayParams) paymentParams;

        this.paymentParams.cardId = "-1";
    }

    @Override
    protected Call<WxOrAlaPayModel> generateRDClient() {
        JSONObject object = paymentParams.getParams();
        if (Constant.LOAN_REPAYMENT_TYPE_PETTY == Integer.parseInt(paymentParams.repaymentType)) {
            //借钱合规V2
            if (ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel().equals(paymentParams.pageType)) {
                return RDClient.getService(LoanApi.class).repayDoV2(paymentParams.getParams());
            }
            //借钱合规V1
            if (ModelEnum.CASH_PAGE_TYPE_NEW_V1.getModel().equals(paymentParams.pageType)) {
                return RDClient.getService(LoanApi.class).repayDo(paymentParams.getParams());
            }
            return RDClient.getService(LoanApi.class).getConfirmRepayInfoNew(object);
            //白领贷
        } else {
            //正常还款
            if (Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR_COMMON.equals(paymentParams.repaymentTypeWhiteCollar)) {
                return RDClient.getService(LoanApi.class).loanRepayDo(paymentParams.getParams());
            }
            //提前结清
            else {
                return RDClient.getService(LoanApi.class).loanAllRepayDo(paymentParams.getParams());
            }
        }
    }
}
