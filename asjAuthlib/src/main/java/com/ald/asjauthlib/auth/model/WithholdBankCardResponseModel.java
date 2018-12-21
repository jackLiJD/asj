package com.ald.asjauthlib.auth.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.util.List;

/**
 * 获取代扣/未代扣银行卡
 * Created by ywd on 2017/11/13.
 */

public class WithholdBankCardResponseModel extends BaseModel {
    private List<WithholdBankCardModel> card1;//已代扣列表
    private List<WithholdBankCardModel> card2;//未代扣列表

    public List<WithholdBankCardModel> getCard1() {
        return card1;
    }

    public void setCard1(List<WithholdBankCardModel> card1) {
        this.card1 = card1;
    }

    public List<WithholdBankCardModel> getCard2() {
        return card2;
    }

    public void setCard2(List<WithholdBankCardModel> card2) {
        this.card2 = card2;
    }
}