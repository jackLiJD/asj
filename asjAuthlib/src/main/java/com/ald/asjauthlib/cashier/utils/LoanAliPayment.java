package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.cashier.PaymentApi;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.ald.asjauthlib.cashier.params.FinancePayParams;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;

import retrofit2.Call;

/**
 * 借钱支付宝还款
 * Created by sean yu on 2017/9/3.
 */
public class LoanAliPayment extends AliPayment {
    private FinancePayParams paymentParams;

    /**
     * @param context 上下文对象
     */
    public LoanAliPayment(Context context) {
        super(context, "");
    }

    @Override
    protected void setPaymentParams(PaymentParams paymentParams) {
        this.paymentParams = (FinancePayParams) paymentParams;

        this.paymentParams.cardId = "-3";
    }

    @Override
    protected Call<PaymentModel> generateRDClient() {
        JSONObject object = paymentParams.getParams();
        //小贷
        if (Constant.LOAN_REPAYMENT_TYPE_PETTY == Integer.parseInt(paymentParams.repaymentType)) {
            //借钱合规V2
            if (ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel().equals(paymentParams.pageType)) {
                return RDClient.getService(PaymentApi.class).repayDoV2(object);
            }
            //借钱合规V1
            if (ModelEnum.CASH_PAGE_TYPE_NEW_V1.getModel().equals(paymentParams.pageType)) {
                return RDClient.getService(PaymentApi.class).repayDo(object);
            }
            //老版本
            return RDClient.getService(PaymentApi.class).getConfirmRepayInfoV1(object);
            //白领贷
        } else {
            //正常还款
            if (Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR_COMMON.equals(paymentParams.repaymentTypeWhiteCollar)) {
                return RDClient.getService(PaymentApi.class).loanRepayDo(paymentParams.getParams());
            }
            //提前结清
            else {
                return RDClient.getService(PaymentApi.class).loanAllRepayDo(paymentParams.getParams());
            }
        }
    }
}
