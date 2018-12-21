package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * Created by luckyliang on 2017/11/7.
 */

public class BankCardUploadModel extends BaseModel {

    public String verifyCode;
    public String bankId;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }


    public String getWifi_mac() {
        return wifi_mac;
    }

    public void setWifi_mac(String wifi_mac) {
        this.wifi_mac = wifi_mac;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double lat;
    public double lng;
    public String wifi_mac;
    public String uuid;
    public String password;


}
