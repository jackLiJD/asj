package com.ald.asjauthlib.cashier.params;


import com.ald.asjauthlib.cashier.utils.PaymentParams;

/**
 * 金融支付相关参数
 * Created by sean yu on 2017/8/11.
 */
public class FinancePayParams extends PaymentParams {

    //银行卡id
    public String cardId;
    //支付密码
    public String payPwd;
    //优惠券
    public String couponId;
    //还款金额
    public String repaymentAmount;
    //余额还款
    public String rebateAmount;
    //积分宝还款
    public String jfbAmount;
    //实际支付金额
    public String actualAmount;
    //新老借钱判断
    public String pageType;
    //从哪个页面进入
    public String from;
    //借款附带的订单id 订单记录进入，必须携带
    public String borrowOrderId;
    public String repaymentType;//还款类型(小贷、白领贷)
    public String repaymentTypeWhiteCollar;//白领贷还款类型(提前还款、正常还款)
    public String loanId;//借款信息ID
    public String loanPeriodsIds;//借款期数ID
    public boolean secretFree = false;//是否免密支付
    public String errMsg;//支付失败提示(不包括支付密码错误)
}
