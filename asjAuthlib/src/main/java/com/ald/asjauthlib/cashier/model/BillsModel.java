package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wjy on 2017/12/14.
 */

public class BillsModel implements Parcelable {

    private double money;
    private List<BillListBean> billList;

    public BillsModel() {
    }

    protected BillsModel(Parcel in) {
        money = in.readDouble();
    }

    public static final Creator<BillsModel> CREATOR = new Creator<BillsModel>() {
        @Override
        public BillsModel createFromParcel(Parcel in) {
            return new BillsModel(in);
        }

        @Override
        public BillsModel[] newArray(int size) {
            return new BillsModel[size];
        }
    };

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<BillListBean> getBillList() {
        return billList;
    }

    public void setBillList(List<BillListBean> billList) {
        this.billList = billList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(money);
    }

    public static class BillListBean implements Parcelable{
        private int year;
        private List<BillsBean> bills;

        public BillListBean() {
        }

        protected BillListBean(Parcel in) {
            year = in.readInt();
        }

        public static final Creator<BillListBean> CREATOR = new Creator<BillListBean>() {
            @Override
            public BillListBean createFromParcel(Parcel in) {
                return new BillListBean(in);
            }

            @Override
            public BillListBean[] newArray(int size) {
                return new BillListBean[size];
            }
        };

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public List<BillsBean> getBills() {
            return bills;
        }

        public void setBills(List<BillsBean> bills) {
            this.bills = bills;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(year);
        }

        public static class BillsBean implements Parcelable{

            private double billAmount;
            private int billMonth;
            private int billYear;
            private long gmtOutDay;
            private long gmtPayTime;
            private int isOut;
            private double overdueInterestAmount;
            private String overdueStatus;

            public BillsBean() {
            }

            protected BillsBean(Parcel in) {
                billAmount = in.readDouble();
                billMonth = in.readInt();
                billYear = in.readInt();
                gmtOutDay = in.readLong();
                gmtPayTime = in.readLong();
                isOut = in.readInt();
                overdueInterestAmount = in.readDouble();
                overdueStatus = in.readString();
            }

            public static final Creator<BillsBean> CREATOR = new Creator<BillsBean>() {
                @Override
                public BillsBean createFromParcel(Parcel in) {
                    return new BillsBean(in);
                }

                @Override
                public BillsBean[] newArray(int size) {
                    return new BillsBean[size];
                }
            };

            public double getBillAmount() {
                return billAmount;
            }

            public void setBillAmount(double billAmount) {
                this.billAmount = billAmount;
            }

            public int getBillMonth() {
                return billMonth;
            }

            public void setBillMonth(int billMonth) {
                this.billMonth = billMonth;
            }

            public int getBillYear() {
                return billYear;
            }

            public void setBillYear(int billYear) {
                this.billYear = billYear;
            }

            public long getGmtOutDay() {
                return gmtOutDay;
            }

            public void setGmtOutDay(long gmtOutDay) {
                this.gmtOutDay = gmtOutDay;
            }

            public long getGmtPayTime() {
                return gmtPayTime;
            }

            public void setGmtPayTime(long gmtPayTime) {
                this.gmtPayTime = gmtPayTime;
            }

            public int getIsOut() {
                return isOut;
            }

            public void setIsOut(int isOut) {
                this.isOut = isOut;
            }

            public double getOverdueInterestAmount() {
                return overdueInterestAmount;
            }

            public void setOverdueInterestAmount(double overdueInterestAmount) {
                this.overdueInterestAmount = overdueInterestAmount;
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
                dest.writeDouble(billAmount);
                dest.writeInt(billMonth);
                dest.writeInt(billYear);
                dest.writeLong(gmtOutDay);
                dest.writeLong(gmtPayTime);
                dest.writeInt(isOut);
                dest.writeDouble(overdueInterestAmount);
                dest.writeString(overdueStatus);
            }
        }
    }
}
