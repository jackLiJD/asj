package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/22 15:22
 * 描述：
 * 修订历史：
 */
public class ContactorUrgentModel extends BaseModel {
    private String allowConsume;
    public ContactorUrgentModel() {
    }

    public String getAllowConsume() {
        return allowConsume;
    }

    public void setAllowConsume(String allowConsume) {
        this.allowConsume = allowConsume;
    }
}
