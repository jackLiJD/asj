package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.cashier.PaymentApi;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.ald.asjauthlib.cashier.params.RenewalParams;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;

import retrofit2.Call;

/**
 * 续期支付宝支付
 * Created by sean yu on 2017/9/3.
 */

public class RenewalAliPayment extends AliPayment {
    private RenewalParams params;

    /**
     * @param context
     */
    public RenewalAliPayment(Context context) {
        super(context, ModelEnum.SOURCE_TYPE_RENEWAL.getModel());
    }

    @Override
    protected void setPaymentParams(PaymentParams paymentParams) {
        this.params = (RenewalParams) paymentParams;

        this.params.cardId = "-3";
    }

    @Override
    protected Call<PaymentModel> generateRDClient() {
        JSONObject object = params.getParams();
        if (ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel().equals(params.pageType)) {
            return RDClient.getService(PaymentApi.class).confirmLegalRenewalPayV2(object);
        }
        if (ModelEnum.CASH_PAGE_TYPE_NEW_V1.getModel().equals(params.pageType)) {
            return RDClient.getService(PaymentApi.class).confirmLegalRenewalPay(object);
        }
        return RDClient.getService(PaymentApi.class).confirmRenewalPayV1(object);
    }
}
