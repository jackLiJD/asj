package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.util.List;

/**
 * 依图上传身份证返回Model
 * Created by yaowenda on 17/5/16.
 */

public class YiTuUploadCardResultModel extends BaseModel {
    //身份证相关信息
    private YiTuResultModel cardInfo;
    //身份证正反面URL地址
    private List<UrlModel> list;

    public YiTuResultModel getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(YiTuResultModel cardInfo) {
        this.cardInfo = cardInfo;
    }

    public List<UrlModel> getList() {
        return list;
    }

    public void setList(List<UrlModel> list) {
        this.list = list;
    }
}
