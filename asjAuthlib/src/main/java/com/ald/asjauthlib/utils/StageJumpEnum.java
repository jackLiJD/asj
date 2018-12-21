package com.ald.asjauthlib.utils;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/20 19:59
 * 描述：
 * 修订历史：
 */
public enum StageJumpEnum {

    STAGE_AUTH("STAGE_AUTH", 0, "实名认证"),
    STAGE_BANK_CARD("STAGE_BANK_CARD", 1, "添加银行卡"),
    STAGE_SET_PAY_PWD("STAGE_SET_PAY_PWD", 2, "设置支付密码"),//设置入口
    STAGE_FORGET_PWD("STAGE_FORGET_PWD", 3, "忘记密码"),
    STAGE_SELECT_BANK("STAGE_SELECT_BANK", 4, "选择银行卡dialog"),
    STAGE_ORAL_ACTIVITY("STAGE_ORAL_ACTIVITY", 5, "回到原始的页面"),
    STAGE_TRADE_SCAN("STAGE_TRADE_SCAN", 6, "商圈扫描二维码时实名"),
    STAGE_APPEAL("STAGE_APPEAL", 7, "账号申诉"),
    STAGE_BASIC_AUTH_EXIT("STAGE_BASIC_AUTH_EXIT", 9, "添加银行卡未完成"),
    STAGE_LIMIT_AUTH("STAGE_LIMIT_AUTH", 10, "51信用首页/全部额度"),
    STAGE_TRAIN_AUTH("STAGE_TRAIN_AUTH", 11, "线下培训"),
    STAGE_START_AUTH("STAGE_START_AUTH", 12, "认证引导"),
    STAGE_ONLINE_BANK("STAGE_ONLINE_BANK", 13, "网银认证"),
    STAGE_CASHIER("STAGE_CASHIER", 14, "收银台"),
    STAGE_BANK_CARD_ADD_PWD_SET("STAGE_BANK_CARD_ADD_PWD_SET", 15, "绑卡支付设密码设置"),
    STAGE_CREDIT_CENTER("STAGE_CREDIT_CENTER", 16, "信用中心"),
    STAGE_TO_REPAY_H5("STAGE_TO_REPAY_H5", 17, "全部待还"),
    STAGE_ORDER_LIST("STAGE_ORDER_LIST", 18, "订单列表"),
    STAGE_H5_BANK_CARD("STAGE_H5_BANK_CARD", 19, "H5绑卡"),
    STAGE_CREDIT_REFUND_CARD("STAGE_CREDIT_BANK_CARD", 20, "信用卡还款绑卡"),
    STAGE_CREDIT_REFUND_CENTER_CARD("STAGE_CREDIT_BANK_CARD", 20, "信用卡还款支付页绑卡");


    private String model;
    private int value;
    private String desc;

    StageJumpEnum(String model, int value, String desc) {
        this.model = model;
        this.value = value;
        this.desc = desc;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
