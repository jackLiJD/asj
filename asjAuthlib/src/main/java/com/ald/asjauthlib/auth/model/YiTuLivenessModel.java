package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.util.List;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/22 15:22
 * 描述：
 * 修订历史：
 */
public class YiTuLivenessModel extends BaseModel {
    private List<String> list;

    public YiTuLivenessModel() {
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
