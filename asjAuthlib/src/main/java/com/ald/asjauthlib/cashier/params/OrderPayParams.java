package com.ald.asjauthlib.cashier.params;


import com.ald.asjauthlib.cashier.utils.PaymentParams;

/**
 * 订单提交参数
 * Created by sean yu on 2017/8/11.
 */

public class OrderPayParams extends PaymentParams {


    /**
     * 额外参数
     */
    public String goodName;

    public String payId;

    public String numId;

    public String amount;

    public String nperInfo;

    /**
     * 订单ID
     */
    public String orderId;

    /**
     * 支付分类
     */
    public String type;

    /**
     * 支付分类
     */
    public String payPwd;

    /**
     * 分期数
     */
    public String nper;

    /**
     * 是否是组合支付
     */
    public String isCombinationPay;

    /**
     * 支付时纬度
     */
    public String lat;

    /**
     * 支付时经度
     */
    public String lng;

    /**
     * 检查
     */
    public void checkParams() {
        super.checkParams("orderId", "payId");
    }

    /**
     * 验证组合支付参数是否合法
     */
    public boolean checkCombinationParams() {
        return super.checkParams("orderId", "goodName");
    }

    /**
     * 验证分期参数
     *
     * @return
     */
    public boolean checkNperParams() {
        return super.checkParams("numId", "amount", "goodName");
    }
}
