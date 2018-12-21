package com.ald.asjauthlib.cashier.order;

import android.content.Context;

import com.ald.asjauthlib.cashier.PaymentApi;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.ald.asjauthlib.cashier.params.OrderPayParams;
import com.ald.asjauthlib.cashier.utils.AliPayment;
import com.ald.asjauthlib.cashier.utils.PaymentParams;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;

import retrofit2.Call;

/**
 * Created by sean yu on 2017/9/3.
 */

public class OrderAliPayment extends AliPayment {

    private OrderPayParams payParams;

    /**
     * @param context
     */
    public OrderAliPayment(Context context) {
        super(context,"");
    }

    @Override
    protected void setPaymentParams(PaymentParams paymentParams) {
        this.payParams = (OrderPayParams) paymentParams;

        payParams.payId = "-1";
        payParams.isCombinationPay = "N";
    }

    @Override
    protected Call<PaymentModel> generateRDClient() {
        JSONObject object = payParams.getParams();
        return RDClient.getService(PaymentApi.class).getConfirmRepayInfoV1(object);
    }
}
