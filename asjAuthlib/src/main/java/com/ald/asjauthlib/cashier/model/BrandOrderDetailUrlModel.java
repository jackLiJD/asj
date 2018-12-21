package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 菠萝觅订单详情接口返回Model
 * Created by yaowenda on 17/3/30.
 */

public class BrandOrderDetailUrlModel extends BaseModel {
    private String detailUrl;//跳转详情地址url

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
