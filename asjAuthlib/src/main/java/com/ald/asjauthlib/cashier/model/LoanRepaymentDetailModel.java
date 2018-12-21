package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/3/30 11:11
 * 描述：
 * 修订历史：
 */

public class LoanRepaymentDetailModel extends BaseModel {
    private String status;
    private BigDecimal amount;
    private BigDecimal couponAmount;// 	优惠金额
    private BigDecimal userAmount;// 	账户余额使用金额
    private BigDecimal actualAmount;// 	实际支付金额
    private String cardNumber;// 	卡号|微信号
    private String cardName;// 银行名称|微信
    private String repayNo;// 	还款编号
    private long gmtCreate;// 	还款时间

    public LoanRepaymentDetailModel() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount != null ? couponAmount : new BigDecimal(0);
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getUserAmount() {
        return userAmount != null ? userAmount : new BigDecimal(0);
    }

    public void setUserAmount(BigDecimal userAmount) {
        this.userAmount = userAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount != null ? actualAmount : new BigDecimal(0);
    }

    public void setActualAmount(BigDecimal cashAmount) {
        this.actualAmount = cashAmount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getRepayNo() {
        return repayNo;
    }

    public void setRepayNo(String repayNo) {
        this.repayNo = repayNo;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
