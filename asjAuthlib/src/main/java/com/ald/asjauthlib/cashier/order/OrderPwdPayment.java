package com.ald.asjauthlib.cashier.order;

import android.content.Context;

import com.ald.asjauthlib.cashier.BrandApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.OrderPayParams;
import com.ald.asjauthlib.cashier.utils.IViewResultCallBack;
import com.ald.asjauthlib.cashier.utils.PaymentParams;
import com.ald.asjauthlib.cashier.utils.PwdInputView;
import com.ald.asjauthlib.cashier.utils.PwdPayment;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;

import retrofit2.Call;

/**
 * 订单密码支付
 * Created by sean yu on 2017/8/11.
 */

public class OrderPwdPayment extends PwdPayment {
    private OrderPayParams payParams;

    public OrderPwdPayment(Context context) {
        super(context);
    }

    @Override
    protected Call<WxOrAlaPayModel> generateRDClient() {
        JSONObject object = payParams.getParams();
        return RDClient.getService(BrandApi.class).payOrder(object);
    }

    @Override
    protected PwdInputView createPaymentView(PaymentParams params) {
        this.payParams = (OrderPayParams) params;

        payParams.payId = "0";
        payParams.isCombinationPay = "N";

        PwdInputView inputView = new PwdInputView(getContext());
        inputView.createView("密码支付");
        return inputView;
    }

    @Override
    protected IViewResultCallBack<String> generateViewCallBack() {
        return new IViewResultCallBack<String>() {
            @Override
            public void onViewResult(String resultData) {
                payParams.payPwd = MD5Util.getMD5Str(resultData);
                submit();
            }
        };
    }
}
