package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sean yu on 2017/5/31.
 */

public class SteadBuyModel extends BaseModel {
    private List<SteadBuyItemModel> nperList;
    private BigDecimal instalmentAmount;//待支付金额
    private BigDecimal useableAmount;//当前可用分期额度
    private String isQuotaGoods;//是否是限额商品
    //当isQuotaGoods为Y时,有以下参数
    private String categoryName;//类目名称
    private BigDecimal goodsTotalAmount;//类目商品单月总额度
    private BigDecimal goodsUseableAmount;//类目商品本月剩余可用额度

    public List<SteadBuyItemModel> getNperList() {
        return nperList;
    }

    public void setNperList(List<SteadBuyItemModel> nperList) {
        this.nperList = nperList;
    }

    public BigDecimal getInstalmentAmount() {
        return instalmentAmount;
    }

    public void setInstalmentAmount(BigDecimal instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public BigDecimal getUseableAmount() {
        return useableAmount;
    }

    public void setUseableAmount(BigDecimal useableAmount) {
        this.useableAmount = useableAmount;
    }

    public String getIsQuotaGoods() {
        return isQuotaGoods;
    }

    public void setIsQuotaGoods(String isQuotaGoods) {
        this.isQuotaGoods = isQuotaGoods;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getGoodsTotalAmount() {
        return goodsTotalAmount;
    }

    public void setGoodsTotalAmount(BigDecimal goodsTotalAmount) {
        this.goodsTotalAmount = goodsTotalAmount;
    }

    public BigDecimal getGoodsUseableAmount() {
        return goodsUseableAmount;
    }

    public void setGoodsUseableAmount(BigDecimal goodsUseableAmount) {
        this.goodsUseableAmount = goodsUseableAmount;
    }
}
