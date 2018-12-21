package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 代扣银行实体类
 * Created by ywd on 2017/11/13.
 */

public class WithholdBankCardModel extends BaseModel {
    private String card;//银行卡名称
    private String cardId;//银行卡ID
    private String isMain;//是否是主卡
    private String sort;//排序 1:已代扣 2:未代扣


    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
