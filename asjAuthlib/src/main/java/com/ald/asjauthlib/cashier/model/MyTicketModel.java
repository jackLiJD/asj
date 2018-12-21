package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;

/**
 * 版权：XXX公司 版权所有
 * 作者：TonyChen
 * 版本：1.0
 * 创建日期：2017/3/4
 * 描述：我的优惠券类
 * 修订历史：
 */
public class MyTicketModel extends BaseModel {

    //优惠券ID
    private long rid;

    //优惠券名字
    private String name;

    //优惠金额 (打折券时，该字段为优惠限额）
    private BigDecimal amount;

    //限制启用金额
    private BigDecimal limitAmount;

    //当为折扣时可抵用最大金额
    private BigDecimal maxAmount;

    //使用须知
    private String useRule;

    //开始时间
    private long gmtStart;

    //截止时间
    private long gmtEnd;

    private boolean isSelect;//是否选中


    private BigDecimal discount;//折扣

    private BigDecimal useCouponAmount;//实际折扣

    public MyTicketModel() {

    }

    protected MyTicketModel(Parcel in) {
        rid = in.readLong();
        name = in.readString();
        useRule = in.readString();
        gmtStart = in.readLong();
        gmtEnd = in.readLong();
        isSelect = in.readByte() != 0;
        type = in.readString();
        status = in.readString();
        willExpireStatus = in.readString();
        willExpireDay = in.readString();
        shopUrl = in.readString();
        useRange = in.readString();
        discount = new BigDecimal(in.readString());
        limitAmount = new BigDecimal(in.readString());
        maxAmount = new BigDecimal(in.readString());
        amount = new BigDecimal(in.readString());
        useCouponAmount = new BigDecimal(in.readString());
    }


    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    //	优惠券类型
    //(品牌【1：金额满减,2：金额满折,3:数量满减,4:数量满折】)
    //MOBILE:充值券 REPAYMENT:还款券 FULLVOUCHER:满减券 CASH:现金券 ACTIVITY:会场券 FREEINTEREST:借钱免息券
    private String type;

    //状态：EXPIRE:过期 ; NOUSE:未使用 ， USED:已使用
    private String status;

    //即将过期状态(Y即将过期 N没有即将过期)
    private String willExpireStatus;

    private String willExpireDay;

    //跳转链接
    //若列表为菠萝觅，则未返回该字段则跳转逛逛首页,返回该字段则跳转H5页面
    //若列表为优惠券，则未返回该字段则跳转APP首页，返回该字段则跳转H5页面
    private String shopUrl;

    //使用范围说明
    private String useRange;


    //---------------------------------------


    public BigDecimal getUseCouponAmount() {
        return useCouponAmount;
    }

    public void setUseCouponAmount(BigDecimal useCouponAmount) {
        this.useCouponAmount = useCouponAmount;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : BigDecimal.ZERO;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getUseRule() {
        return useRule;
    }

    public void setUseRule(String useRule) {
        this.useRule = useRule;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getGmtStart() {
        return gmtStart;
    }

    public void setGmtStart(long gmtStart) {
        this.gmtStart = gmtStart;
    }

    public long getGmtEnd() {
        return gmtEnd;
    }

    public void setGmtEnd(long gmtEnd) {
        this.gmtEnd = gmtEnd;
    }

    public String getWillExpireStatus() {
        return willExpireStatus;
    }

    public void setWillExpireStatus(String willExpireStatus) {
        this.willExpireStatus = willExpireStatus;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getUseRange() {
        return useRange;
    }

    public void setUseRange(String useRange) {
        this.useRange = useRange;
    }

}

