package com.ald.asjauthlib.cashier.model;

/**
 * Created by wjy on 2017/12/26.
 */

public class ItemStickyEntity {

    private int tag;
    private boolean isTitle;
    private String titleName;

    public ItemStickyEntity(int tag, boolean isTitle, String titleName) {
        this.tag = tag;
        this.isTitle = isTitle;
        this.titleName = titleName;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}
