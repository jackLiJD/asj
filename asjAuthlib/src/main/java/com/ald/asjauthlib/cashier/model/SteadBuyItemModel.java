package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

import java.math.BigDecimal;

/**
 * 分期模型
 * Created by sean yu on 2017/5/29.
 */
public class SteadBuyItemModel extends BaseModel {
    private BigDecimal totalAmount;
    private BigDecimal amount;
    //手续费
    private BigDecimal poundageAmount;
    private String nper;
    private String freeNper;
    //1完全免息、2部分免息、0非免息
    private String isFree;
    private BigDecimal freeAmount;

    public BigDecimal getTotalAmount() {
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPoundageAmount() {
        return poundageAmount != null ? poundageAmount : BigDecimal.ZERO;
    }

    public void setPoundageAmount(BigDecimal poundageAmount) {
        this.poundageAmount = poundageAmount;
    }

    public String getNper() {
        return MiscUtils.isNotEmpty(nper) ? nper : "0";
    }

    public void setNper(String nper) {
        this.nper = nper;
    }

    public String getFreeNper() {
        return freeNper;
    }

    public void setFreeNper(String freeNper) {
        this.freeNper = freeNper;
    }

    public String getIsFree() {
        return isFree;
    }

    public void setIsFree(String isFree) {
        this.isFree = isFree;
    }

    public BigDecimal getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(BigDecimal freeAmount) {
        this.freeAmount = freeAmount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SteadBuyItemModel) {
            return ((SteadBuyItemModel) obj).getNper().equals(getNper());
        }
        return super.equals(obj);
    }
}
