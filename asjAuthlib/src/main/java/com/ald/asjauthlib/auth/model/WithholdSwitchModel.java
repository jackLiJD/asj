package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 开启/关闭代扣返回实体类
 * Created by ywd on 2017/11/13.
 */

public class WithholdSwitchModel extends BaseModel {
    private String isWithhold;//是否启用代扣

    public String getIsWithhold() {
        return isWithhold;
    }

    public void setIsWithhold(String isWithhold) {
        this.isWithhold = isWithhold;
    }
}
