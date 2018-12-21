package com.ald.asjauthlib.auth.model;

/**
 * 认证模型基类
 * Created by sean yu on 2017/6/12.
 */
public interface AuthModel {

    String getRealNameStatus();

    String getFaceStatus();

    String getIsBind();

    String getRiskStatus();

    String getRealName();

    String getIdNumber();

    String getIsSupplyCertify();

    String getMobileStatus();

    String getErrorUrl();

    String getBasicStatus();
}
