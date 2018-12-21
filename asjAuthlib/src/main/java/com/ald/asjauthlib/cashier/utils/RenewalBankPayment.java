package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.cashier.LoanApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.RenewalParams;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;

import retrofit2.Call;

/**
 * 续期银行卡支付
 * Created by sean yu on 2017/8/25.
 */

public class RenewalBankPayment extends BankPayment {
    private RenewalParams paymentParams;

    public RenewalBankPayment(Context context) {
        super(context);
    }

    @Override
    protected Call<WxOrAlaPayModel> generateRDClient() {
        if (ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel().equals(paymentParams.pageType)) {
            return RDClient.getService(LoanApi.class).confirmLegalRenewalPayV2(paymentParams.getParams());
        }
        if (ModelEnum.CASH_PAGE_TYPE_NEW_V1.getModel().equals(paymentParams.pageType)) {
            return RDClient.getService(LoanApi.class).confirmLegalRenewalPay(paymentParams.getParams());
        }
        return RDClient.getService(LoanApi.class).confirmRenewalPay(paymentParams.getParams());
    }

    @Override
    protected PwdInputView createPaymentView(PaymentParams params) {
        this.paymentParams = (RenewalParams) params;
        this.paymentParams.cardId = params.paType;    // 即rid

        PwdInputView inputView = new PwdInputView(getContext());
        inputView.setSecretFree(paymentParams.secretFree);
        inputView.setSecretPwd(paymentParams.payPwd);
        inputView.createView("银行卡支付");
        return inputView;
    }

    @Override
    protected IViewResultCallBack<String> generateViewCallBack() {
        return new IViewResultCallBack<String>() {
            @Override
            public void onViewResult(String resultData) {
                if(resultData.length()>6){
                    submit();
                }else {
                    paymentParams.payPwd = MD5Util.getMD5Str(resultData);
                    submit();
                }
            }
        };
    }
}
