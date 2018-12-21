package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/7.
 * borrow/getMyHistoryBorrowV1 返回
 */
public class HistoryBillListModel implements Parcelable {

    List<BillItem> list;

    public HistoryBillListModel() {

    }

    protected HistoryBillListModel(Parcel in) {
    }

    public static final Creator<HistoryBillListModel> CREATOR = new Creator<HistoryBillListModel>() {
        @Override
        public HistoryBillListModel createFromParcel(Parcel in) {
            return new HistoryBillListModel(in);
        }

        @Override
        public HistoryBillListModel[] newArray(int size) {
            return new HistoryBillListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static class BillItem implements Parcelable {
        int year;

        public BillItem() {
        }

        protected BillItem(Parcel in) {
            year = in.readInt();
        }

        public static final Creator<BillItem> CREATOR = new Creator<BillItem>() {
            @Override
            public BillItem createFromParcel(Parcel in) {
                return new BillItem(in);
            }

            @Override
            public BillItem[] newArray(int size) {
                return new BillItem[size];
            }
        };

        public List<Bill> getBills() {
            return bills;
        }

        public void setBills(List<Bill> bills) {
            this.bills = bills;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        List<Bill> bills;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(year);
        }
    }

    public List<BillItem> getList() {
        return list;
    }

    public void setList(List<BillItem> list) {
        this.list = list;
    }

    public static class Bill implements Parcelable {

        //账单金额
        BigDecimal billAmount;
        //月份
        int billMonth;
        //逾期笔数
        int overdueDays;
        //逾期状态 Y逾期 N未逾期
        String overdueStatus;

        public Bill() {

        }

        protected Bill(Parcel in) {
            billMonth = in.readInt();
            overdueDays = in.readInt();
            overdueStatus = in.readString();
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

        public BigDecimal getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(BigDecimal billAmount) {
            this.billAmount = billAmount;
        }

        public int getBillMonth() {
            return billMonth;
        }

        public void setBillMonth(int billMonth) {
            this.billMonth = billMonth;
        }

        public int getOverdueDays() {
            return overdueDays;
        }

        public void setOverdueDays(int overdueDays) {
            this.overdueDays = overdueDays;
        }

        public String getOverdueStatus() {
            return overdueStatus;
        }

        public void setOverdueStatus(String overdueStatus) {
            this.overdueStatus = overdueStatus;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(billMonth);
            dest.writeInt(overdueDays);
            dest.writeString(overdueStatus);
        }
    }

}
