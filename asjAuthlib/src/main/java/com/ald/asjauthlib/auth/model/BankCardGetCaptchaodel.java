package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/20 10:50
 * 描述：
 * 修订历史：
 */
public class BankCardGetCaptchaodel extends BaseModel {
    private String realName;

    public BankCardGetCaptchaodel() {
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
