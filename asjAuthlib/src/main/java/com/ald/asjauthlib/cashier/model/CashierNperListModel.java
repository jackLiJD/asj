package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;
import java.util.List;

/*
 * Created by liangchen on 2018/9/4.
 * 购物流程优化
 */

public class CashierNperListModel extends BaseModel {

    List<NperModel> nperList;

    public List<NperModel> getNperList() {
        return nperList;
    }

    public void setNperList(List<NperModel> nperList) {
        this.nperList = nperList;
    }

    public CashierNperListModel() {

    }


    public static class NperModel {
        private BigDecimal totalAmount;//用户支付总金额
        private BigDecimal poundageAmount;//用户支付总的手续费和利息
        private String isFree;//分期账单类型： 1：完全免息账单，2：部分免息账单，0：非免息账单
        private String freeNper;//免息期数
        private List<NperDetailModel> nperDetailList;
        private int nper;//期数

        public int getNper() {
            return nper;
        }

        public void setNper(int nper) {
            this.nper = nper;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public BigDecimal getPoundageAmount() {
            return poundageAmount;
        }

        public void setPoundageAmount(BigDecimal poundageAmount) {
            this.poundageAmount = poundageAmount;
        }

        public String getIsFree() {
            return isFree;
        }

        public void setIsFree(String isFree) {
            this.isFree = isFree;
        }

        public String getFreeNper() {
            return freeNper;
        }

        public void setFreeNper(String freeNper) {
            this.freeNper = freeNper;
        }

        public List<NperDetailModel> getNperDetailList() {
            return nperDetailList;
        }

        public void setNperDetailList(List<NperDetailModel> nperDetailList) {
            this.nperDetailList = nperDetailList;
        }
    }

    public static class NperDetailModel {
        private BigDecimal amount;//月供本金
        private BigDecimal poundageAmount2;//月供利息和手续费
        private int nperNum;//第几期
        private BigDecimal monthAmount;//月供总金额

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getPoundageAmount2() {
            return poundageAmount2;
        }

        public void setPoundageAmount2(BigDecimal poundageAmount2) {
            this.poundageAmount2 = poundageAmount2;
        }

        public int getNperNum() {
            return nperNum;
        }

        public void setNperNum(int nperNum) {
            this.nperNum = nperNum;
        }

        public BigDecimal getMonthAmount() {
            return monthAmount;
        }

        public void setMonthAmount(BigDecimal monthAmount) {
            this.monthAmount = monthAmount;
        }
    }
}
