package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * Created by Yangyang on 2018/12/13.
 * desc:
 */

public class ThirdLoginModel extends BaseModel {
    /**
     * mobilePhone : 15268238961
     * token : f457e5b6a9af498b15c2bf4d82661e8a16bb3e6867a8ef19126beec071a2fb56
     */

    private String mobilePhone;
    private String token;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
