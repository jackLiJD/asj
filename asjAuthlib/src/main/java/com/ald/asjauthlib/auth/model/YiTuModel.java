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
public class YiTuModel extends BaseModel {
    private YiTuResultModel idcard_ocr_result;

    public YiTuModel() {
    }

    public YiTuResultModel getIdcard_ocr_result() {
        return idcard_ocr_result;
    }

    public void setIdcard_ocr_result(YiTuResultModel idcard_ocr_result) {
        this.idcard_ocr_result = idcard_ocr_result;
    }
}
