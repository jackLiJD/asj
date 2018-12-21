package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 新老借钱页面跳转判断实体类
 * Created by ywd on 2017/12/12.
 */

public class CashPageTypeModel extends BaseModel {
    private String pageType;//old:老版本 new:新版本

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }
}
