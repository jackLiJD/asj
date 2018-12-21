package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/19 19:31
 * 描述：
 * 修订历史：
 */
public class LimitDetailModel extends BaseModel {
    private long refId;
    private String cardName;
    private BigDecimal actualAmount;
    private BigDecimal rebateAmount;
    private BigDecimal couponAmount;
    private BigDecimal amount;
    private long gmtCreate;
    private String cardNo;
    private String type;
    private String number;
    private int nper;
    private int nperRepayment;//已还期数
    private BigDecimal perAmount;
    private BigDecimal repayPrinAmount;
    private String name;
    private String borrowDetail;//借款明细
    private long borrowDay;
    private BigDecimal payAmount;
    private String status;	//	借还款状态 APPLY:借款申请 , AGREE:审核通过 ,REFUSE:审核拒绝 , TRANSED:打款成功, CLOSED:借款关闭 Y:还款成功

    public LimitDetailModel() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorrowDetail() {
        return borrowDetail;
    }

    public void setBorrowDetail(String borrowDetail) {
        this.borrowDetail = borrowDetail;
    }

    public long getBorrowDay() {
        return borrowDay;
    }

    public void setBorrowDay(long borrowDay) {
        this.borrowDay = borrowDay;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public int getNper() {
        return nper;
    }

    public void setNper(int nper) {
        this.nper = nper;
    }

    public int getNperRepayment() {
        return nperRepayment;
    }

    public void setNperRepayment(int nperRepayment) {
        this.nperRepayment = nperRepayment;
    }

    public BigDecimal getPerAmount() {
        return perAmount;
    }

    public void setPerAmount(BigDecimal perAmount) {
        this.perAmount = perAmount;
    }

    public BigDecimal getRepayPrinAmount() {
        return repayPrinAmount;
    }

    public void setRepayPrinAmount(BigDecimal repayPrinAmount) {
        this.repayPrinAmount = repayPrinAmount;
    }

    public long getRefId() {
        return refId;
    }

    public void setRefId(long refId) {
        this.refId = refId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount == null ? new BigDecimal(0F) : rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount == null ? new BigDecimal(0F) : couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
