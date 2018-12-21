package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/15.
 */

public class SettleAdvancedModel implements Parcelable {

    List<Bill> result = new ArrayList<>();

    public SettleAdvancedModel() {

    }

    protected SettleAdvancedModel(Parcel in) {
    }

    public static final Creator<SettleAdvancedModel> CREATOR = new Creator<SettleAdvancedModel>() {
        @Override
        public SettleAdvancedModel createFromParcel(Parcel in) {
            return new SettleAdvancedModel(in);
        }

        @Override
        public SettleAdvancedModel[] newArray(int size) {
            return new SettleAdvancedModel[size];
        }
    };

    public List<Bill> getResult() {
        return result;
    }

    public void setResult(List<Bill> result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static class Bill implements Parcelable {
        long borrowId;        //借款编号
        String title;        //标题
        int startNper;      //开始还的期数
        int endNper;        //结止期数

        public Bill() {
            
        }

        protected Bill(Parcel in) {
            borrowId = in.readLong();
            title = in.readString();
            startNper = in.readInt();
            endNper = in.readInt();
            nper = in.readInt();
            detailList = in.createTypedArrayList(Detail.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(borrowId);
            dest.writeString(title);
            dest.writeInt(startNper);
            dest.writeInt(endNper);
            dest.writeInt(nper);
            dest.writeTypedList(detailList);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Bill> CREATOR = new Creator<Bill>() {
            @Override
            public Bill createFromParcel(Parcel in) {
                return new Bill(in);
            }

            @Override
            public Bill[] newArray(int size) {
                return new Bill[size];
            }
        };

        public long getBorrowId() {
            return borrowId;
        }

        public void setBorrowId(long borrowId) {
            this.borrowId = borrowId;
        }

        int nper;            //总期数
        BigDecimal amount;   //总金额

        public List<Detail> detailList;  //详情


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStartNper() {
            return startNper;
        }

        public void setStartNper(int startNper) {
            this.startNper = startNper;
        }

        public int getEndNper() {
            return endNper;
        }

        public void setEndNper(int endNper) {
            this.endNper = endNper;
        }

        public int getNper() {
            return nper;
        }

        public void setNper(int nper) {
            this.nper = nper;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public List<Detail> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<Detail> detailList) {
            this.detailList = detailList;
        }
    }

    public static class Detail implements Parcelable {
        long billId;
        int nper;  //期数
        BigDecimal amount; //金额
        BigDecimal poundAmount; //手续费
        BigDecimal interest;    //预期利息
        int status;             //1己出帐  ,0未出帐
        int overdue;            //1 逾期，0正常
        Boolean free;         //是否勉手续费;

        public Detail() {

        }

        protected Detail(Parcel in) {
            billId = in.readLong();
            nper = in.readInt();
            status = in.readInt();
            overdue = in.readInt();
        }

        public static final Creator<Detail> CREATOR = new Creator<Detail>() {
            @Override
            public Detail createFromParcel(Parcel in) {
                return new Detail(in);
            }

            @Override
            public Detail[] newArray(int size) {
                return new Detail[size];
            }
        };

        public long getBillId() {
            return billId;
        }

        public void setBillId(long billId) {
            this.billId = billId;
        }

        public int getNper() {
            return nper;
        }

        public void setNper(int nper) {
            this.nper = nper;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getPoundAmount() {
            return poundAmount;
        }

        public void setPoundAmount(BigDecimal poundAmount) {
            this.poundAmount = poundAmount;
        }

        public BigDecimal getInterest() {
            return interest;
        }

        public void setInterest(BigDecimal interest) {
            this.interest = interest;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getOverdue() {
            return overdue;
        }

        public void setOverdue(int overdue) {
            this.overdue = overdue;
        }

        public Boolean getFree() {
            return free;
        }

        public void setFree(Boolean free) {
            this.free = free;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(billId);
            dest.writeInt(nper);
            dest.writeInt(status);
            dest.writeInt(overdue);
        }
    }
}
