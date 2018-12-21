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
public class BankCardCheckModel extends BaseModel {
    private String allowPayPwd;
    private String realNameStatus;
    private String zmxyAuthUrl;
    private long realNameScore;

    public BankCardCheckModel() {
    }

    public String getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(String realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    public String getZmxyAuthUrl() {
        return zmxyAuthUrl;
    }

    public void setZmxyAuthUrl(String zmxyAuthUrl) {
        this.zmxyAuthUrl = zmxyAuthUrl;
    }

    public long getRealNameScore() {
        return realNameScore;
    }

    public void setRealNameScore(long realNameScore) {
        this.realNameScore = realNameScore;
    }

    public String getAllowPayPwd() {
        return allowPayPwd;
    }

    public void setAllowPayPwd(String allowPayPwd) {
        this.allowPayPwd = allowPayPwd;
    }
}
