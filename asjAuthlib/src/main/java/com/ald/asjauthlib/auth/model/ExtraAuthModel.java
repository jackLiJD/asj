package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;

/**
 * 额外认证参数模型
 * Created by sean yu on 2017/7/11.
 */

public class ExtraAuthModel extends BaseModel {

    //公积金认证
    private String fundStatus;
    //社
    private String socialSecurityStatus;
    //信用卡认证
    private String creditStatus;
    //
    private BigDecimal currentAmount;
    //支付宝认证中
    private String alipayStatus;

    private BigDecimal highestAmount;

    private String gmtFundExist;

    private String gmtSocialSecurityExist;

    private String gmtCreditExist;

    private String gmtAlipayExist;

    public String getAlipayStatus() {
        return alipayStatus;
    }

    public void setAlipayStatus(String alipayStatus) {
        this.alipayStatus = alipayStatus;
    }

    public String getFundStatus() {
        return fundStatus;
    }

    public void setFundStatus(String fundStatus) {
        this.fundStatus = fundStatus;
    }

    public String getSocialSecurityStatus() {
        return socialSecurityStatus;
    }

    public void setSocialSecurityStatus(String socialSecurityStatus) {
        this.socialSecurityStatus = socialSecurityStatus;
    }

    public String getCreditStatus() {
        return creditStatus;
    }

    public void setCreditStatus(String creditStatus) {
        this.creditStatus = creditStatus;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount != null ? currentAmount : BigDecimal.ZERO;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getHighestAmount() {
        return highestAmount != null ? highestAmount : BigDecimal.ZERO;
    }

    public void setHighestAmount(BigDecimal highestAmount) {
        this.highestAmount = highestAmount;
    }

    public String getGmtFundExist() {
        return gmtFundExist;
    }

    public void setGmtFundExist(String gmtFundExist) {
        this.gmtFundExist = gmtFundExist;
    }

    public String getGmtSocialSecurityExist() {
        return gmtSocialSecurityExist;
    }

    public void setGmtSocialSecurityExist(String gmtSocialSecurityExist) {
        this.gmtSocialSecurityExist = gmtSocialSecurityExist;
    }

    public String getGmtCreditExist() {
        return gmtCreditExist;
    }

    public void setGmtCreditExist(String gmtCreditExist) {
        this.gmtCreditExist = gmtCreditExist;
    }

    public String getGmtAlipayExist() {
        return gmtAlipayExist;
    }

    public void setGmtAlipayExist(String gmtAlipayExist) {
        this.gmtAlipayExist = gmtAlipayExist;
    }
}
