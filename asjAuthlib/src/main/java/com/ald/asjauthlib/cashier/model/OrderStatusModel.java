package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * Created by sean yu on 2017/9/8.
 */

public class OrderStatusModel extends BaseModel {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
