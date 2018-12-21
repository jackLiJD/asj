package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.cashier.LoanApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.RenewalParams;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;

import retrofit2.Call;

/**
 * 续期微信支付
 * Created by sean yu on 2017/8/25.
 */
public class RenewalWxPayment extends WxPayment {
    private RenewalParams params;

    /**
     * @param context
     */
    public RenewalWxPayment(Context context) {
        super(context);
    }

    @Override
    protected void setPaymentParams(PaymentParams paymentParams) {
        this.params = (RenewalParams) paymentParams;
        params.cardId = "-1";
    }

    @Override
    protected Call<WxOrAlaPayModel> generateRDClient() {
        JSONObject object = params.getParams();
        if (ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel().equals(params.pageType)) {
            return RDClient.getService(LoanApi.class).confirmLegalRenewalPayV2(object);
        }
        if (ModelEnum.CASH_PAGE_TYPE_NEW_V1.getModel().equals(params.pageType)) {
            return RDClient.getService(LoanApi.class).confirmLegalRenewalPay(object);
        }
        return RDClient.getService(LoanApi.class).confirmRenewalPay(object);
    }
}
