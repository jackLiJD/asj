package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/14.
 * 还款页面数据返回
 */

public class MyRepaymentModel implements Parcelable {
    BigDecimal overdueMoney;//逾期金额
    BigDecimal outMoney;//已出未逾期金额
    int month;//已出月份
    BigDecimal notOutMoney;//下月未出金额
    BigDecimal rebateAmount;//用户余额
    int nextMonth;//下月月份

    public MyRepaymentModel() {

    }

    protected MyRepaymentModel(Parcel in) {
        month = in.readInt();
        overdueBills = in.createStringArrayList();
        outBills = in.createStringArrayList();
        notOutBills = in.createStringArrayList();
        nextMonth = in.readInt();
    }

    public static final Creator<MyRepaymentModel> CREATOR = new Creator<MyRepaymentModel>() {
        @Override
        public MyRepaymentModel createFromParcel(Parcel in) {
            return new MyRepaymentModel(in);
        }

        @Override
        public MyRepaymentModel[] newArray(int size) {
            return new MyRepaymentModel[size];
        }
    };

    public List<String> getOverdueBills() {
        return overdueBills;
    }

    public void setOverdueBills(List<String> overdueBills) {
        this.overdueBills = overdueBills;
    }

    public List<String> getOutBills() {
        return outBills;
    }

    public void setOutBills(List<String> outBills) {
        this.outBills = outBills;
    }

    public List<String> getNotOutBills() {
        return notOutBills;
    }

    public void setNotOutBills(List<String> notOutBills) {
        this.notOutBills = notOutBills;
    }

    List<String> overdueBills;//逾期bills
    List<String> outBills; //已出bills
    List<String> notOutBills;//未出bill;

    public BigDecimal getOverdueMoney() {
        return overdueMoney;
    }

    public void setOverdueMoney(BigDecimal overdueMoney) {
        this.overdueMoney = overdueMoney;
    }

    public BigDecimal getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(BigDecimal outMoney) {
        this.outMoney = outMoney;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getNotOutMoney() {
        return notOutMoney;
    }

    public void setNotOutMoney(BigDecimal notOutMoney) {
        this.notOutMoney = notOutMoney;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public int getNextMonth() {
        return nextMonth;
    }

    public void setNextMonth(int nextMonth) {
        this.nextMonth = nextMonth;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(month);
        dest.writeStringList(overdueBills);
        dest.writeStringList(outBills);
        dest.writeStringList(notOutBills);
        dest.writeInt(nextMonth);
    }
}
