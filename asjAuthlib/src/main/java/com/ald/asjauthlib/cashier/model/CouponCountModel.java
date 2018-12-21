package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 优惠券数量实体类
 * Created by ywd on 2017/10/24.
 */

public class CouponCountModel extends BaseModel {
    private int brandCouponCount;//OtoSaas平台优惠券数量
    private int plantformCouponCount;//本地平台优惠券数量
    private int couponCount;//我的优惠券总数

    public int getBrandCouponCount() {
        return brandCouponCount;
    }

    public void setBrandCouponCount(int brandCouponCount) {
        this.brandCouponCount = brandCouponCount;
    }

    public int getPlantformCouponCount() {
        return plantformCouponCount;
    }

    public void setPlantformCouponCount(int plantformCouponCount) {
        this.plantformCouponCount = plantformCouponCount;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }
}
