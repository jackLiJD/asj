package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/*
 * Created by luckyliang on 2017/12/19.
 */

public class BorrowHomeModel implements Parcelable {

    private BigDecimal auAmount;//信用额度
    BigDecimal amount;//可用额度
    private int overduedMonth;//逾期月数
    private BigDecimal outMoney;//已出待还金额
    private BigDecimal notOutMoeny;//未出账单金额
    private String lastPayDay;//最后还款日

    private int interimType;//是否已获取临时额度 0未获取 1 已获取
    private int failureStatus;//临时额度是否已失效 0未失效
    private int floatType;//是否已开启临时额度 1.已开启
    private String pic1;//跳转连接
    String name;//h5页面标题
    private String pic2;//图标地址
    private BigDecimal interimAmount;//总临时额度

    private BigDecimal onlineAuAmount;//购物授予额度
    private BigDecimal onlineAmount;//购物可用额度
    private BigDecimal trainAuAmount;//线下授予额度
    private BigDecimal trainAmount;//线下可用额度
    private String showAmount;//未通过强风控时的借钱额度
    private String desc;//未通过强风控时的借钱描述
    private String onlineShowAmount;//未通过强风控时,购物额度
    private String onlineDesc;//未通过强风控时,购物描述
    private String borrowStatus;// 借钱 1:尚未认证状态 2:认证一半退出 3:强风控失败 4:认证成功
    String onlineStatus;//购物 1:尚未认证状态 2:认证一半退出 3:强风控失败 4:认证成功
    BigDecimal interimUsed;//已使用臨時額度
    String status;//noBill 没有账单 bill有账单
    String realName;//真实姓名
    String onRepaymentCount;//还款中数量

    public String getOnRepaymentCount() {
        return onRepaymentCount;
    }

    public void setOnRepaymentCount(String onRepaymentCount) {
        this.onRepaymentCount = onRepaymentCount;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public static final Creator<BorrowHomeModel> CREATOR = new Creator<BorrowHomeModel>() {
        @Override
        public BorrowHomeModel createFromParcel(Parcel in) {
            return new BorrowHomeModel(in);
        }

        @Override
        public BorrowHomeModel[] newArray(int size) {
            return new BorrowHomeModel[size];
        }
    };

    public String getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(String borrowStatus) {
        this.borrowStatus = borrowStatus;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public BorrowHomeModel() {
    }

    public BorrowHomeModel(Parcel in) {
        overduedMonth = in.readInt();
        lastPayDay = in.readString();
        interimType = in.readInt();
        failureStatus = in.readInt();
        floatType = in.readInt();
        pic1 = in.readString();
        name = in.readString();
        pic2 = in.readString();
        showAmount = in.readString();
        desc = in.readString();
        onlineShowAmount = in.readString();
        onlineDesc = in.readString();
        borrowStatus = in.readString();
        onlineStatus = in.readString();
        status = in.readString();
        realName = in.readString();
        onRepaymentCount = in.readString();
    }

    public BigDecimal getOnlineAuAmount() {
        return onlineAuAmount;
    }

    public void setOnlineAuAmount(BigDecimal onlineAuAmount) {
        this.onlineAuAmount = onlineAuAmount;
    }

    public BigDecimal getOnlineAmount() {
        return onlineAmount;
    }

    public void setOnlineAmount(BigDecimal onlineAmount) {
        this.onlineAmount = onlineAmount;
    }

    public BigDecimal getTrainAuAmount() {
        return trainAuAmount;
    }

    public void setTrainAuAmount(BigDecimal trainAuAmount) {
        this.trainAuAmount = trainAuAmount;
    }

    public BigDecimal getTrainAmount() {
        return trainAmount;
    }

    public void setTrainAmount(BigDecimal trainAmount) {
        this.trainAmount = trainAmount;
    }

    public String getShowAmount() {
        return showAmount;
    }

    public void setShowAmount(String showAmount) {
        this.showAmount = showAmount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOnlineShowAmount() {
        return onlineShowAmount;
    }

    public void setOnlineShowAmount(String onlineShowAmount) {
        this.onlineShowAmount = onlineShowAmount;
    }

    public String getOnlineDesc() {
        return onlineDesc;
    }

    public void setOnlineDesc(String onlineDesc) {
        this.onlineDesc = onlineDesc;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getInterimType() {
        return interimType;
    }

    public void setInterimType(int interimType) {
        this.interimType = interimType;
    }

    public int getFailureStatus() {
        return failureStatus;
    }

    public void setFailureStatus(int failureStatus) {
        this.failureStatus = failureStatus;
    }

    public int getFloatType() {
        return floatType;
    }

    public void setFloatType(int floatType) {
        this.floatType = floatType;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public BigDecimal getInterimAmount() {
        return interimAmount;
    }

    public void setInterimAmount(BigDecimal interimAmount) {
        this.interimAmount = interimAmount;
    }

    public BigDecimal getInterimUsed() {
        return interimUsed;
    }

    public void setInterimUsed(BigDecimal interimUsed) {
        this.interimUsed = interimUsed;
    }


    public BigDecimal getAuAmount() {
        return auAmount;
    }

    public void setAuAmount(BigDecimal auAmount) {
        this.auAmount = auAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getOverduedMonth() {
        return overduedMonth;
    }

    public void setOverduedMonth(int overduedMonth) {
        this.overduedMonth = overduedMonth;
    }

    public BigDecimal getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(BigDecimal outMoney) {
        this.outMoney = outMoney;
    }

    public BigDecimal getNotOutMoeny() {
        return notOutMoeny;
    }

    public void setNotOutMoeny(BigDecimal notOutMoeny) {
        this.notOutMoeny = notOutMoeny;
    }

    public String getLastPayDay() {
        return lastPayDay;
    }

    public void setLastPayDay(String lastPayDay) {
        this.lastPayDay = lastPayDay;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(overduedMonth);
        dest.writeString(lastPayDay);
        dest.writeInt(interimType);
        dest.writeInt(failureStatus);
        dest.writeInt(floatType);
        dest.writeString(pic1);
        dest.writeString(name);
        dest.writeString(pic2);
        dest.writeString(showAmount);
        dest.writeString(desc);
        dest.writeString(onlineShowAmount);
        dest.writeString(onlineDesc);
        dest.writeString(borrowStatus);
        dest.writeString(onlineStatus);
        dest.writeString(status);
        dest.writeString(realName);
        dest.writeString(onRepaymentCount);
    }


}
