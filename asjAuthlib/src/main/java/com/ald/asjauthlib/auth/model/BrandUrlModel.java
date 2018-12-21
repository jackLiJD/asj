package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 品牌URL model
 * Created by yaowenda on 17/3/24.
 */

public class BrandUrlModel extends BaseModel {
    private String shopUrl;//品牌URL

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }
}
