package com.ald.asjauthlib.cashier.order;

import android.content.Context;

import com.ald.asjauthlib.cashier.BrandApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.OrderPayParams;
import com.ald.asjauthlib.cashier.utils.PaymentParams;
import com.ald.asjauthlib.cashier.utils.WxPayment;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;

import retrofit2.Call;

/**
 * 订单
 * Created by sean yu on 2017/8/11.
 */

public class OrderWxPayment extends WxPayment {
    private OrderPayParams payParams;

    /**
     * @param context
     */
    public OrderWxPayment(Context context) {
        super(context);
    }

    @Override
    protected void setPaymentParams(PaymentParams paymentParams) {
        this.payParams = (OrderPayParams) paymentParams;

        payParams.payId = "-1";
        payParams.isCombinationPay = "N";
    }

    @Override
    protected Call<WxOrAlaPayModel> generateRDClient() {
        JSONObject object = payParams.getParams();
        return RDClient.getService(BrandApi.class).payOrder(object);
    }
}
