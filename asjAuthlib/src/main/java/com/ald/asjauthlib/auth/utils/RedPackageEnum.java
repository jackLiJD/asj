package com.ald.asjauthlib.auth.utils;

/**
 * Created by sean yu on 2017/7/6.
 */
public enum RedPackageEnum {

    MOBILE("MOBILE", "话费充值"),
    REPAYMENT("REPAYMENT", "还款"),
    FULLVOUCHER("FULLVOUCHER", "满减卷"),
    CASH("MOBILE", "现金奖励"),
    ACTIVITY("ACTIVITY", "会场券"),
    FREEINTEREST("FREEINTEREST", "钱免息券"),
    STROLL("STROLL", "逛逛优惠券");

    private String value;
    private String des;

    RedPackageEnum(String value, String des) {
        this.value = value;
        this.des = des;
    }

    public String getValue() {
        return value;
    }

    public String getDes() {
        return des;
    }
}
