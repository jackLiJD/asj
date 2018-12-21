package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.cashier.model.ComPayResultModel;
import com.ald.asjauthlib.cashier.order.OrderOtherPayment;
import com.ald.asjauthlib.cashier.order.OrderPwdPayment;


/**
 * 支付工厂方法
 * Created by sean yu on 2017/8/4.
 */
public class PaymentFactory {
    //订单还款相关参数
    public static final String ALA_ORDER_PAYMENT_OTHER = "ala_order_payment_other";
    public static final String ALA_ORDER_PAYMENT_PWD = "ala_order_payment_pwd";
    public static final String ALA_ORDER_PAYMENT_ADD_CARD = "ala_payment_add_card";

    //现金借款还款相关参数
    public static final String ALA_LOAN_PAYMENT_OTHER = "ala_loan_payment_other";
    public static final String ALA_LOAN_PAYMENT_PWD = "ala_loan_payment_pwd";

    //消费分期还款相关参数
    public static final String ALA_STAGE_PAYMENT_OTHER = "ala_stage_payment_other";
    public static final String ALA_STAGE_PAYMENT_PWD = "ala_stage_payment_pwd";

    //续期还款
    public static final String ALA_RENEWAL_PAYMENT_OTHER = "ala_renewal_payment_other";

    private IPayment AlaPayment;
    private Context context;

    private IPaymentCallBack callBack;
    private IViewResultCallBack<ComPayResultModel> viewCallBack;

    public PaymentFactory(Context context) {
        this.context = context;
    }

    /**
     * 生成支付类
     *
     * @param paymentType 支付类型
     * @return IPayment
     */
    public PaymentFactory generatePayment(String paymentType) {
        if (ALA_ORDER_PAYMENT_OTHER.equals(paymentType)) {
            AlaPayment = new OrderOtherPayment(context);
        } else if (ALA_ORDER_PAYMENT_PWD.equals(paymentType)) {
            AlaPayment = new OrderPwdPayment(context);

            // 借钱还款
        } else if (ALA_LOAN_PAYMENT_OTHER.equals(paymentType)) {
            AlaPayment = new LoanOtherPayment(context);
        } else if (ALA_LOAN_PAYMENT_PWD.equals(paymentType)) {
            AlaPayment = new LoanPwdPayment(context);

            // 消费分期还款
        } else if (ALA_STAGE_PAYMENT_OTHER.equals(paymentType)) {
            AlaPayment = new StageOtherPayment(context);
        } else if (ALA_STAGE_PAYMENT_PWD.equals(paymentType)) {
            AlaPayment = new StagePwdPayment(context);

            //续期还款
        } else if (ALA_RENEWAL_PAYMENT_OTHER.equals(paymentType)) {
            AlaPayment = new RenewalOtherPayment(context);
        }
        if (AlaPayment == null) {
            throw new NullPointerException("请选择正确参数");
        }
        AlaPayment.ObserverPayInfo(callBack);
        return this;
    }

    /**
     * 传入支付参数
     *
     * @param paymentParams 支付参数
     */
    public void initParams(PaymentParams paymentParams) {
        AlaPayment.initPayment(paymentParams);
    }



    /**
     * 获取视图回调
     */
    public void setComplexCallBack(IViewResultCallBack<ComPayResultModel> viewCallBack) {
        this.viewCallBack = viewCallBack;
    }


    /**
     * 注册支付回调
     *
     * @param callBack 回调接口
     */
    public void ObserverPayInfo(IPaymentCallBack callBack) {
        this.callBack = callBack;
        if (AlaPayment != null) {
            AlaPayment.ObserverPayInfo(callBack);
        }
    }


    /**
     * 销毁支付毁掉
     */
    public void UnObserverPayInfo() {
        if (AlaPayment != null) {
            AlaPayment.UnObserverPayInfo(null);
        }
    }

}
