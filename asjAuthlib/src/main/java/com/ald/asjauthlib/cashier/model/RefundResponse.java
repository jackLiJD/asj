package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.util.List;

/**
 * Created by zhuangdaoyuan on 2018/3/23.
 * 安静撸码，淡定做人
 */

public class RefundResponse extends BaseModel {
    private List<RefundType> payTypeStatusList;

    public List<RefundType> getPayTypeStatusList() {
        return payTypeStatusList;
    }

    public void setPayTypeStatusList(List<RefundType> payTypeStatusList) {
        this.payTypeStatusList = payTypeStatusList;
    }

    public class RefundType extends BaseModel {
        private String payType;//还款方式WX,ZFB,XXHK
        private String status;//是否启用

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
