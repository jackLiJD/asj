package com.ald.asjauthlib.cashier.impl;


import com.ald.asjauthlib.cashier.utils.IAccountIRepayment;

import java.math.BigDecimal;

/*
 * Created by sean yu on 2017/4/14.
 */

public class AccountInfoImpl implements IAccountIRepayment {
    //账户余额
    private BigDecimal accountAmount;
    //需要支付的金额
    private BigDecimal totalPayAmount;
    //是否可用
    private boolean isUse;

    public AccountInfoImpl(BigDecimal accountAmount) {
        this.accountAmount = accountAmount;
    }

    @Override
    public BigDecimal getValueAmount() {
        if (isUse) {
            return accountAmount != null ? accountAmount : BigDecimal.ZERO;
        } else {
            return BigDecimal.ZERO;
        }

    }

    @Override
    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

    @Override
    public void setTotalPayAmount(BigDecimal bigDecimal) {
        this.totalPayAmount = bigDecimal;
    }

    public BigDecimal getAccountAmount() {
        return accountAmount != null ? accountAmount : BigDecimal.ZERO;
    }

    private BigDecimal getTotalPayAmount() {
        return totalPayAmount != null ? totalPayAmount : BigDecimal.ZERO;
    }

    /**
     * 实际支付的金额
     *
     */
    public String getActualPayAmount() {
        if (!isUse) {
            return BigDecimal.ZERO.toString();
        }
        if (getAccountAmount().compareTo(getTotalPayAmount()) > 0) {
            return getTotalPayAmount().toString();
        } else {
            return getAccountAmount().toString();
        }
    }
}
