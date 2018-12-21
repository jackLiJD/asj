package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wjy on 2017/12/19.
 */

public class BillMonthModel implements Parcelable{

    private String status;
    private String str;
    private String end;
    private BigDecimal money;
    private int overdueBillCount;
    private BigDecimal overdeuMoney;
    private BigDecimal overdeuInterest;
    private String lastPayDay;
    private int notInCount;
    private BigDecimal notInMoney;
    private String outDay;
    private List<BorrowBean> borrowList; // 未入账账单
    private List<BillListBean> billList;

    public BillMonthModel() {
    }

    protected BillMonthModel(Parcel in) {
        status = in.readString();
        str = in.readString();
        end = in.readString();
        money = new BigDecimal(in.readString());
        overdueBillCount = in.readInt();
        overdeuMoney = new BigDecimal(in.readString());
        overdeuInterest = new BigDecimal(in.readString());
        lastPayDay = in.readString();
        notInCount = in.readInt();
        notInMoney = new BigDecimal(in.readString());
        outDay = in.readString();
        borrowList = in.createTypedArrayList(BorrowBean.CREATOR);
        billList = in.createTypedArrayList(BillListBean.CREATOR);
    }

    public static final Creator<BillMonthModel> CREATOR = new Creator<BillMonthModel>() {
        @Override
        public BillMonthModel createFromParcel(Parcel in) {
            return new BillMonthModel(in);
        }

        @Override
        public BillMonthModel[] newArray(int size) {
            return new BillMonthModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getOverdueBillCount() {
        return overdueBillCount;
    }

    public void setOverdueBillCount(int overdueBillCount) {
        this.overdueBillCount = overdueBillCount;
    }

    public BigDecimal getOverdeuMoney() {
        return overdeuMoney;
    }

    public void setOverdeuMoney(BigDecimal overdeuMoney) {
        this.overdeuMoney = overdeuMoney;
    }

    public BigDecimal getOverdeuInterest() {
        return overdeuInterest;
    }

    public void setOverdeuInterest(BigDecimal overdeuInterest) {
        this.overdeuInterest = overdeuInterest;
    }

    public String getLastPayDay() {
        return lastPayDay;
    }

    public void setLastPayDay(String lastPayDay) {
        this.lastPayDay = lastPayDay;
    }

    public int getNotInCount() {
        return notInCount;
    }

    public void setNotInCount(int notInCount) {
        this.notInCount = notInCount;
    }

    public BigDecimal getNotInMoney() {
        return notInMoney;
    }

    public void setNotInMoney(BigDecimal notInMoney) {
        this.notInMoney = notInMoney;
    }

    public String getOutDay() {
        return outDay;
    }

    public void setOutDay(String outDay) {
        this.outDay = outDay;
    }

    public List<BorrowBean> getBorrowList() {
        return borrowList;
    }

    public void setBorrowList(List<BorrowBean> borrowList) {
        this.borrowList = borrowList;
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
        dest.writeString(status);
        dest.writeString(str);
        dest.writeString(end);
        dest.writeString(money == null ? "0" : money.toString());
        dest.writeInt(overdueBillCount);
        dest.writeString(overdeuMoney == null ? "0" : overdeuMoney.toString());
        dest.writeString(overdeuInterest == null ? "0" : overdeuInterest.toString());
        dest.writeString(lastPayDay);
        dest.writeInt(notInCount);
        dest.writeString(notInMoney == null ? "0" : notInMoney.toString());
        dest.writeString(outDay);
        dest.writeTypedList(borrowList);
        dest.writeTypedList(billList);
    }

    public static class BorrowBean implements Parcelable{
        private int rid;
        private String name;
        private BigDecimal amount;
        private long payDate;

        public BorrowBean() {
        }

        protected BorrowBean(Parcel in) {
            rid = in.readInt();
            name = in.readString();
            //读取的为String类型, 需要转为BigDecimal
            amount = new BigDecimal(in.readString());
            payDate = in.readLong();
        }

        public static final Creator<BorrowBean> CREATOR = new Creator<BorrowBean>() {
            @Override
            public BorrowBean createFromParcel(Parcel in) {
                return new BorrowBean(in);
            }

            @Override
            public BorrowBean[] newArray(int size) {
                return new BorrowBean[size];
            }
        };

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public long getPayDate() {
            return payDate;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(rid);
            dest.writeString(name);
            //判空, 因为如果为null,会报异常, 没有写入BigDecimal的数据类型, 需要转为String
            dest.writeString(amount == null ? "0" : amount.toString());
            dest.writeLong(payDate);
        }
    }

    public static class BillListBean implements Parcelable{

        private int rid;
        private String name;
        private double billAmount;
        private int billNper;
        private int nper;
        private long payDate;

        public BillListBean() {
        }

        protected BillListBean(Parcel in) {
            rid = in.readInt();
            name = in.readString();
            billAmount = in.readDouble();
            billNper = in.readInt();
            nper = in.readInt();
            payDate = in.readLong();
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

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(double billAmount) {
            this.billAmount = billAmount;
        }

        public int getBillNper() {
            return billNper;
        }

        public void setBillNper(int billNper) {
            this.billNper = billNper;
        }

        public int getNper() {
            return nper;
        }

        public void setNper(int nper) {
            this.nper = nper;
        }

        public long getPayDate() {
            return payDate;
        }

        public void setPayDate(long payDate) {
            this.payDate = payDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(rid);
            dest.writeString(name);
            dest.writeDouble(billAmount);
            dest.writeInt(billNper);
            dest.writeInt(nper);
            dest.writeLong(payDate);
        }
    }
}
