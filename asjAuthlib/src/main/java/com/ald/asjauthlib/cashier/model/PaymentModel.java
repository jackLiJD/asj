package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 支付回調
 * Created by sean yu on 2017/9/7.
 */
public class PaymentModel extends BaseModel {
    private String directPayType;

    private String merchantNo;

    private String orderId;

    private String token;

    private String userNo;

    private String userType;

    private String timeStamp;

    private String sign;

    private String refId;

    private String type;

    private String urlscheme;

    private String needCode;

    public String getNeedCode() {
        return needCode;
    }

    public void setNeedCode(String needCode) {
        this.needCode = needCode;
    }

    public String getDirectPayType() {
        return directPayType;
    }

    public void setDirectPayType(String directPayType) {
        this.directPayType = directPayType;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlscheme() {
        return urlscheme;
    }

    public void setUrlscheme(String urlscheme) {
        this.urlscheme = urlscheme;
    }
}
