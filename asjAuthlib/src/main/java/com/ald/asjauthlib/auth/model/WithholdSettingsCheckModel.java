package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 检查是否可设置代扣
 * Created by ywd on 2017/11/17.
 */

public class WithholdSettingsCheckModel extends BaseModel {
    private String isMain;//是否是主卡
    private String isDeal;//是否在代扣任务处理中
    private String isUserSeed;//是否是白名单用户
    private String message;//提示信息

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

    public String getIsUserSeed() {
        return isUserSeed;
    }

    public void setIsUserSeed(String isUserSeed) {
        this.isUserSeed = isUserSeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
