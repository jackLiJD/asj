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
public class AuthTdModel extends BaseModel {

    private String realNameScore;
    private String realNameStatus;
    private String creditLevel;
    private long  creditAssessTime;

    public AuthTdModel() {
    }

    public long getCreditAssessTime() {
        return creditAssessTime;
    }

    public void setCreditAssessTime(long creditAssessTime) {
        this.creditAssessTime = creditAssessTime;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public String getRealNameScore() {
        return realNameScore;
    }

    public void setRealNameScore(String realNameScore) {
        this.realNameScore = realNameScore;
    }

    public String getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(String realNameStatus) {
        this.realNameStatus = realNameStatus;
    }
}
