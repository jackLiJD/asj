package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.SettlePayParams;
import com.ald.asjauthlib.cashier.params.StagePayParams;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;

import retrofit2.Call;

/**
 * 分期还款微信支付
 * Created by sean yu on 2017/8/11.
 */
public class StageWxPayment extends WxPayment {
    private StagePayParams paymentParams;

    private SettlePayParams settlePayParams;

    /**
     * @param context
     */
    public StageWxPayment(Context context) {
        super(context);
    }

    @Override
    protected void setPaymentParams(PaymentParams paymentParams) {

        if (paymentParams instanceof SettlePayParams) {
            this.settlePayParams = (SettlePayParams) paymentParams;
            this.settlePayParams.cardId = "-1";

        } else {
            this.paymentParams = (StagePayParams) paymentParams;
            this.paymentParams.cardId = "-1";
        }
    }

    @Override
    protected Call<WxOrAlaPayModel> generateRDClient() {
        if (settlePayParams != null) {
            JSONObject object = new JSONObject();
            object.put("billId", settlePayParams.getBillId());
            object.put("repayAmount", settlePayParams.getRepayAmount());
            object.put("rabteAmount", "");
            object.put("payPwd", "123456");
            object.put("couponId", "");
            object.put("cardId", -1);
            return RDClient.getService(BusinessApi.class).submitClear(object);

        } else {
            JSONObject object = paymentParams.getParams();
            return RDClient.getService(BusinessApi.class).submitRepaymentByYiBao(object);
        }
    }
}
