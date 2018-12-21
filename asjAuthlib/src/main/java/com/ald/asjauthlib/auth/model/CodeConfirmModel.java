package com.ald.asjauthlib.auth.model;

import java.io.Serializable;

/**
 * Created by wjy on 2017/11/10.
 */

public class CodeConfirmModel implements Serializable{

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
