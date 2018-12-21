package com.ald.asjauthlib.cashier.params;

/*
 * Created by luckyliang on 2017/12/17.
 */

public class SettlePayParams extends FinancePayParams {
    String billId;
    String repayAmount;
    String rabteAmount;
    String couponId;
    //支付密码
    public String payPwd;
    //银行卡id

    public boolean isUserRabte() {
        return userRabte;
    }

    public void setUserRabte(boolean userRabte) {
        this.userRabte = userRabte;
    }

    public String cardId;

    public boolean userRabte;//使用余额

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getRabteAmount() {
        return rabteAmount;
    }

    public void setRabteAmount(String rabteAmount) {
        this.rabteAmount = rabteAmount;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
