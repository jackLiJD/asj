package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.util.List;

/**
 * Created by yushumin on 2017/3/29.
 */

public class CouponListModel extends BaseModel {
    private List<MyTicketModel> couponList;

    public List<MyTicketModel> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<MyTicketModel> couponList) {
        this.couponList = couponList;
    }
}
