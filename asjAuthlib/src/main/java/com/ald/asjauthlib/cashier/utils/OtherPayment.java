package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.utils.Utils;


/**
 * 其他支付方式微信+银行卡
 * Created by sean yu on 2017/7/31.
 */
public abstract class OtherPayment implements IPayment {

    private IPayment paymentClient;
    private IPaymentCallBack callBack;
    private PaymentParams params;
    private Context context;

    private OtherSelectView paymentView;

    private IViewResultCallBack<BankCardModel> viewCallBack = new IViewResultCallBack<BankCardModel>() {
        @Override
        public void onViewResult(BankCardModel resultData) {
            if (resultData.getRid() > 0) {    // 银行卡支付
                paymentClient = generateBankPayment(context);
                params.paType = String.valueOf(resultData.getRid());
                params.bankChannel=resultData.getBankChannel()==null?"":resultData.getBankChannel();
                params.mobile=resultData.getMobile()==null?"":resultData.getMobile();
            } else if (resultData.getRid() == -1) {// 微信支付 rid = -1
                paymentClient = generateWxPayment(context);
            } else if (resultData.getRid() == -3) {//支付宝
                paymentClient = generateAliPayment(context);
            } else if (resultData.getRid() == -4) {//线下还款
                paymentClient = generateOutlinePayment(context);
            }
            paymentClient.initPayment(params);// 调起后续视图
            paymentClient.ObserverPayInfo(callBack);

            //现有业务逻辑微信和支付宝直接提交支付请求
            if (paymentClient instanceof WxPayment
                    || paymentClient instanceof AliPayment) {
                paymentClient.submit();
            } else if (paymentClient instanceof OutLinePayment) {
                //线下还款直接跳转H5
                outlineRefund();
            }
        }
    };

    private void outlineRefund() {
        Utils.jumpH5byPinH5(Constant.H5_OTHER_REPAYMENT);
    }


    public OtherPayment(Context context) {
        this.context = context;
    }

    @Override
    public void initPayment(PaymentParams params) {
        this.params = params;
        paymentView = new OtherSelectView(context);
        paymentView.createView(params);
        paymentView.ObserverView(viewCallBack);
    }

    @Override
    public void ObserverPayInfo(IPaymentCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 银行卡支付
     */
    protected abstract BankPayment generateBankPayment(Context context);

    /**
     * 微信支付
     */
    protected abstract WxPayment generateWxPayment(Context context);

    /**
     * 支付宝支付
     */
    protected abstract AliPayment generateAliPayment(Context context);

    /**
     * 线下还款
     */
    protected abstract OutLinePayment generateOutlinePayment(Context context);

    @Override
    public void submit() {
        if (paymentClient != null) {
            paymentClient.submit();
        }
    }

    @Override
    public void UnObserverPayInfo(IPaymentCallBack callBack) {
        if (paymentClient != null) {
            paymentClient.UnObserverPayInfo(callBack);
        }
        paymentView.UnObserverView(viewCallBack);
        paymentView.destroyView();
    }

    @Override
    public IPaymentView getPaymentView() {
        return paymentView;
    }
}
