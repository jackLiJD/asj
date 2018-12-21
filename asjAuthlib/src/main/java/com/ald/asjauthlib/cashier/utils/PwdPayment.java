package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 直接输入支付密码支付
 * Created by sean yu on 2017/8/10.
 */

public abstract class PwdPayment implements IPayment {
    private IPaymentCallBack callBack;
    private IViewResultCallBack<String> viewCallBack;

    private PwdInputView pwdView;
    private Context context;
    private PaymentParams paymentParams;

    public PwdPayment(Context context) {
        this.context = context;
    }

    @Override
    public void initPayment(PaymentParams params) {
        this.paymentParams = params;
        pwdView = createPaymentView(params);
        viewCallBack = generateViewCallBack();
        pwdView.ObserverView(viewCallBack);
    }

    @Override
    public void ObserverPayInfo(IPaymentCallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public void submit() {
        Call<WxOrAlaPayModel> call = generateRDClient();
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<WxOrAlaPayModel>() {
            @Override
            public void onSuccess(Call<WxOrAlaPayModel> call, Response<WxOrAlaPayModel> response) {
                if (callBack != null) {
                    WxOrAlaPayModel wxOrAlaPayModel = response.body();
                    wxOrAlaPayModel.setBankChannel(paymentParams.bankChannel);
                    wxOrAlaPayModel.setMobile(paymentParams.mobile);
                    callBack.onSuccess(wxOrAlaPayModel);
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


    public Context getContext() {
        return context;
    }

    @Override
    public IPaymentView getPaymentView() {
        return pwdView;
    }

    /**
     * 生成支付请求
     *
     * @return Call<WxOrAlaPayModel>
     */
    protected abstract Call<WxOrAlaPayModel> generateRDClient();

    /**
     * 生成支付视图
     *
     * @param params 支付参数
     * @return 支付视图
     */
    protected abstract PwdInputView createPaymentView(PaymentParams params);

    /**
     * 支付回调
     *
     * @return 支付回调接口
     */
    protected abstract IViewResultCallBack<String> generateViewCallBack();
}
