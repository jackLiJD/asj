package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.cashier.PaymentApi;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.ald.asjauthlib.cashier.params.SettlePayParams;
import com.ald.asjauthlib.cashier.params.StagePayParams;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;

import retrofit2.Call;

/**
 * 分期还款支付宝支付
 * Created by sean yu on 2017/9/3.
 */

public class StageAliPayment extends AliPayment {
    private StagePayParams paymentParams;

    private SettlePayParams settlePayParams;

    /**
     * @param context
     */
    public StageAliPayment(Context context) {
        super(context, "");
    }

    @Override
    protected void setPaymentParams(PaymentParams paymentParams) {
        if (paymentParams instanceof SettlePayParams) {
            this.settlePayParams = (SettlePayParams) paymentParams;
            this.settlePayParams.cardId = "-3";

        } else {
            this.paymentParams = (StagePayParams) paymentParams;
            this.paymentParams.cardId = "-3";
        }


    }

    @Override
    protected Call<PaymentModel> generateRDClient() {
        if (this.settlePayParams != null) {
            JSONObject object = new JSONObject();
            object.put("billId", settlePayParams.getBillId());
            object.put("repayAmount", settlePayParams.getRepayAmount());
            object.put("rabteAmount", "");//选择支付宝不可使用余额
            object.put("payPwd", "");//不用填
//            object.put("couponId", settlePayParams.getCouponId());
            object.put("couponId", "");
            object.put("cardId", -3);
            return RDClient.getService(PaymentApi.class).submitClear(object);

        } else {
            JSONObject object = paymentParams.getParams();
            return RDClient.getService(PaymentApi.class).submitRepaymentByYiBao(object);
        }
    }
}
