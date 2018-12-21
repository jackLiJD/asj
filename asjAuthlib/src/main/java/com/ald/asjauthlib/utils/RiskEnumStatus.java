package com.ald.asjauthlib.utils;

/**
 * Created by sean yu on 2017/6/12.
 */

public enum RiskEnumStatus {

    A("A", "未审核"),
    N("N", "未通过审核"),
    P("P", "审核中"),
    Y("Y", "已通过审核");

    String status;
    String value;

    RiskEnumStatus(String status, String value) {
        this.status = status;
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
