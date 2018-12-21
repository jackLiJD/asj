package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;

/**
 * 收银台下单返回实体类
 * Created by ywd on 2017/10/30.
 */

public class CashierSubmitResponseModel extends BaseModel {
    private String orderId;
    private String nper;
    private String isEnoughAmount;
    private String isNoneQuota;//可用额度是否为0
    private long goldCoinCount;
    private BigDecimal goldCoinAmount;
    private String isWorm;
    private String hasCrossBorder;

    public BigDecimal getGoldCoinAmount() {
        return goldCoinAmount;
    }

    public void setGoldCoinAmount(BigDecimal goldCoinAmount) {
        this.goldCoinAmount = goldCoinAmount;
    }

    public long getGoldCoinCount() {
        return goldCoinCount;
    }

    public void setGoldCoinCount(long goldCoinCount) {
        this.goldCoinCount = goldCoinCount;
    }

    public String getIsWorm() {
        return isWorm == null ? "" : isWorm;
    }

    public void setIsWorm(String isWorm) {
        this.isWorm = isWorm;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNper() {
        return nper;
    }

    public void setNper(String nper) {
        this.nper = nper;
    }

    public String getIsEnoughAmount() {
        return isEnoughAmount;
    }

    public void setIsEnoughAmount(String isEnoughAmount) {
        this.isEnoughAmount = isEnoughAmount;
    }

    public String getIsNoneQuota() {
        return isNoneQuota;
    }

    public void setIsNoneQuota(String isNoneQuota) {
        this.isNoneQuota = isNoneQuota;
    }

    public String getHasCrossBorder() {
        return hasCrossBorder;
    }

    public void setHasCrossBorder(String hasCrossBorder) {
        this.hasCrossBorder = hasCrossBorder;
    }
}
