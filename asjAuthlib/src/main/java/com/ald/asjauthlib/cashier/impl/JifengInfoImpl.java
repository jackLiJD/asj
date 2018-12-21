package com.ald.asjauthlib.cashier.impl;


import com.ald.asjauthlib.cashier.utils.IAccountIRepayment;

import java.math.BigDecimal;

/**
 * Created by sean yu on 2017/4/14.
 */

public class JifengInfoImpl implements IAccountIRepayment {

    private int jifengCount;

    private BigDecimal totalPayAmount;

    private boolean isUse;

    public JifengInfoImpl(int count) {
        this.jifengCount = count;
    }

    @Override
    public BigDecimal getValueAmount() {
        return count2Value();
    }

    @Override
    public boolean isUse() {
        return isUse;
    }

    public void setUse(boolean use) {
        isUse = use;
    }

    private BigDecimal count2Value() {
        return new BigDecimal(jifengCount).divide(new BigDecimal(100));
    }

    private int value2Count(BigDecimal value) {
        return value.multiply(new BigDecimal(100)).intValue();
    }

    public BigDecimal getTotalPayAmount() {
        return totalPayAmount != null ? totalPayAmount : BigDecimal.ZERO;
    }

    @Override
    public void setTotalPayAmount(BigDecimal bigDecimal) {
        this.totalPayAmount = bigDecimal;
    }

    public int getJifengCount() {
        return jifengCount;
    }

    /**
     * 账户实际支付
     */
    public int getActualPayCount() {
        if (!isUse) {
            return value2Count(BigDecimal.ZERO);
        }
        if (getValueAmount().compareTo(getTotalPayAmount()) <= 0) {
            return value2Count(getValueAmount());
        } else {
            return value2Count(getTotalPayAmount());
        }
    }

}
