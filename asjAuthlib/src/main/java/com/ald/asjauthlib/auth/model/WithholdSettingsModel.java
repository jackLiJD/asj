package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 代扣设置返回实体类
 * Created by ywd on 2017/11/13.
 */

public class WithholdSettingsModel extends BaseModel {
    private String isWithhold;//代扣设置是否开启 1:开启 0:关闭
    private String usebalance;//是否使用账户余额 1:开启 0:关闭
    private String isMain;//是否是主卡
    private String isDeal;//是否在代扣任务处理中
    private String message;//提示信息

    public String getIsWithhold() {
        return isWithhold;
    }

    public void setIsWithhold(String isWithhold) {
        this.isWithhold = isWithhold;
    }

    public String getUsebalance() {
        return usebalance;
    }

    public void setUsebalance(String usebalance) {
        this.usebalance = usebalance;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getIsDeal() {
        return isDeal;
    }

    public void setIsDeal(String isDeal) {
        this.isDeal = isDeal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
