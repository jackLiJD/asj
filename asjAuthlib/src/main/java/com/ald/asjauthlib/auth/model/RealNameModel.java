package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/19 19:31
 * 描述：
 * 修订历史：
 */
public class RealNameModel extends BaseModel {
    private String zmxyAuthUrl;

    public RealNameModel() {
    }

    public String getZmxyAuthUrl() {
        return zmxyAuthUrl;
    }

    public void setZmxyAuthUrl(String zmxyAuthUrl) {
        this.zmxyAuthUrl = zmxyAuthUrl;
    }

}
