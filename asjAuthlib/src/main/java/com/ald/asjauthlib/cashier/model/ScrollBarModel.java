package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * Created by sean yu on 2017/7/27.
 */

public class ScrollBarModel extends BaseModel {
    private String wordUrl;
    private String name;
    private String type;
    private String content;

    public String getWordUrl() {
        return wordUrl;
    }

    public void setWordUrl(String wordUrl) {
        this.wordUrl = wordUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
