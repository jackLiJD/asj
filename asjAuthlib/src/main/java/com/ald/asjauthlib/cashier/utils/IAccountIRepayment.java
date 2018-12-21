package com.ald.asjauthlib.cashier.utils;

import java.math.BigDecimal;

/**
 * Created by yushumin on 2017/4/24.
 */

public interface IAccountIRepayment extends IRepaymentModule {

    /**
     *
     */
    public void setTotalPayAmount(BigDecimal bigDecimal);
}
