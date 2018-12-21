package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * Created by ywd on 2017/11/13.
 */

public class WithholdUseBalanceModel extends BaseModel {
    private String usebalance;//是否启用余额

    public String getUsebalance() {
        return usebalance;
    }

    public void setUsebalance(String usebalance) {
        this.usebalance = usebalance;
    }
}
