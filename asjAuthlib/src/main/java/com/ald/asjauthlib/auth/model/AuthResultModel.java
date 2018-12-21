package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * Created by yushumin on 2017/4/26.
 */

public class AuthResultModel extends BaseModel {

    private String canRetry;

    public String getCanRetry() {
        return canRetry;
    }

    public void setCanRetry(String canRetry) {
        this.canRetry = canRetry;
    }
}
