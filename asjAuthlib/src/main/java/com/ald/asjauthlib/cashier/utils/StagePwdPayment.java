package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.auth.BusinessApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.SettlePayParams;
import com.ald.asjauthlib.cashier.params.StagePayParams;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;

import retrofit2.Call;

/**
 * 分期密码支付
 * Created by sean yu on 2017/8/11.
 */

public class StagePwdPayment extends PwdPayment {

    private StagePayParams paymentParams;

    private SettlePayParams settlePayParams;

    public StagePwdPayment(Context context) {
        super(context);
    }

    @Override
    protected Call<WxOrAlaPayModel> generateRDClient() {
        if (settlePayParams != null) {
            JSONObject object = new JSONObject();
            object.put("billId", settlePayParams.getBillId());
            object.put("repayAmount", settlePayParams.getRepayAmount());
            object.put("rabteAmount", settlePayParams.getRabteAmount());
            object.put("payPwd", settlePayParams.getPayPwd());
            object.put("couponId", settlePayParams.getCouponId());
            object.put("cardId", settlePayParams.cardId);
            return RDClient.getService(BusinessApi.class).submitClear(object);
        } else {
            JSONObject object = paymentParams.getParams();
            return RDClient.getService(BusinessApi.class).submitRepaymentByYiBao(object);
        }
    }

    @Override
    protected PwdInputView createPaymentView(PaymentParams params) {
        if (params instanceof SettlePayParams) {
            this.settlePayParams = (SettlePayParams) params;
            this.settlePayParams.cardId = "-2";

        } else {
            this.paymentParams = (StagePayParams) params;
            this.paymentParams.cardId = "-2";
        }


        PwdInputView inputView = new PwdInputView(getContext());
        inputView.createView("银行卡支付");
        return inputView;
    }

    @Override
    protected IViewResultCallBack<String> generateViewCallBack() {
        return new IViewResultCallBack<String>() {
            @Override
            public void onViewResult(String resultData) {
                if (settlePayParams != null) {
                    settlePayParams.payPwd = MD5Util.getMD5Str(resultData);
                } else {
                    paymentParams.payPwd = MD5Util.getMD5Str(resultData);
                }
                submit();
            }
        };
    }
}
