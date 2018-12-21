package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;

/**
 * 组合支付返回参数
 * Created by sean yu on 2017/8/7.
 */

public class ComPayResultModel extends BaseModel {
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 被选中的支付方式
     */
    private String payType;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 银行卡ID
     */
    private long rid;

    /**
     * 支付密码
     */
    private String pwd;

    /**
     * 分期
     */
    private String nper;

    /**
     * 支付的金额
     */
    private BigDecimal payAmount;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getNper() {
        return nper;
    }

    public void setNper(String nper) {
        this.nper = nper;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
