package com.ald.asjauthlib.cashier.utils;

import java.math.BigDecimal;

/**
 * 优惠信息接口模块
 * Created by sean yu on 2017/4/14.
 */
public interface IRepaymentModule {

    /**
     * 获取金额
     *
     * @return 金额
     */
    public BigDecimal getValueAmount();

    /**
	 * 是否使用了优惠券
     */
    public boolean isUse();

}
