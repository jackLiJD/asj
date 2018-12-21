package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/22 15:22
 * 描述：
 * 修订历史：
 */
public class BillRefundModel extends BaseModel {
    private BigDecimal rebateAmount;	// 返现金额
    private BigDecimal repayAmount;	// 还款金额
    private String billId;	// 账单id
    private long couponId;	// 推荐优惠券id
    private String couponName;	// 推荐优惠券名称
    private BigDecimal couponAmount;	// 推荐优惠券金额
    private int jfbAmount;	// 账号集分宝个数
    private List<MyTicketModel> couponList;	// 优惠券列表

    public BillRefundModel() {
    }

    public List<MyTicketModel> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<MyTicketModel> couponList) {
        this.couponList = couponList;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount != null ? couponAmount : new BigDecimal(0f);
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getJfbAmount() {
        return jfbAmount;
    }

    public void setJfbAmount(int jfbAmount) {
        this.jfbAmount = jfbAmount;
    }
}
