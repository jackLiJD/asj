package com.ald.asjauthlib.cashier;

/**
 * 支付中各类支付无法使用的错误码对照表
 * Created by ywd on 2017/10/30.
 */

public class CashierConstant {
    //订单类型
    public static final String ORDER_TYPE_AGENTBUY = "AGENTBUY";//代买
    public static final String ORDER_TYPE_SELFSUPPORT = "SELFSUPPORT";//自营
    public static final String ORDER_TYPE_BOLUOME = "BOLUOME";//菠萝觅
    public static final String ORDER_TYPE_TRADE = "TRADE";//商圈
    public static final String ORDER_TYPE_RENT = "LEASE";//租赁
    public static final String ORDER_TYPE_GROUP = "GROUP";//拼团

    //收银台相关状态
    public static final String NEEDAUTH = "NEEDAUTH";//("NEEDAUTH", "需要认证"),
    public static final String USE_ABLED_LESS = "USE_ABLED_LESS";//("USE_ABLED_LESS", "可用额度小于分期额度"),//可以使用组合
    public static final String NOT_NEED = "NOT_NEED";//("NOT_NEED", "不需要组合支付"),
    public static final String NEEDUP = "NEEDUP";//("NEEDUP", "可用额度小于资源配置限制金额 比如额度只有1块钱，那么不能走分期"),
    public static final String CONSUME_MIN_AMOUNT = "CONSUME_MIN_AMOUNT";//("CONSUME_MIN_AMOUNT", "订单金额小于分期/组合支付最小额度限制"),
    public static final String VIRTUAL_GOODS_LIMIT = "NEEDUP";//("NEEDUP", "虚拟商品限额"),
    public static final String CASHIER = "CASHIER";//("CASHIER", "收银台限制"),
    public static final String OVERDUE_BORROW = "OVERDUE_BORROW";//("OVERDUE_BORROW", "消费分期逾期"),
    public static final String RISK_CREDIT_PAYMENT = "RISK_CREDIT_PAYMENT";//("RISK_CREDIT_PAYMENT", "风控限制使用信用支付"),
    public static final String OVERDUE_BORROW_CASH = "OVERDUE_BORROW_CASH";//("OVERDUE_BORROW_CASH", "现金借逾期");
    public static final String NEEDUP_VIRTUAL = "NEEDUP_VIRTUAL";//虚拟可用额度小于资源配置限制金额 比如虚拟可用额度只有1块钱，那么不能走分期

    public static final String CASHIER_PAY_TYPE = "cashier_pay_type";
    public static final String CASHIER_PAY_TYPE_NORMAL = "NORMAL";
    public static final String CASHIER_PAY_TYPE_ORDER = "ORDER";
    public static final String CASHIER_PAY_GOODS_NAME = "GOODS_NAME";
}
