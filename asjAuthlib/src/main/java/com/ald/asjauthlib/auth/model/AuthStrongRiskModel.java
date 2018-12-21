package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 提交强风控返回实体类
 * Created by yaowenda on 17/7/6.
 */

public class AuthStrongRiskModel extends BaseModel {
    private String creditRebateMsg;

    public String getCreditRebateMsg() {
        return creditRebateMsg;
    }

    public void setCreditRebateMsg(String creditRebateMsg) {
        this.creditRebateMsg = creditRebateMsg;
    }
}
