package com.ald.asjauthlib.cashier.params;


import com.ald.asjauthlib.cashier.utils.PaymentParams;

/**
 * 续期支付参数
 * Created by sean yu on 2017/8/25.
 */

public class RenewalParams extends PaymentParams {

    public String borrowId;
    public String payPwd;
    public String cardId;
    public String amount;
    public String renewalAmount;//用户输入金额
    public String pageType;//新老借钱页面跳转类型,用于判断新老续借(v2 v1 old)
    public String goodsId;//商品ID
    public String deliveryUser;//收件人姓名
    public String deliveryPhone;//收件人手机号
    public String address;//收货地址
    public boolean secretFree = false;//是否免密支付
    public String errMsg;//支付失败提示(不包括支付密码错误)
}
