package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * Created by sean yu on 2017/7/13.
 */
public class ExtraUserIdModel extends BaseModel {

    public String transPara;

    public String getTransPara() {
        return transPara;
    }

    public void setTransPara(String transPara) {
        this.transPara = transPara;
    }
}
