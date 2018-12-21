package com.ald.asjauthlib.cashier.order;

import android.content.Context;

import com.ald.asjauthlib.cashier.BrandApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.OrderPayParams;
import com.ald.asjauthlib.cashier.utils.IPayment;
import com.ald.asjauthlib.cashier.utils.IPaymentCallBack;
import com.ald.asjauthlib.cashier.utils.IPaymentView;
import com.ald.asjauthlib.cashier.utils.IViewResultCallBack;
import com.ald.asjauthlib.cashier.utils.PaymentParams;
import com.ald.asjauthlib.cashier.utils.PwdInputView;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 组合支付页面
 * Created by sean yu on 2017/7/31.
 */
public class CombinationPayment implements IPayment {
    private IPaymentCallBack callBack;
    private OrderPayParams orderParams;

    private PwdInputView pwdView;
    private Context context;

    private IViewResultCallBack<String> viewCallBack = new IViewResultCallBack<String>() {
        @Override
        public void onViewResult(String resultData) {
            orderParams.payPwd = MD5Util.getMD5Str(resultData);
            submit();
        }
    };

    public CombinationPayment(Context context) {
        this.context = context;
    }

    @Override
    public void initPayment(PaymentParams params) {
        this.orderParams = (OrderPayParams) params;

        orderParams.type = "AGENTBUY";
        orderParams.isCombinationPay = "Y";

        pwdView = new PwdInputView(context);
        pwdView.createView("组合支付");
        pwdView.ObserverView(viewCallBack);
    }

    @Override
    public void ObserverPayInfo(IPaymentCallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public void submit() {
        Call<WxOrAlaPayModel> call = RDClient.getService(BrandApi.class).payOrder(orderParams.getParams());
        call.enqueue(new RequestCallBack<WxOrAlaPayModel>() {
            @Override
            public void onSuccess(Call<WxOrAlaPayModel> call, Response<WxOrAlaPayModel> response) {
                if (callBack != null) {
                    callBack.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<WxOrAlaPayModel> call, Throwable t) {
                super.onFailure(call, t);
                if (callBack != null) {
                    callBack.onCancel(t);
                }
            }
        });
    }

    @Override
    public void UnObserverPayInfo(IPaymentCallBack callBack) {
        if (this.callBack != null) {
            this.callBack = null;
        }
        pwdView.UnObserverView(viewCallBack);
        pwdView.destroyView();
    }

    @Override
    public IPaymentView getPaymentView() {
        return pwdView;
    }
}
