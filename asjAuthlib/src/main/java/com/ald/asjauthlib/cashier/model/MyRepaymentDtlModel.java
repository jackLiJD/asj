package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/14.
 * 还款详情底部弹框
 */

public class MyRepaymentDtlModel implements Parcelable {
    String status;//账单类型 overdue 逾期 out 某月已出 notOut 未出账单
    BigDecimal moneny;//总金额
    String month;//月份
    String payTime;//还款日
    List<Bill> billList;

    public MyRepaymentDtlModel() {
    }

    protected MyRepaymentDtlModel(Parcel in) {
        status = in.readString();
        month = in.readString();
        payTime = in.readString();
    }

    public static final Creator<MyRepaymentDtlModel> CREATOR = new Creator<MyRepaymentDtlModel>() {
        @Override
        public MyRepaymentDtlModel createFromParcel(Parcel in) {
            return new MyRepaymentDtlModel(in);
        }

        @Override
        public MyRepaymentDtlModel[] newArray(int size) {
            return new MyRepaymentDtlModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getMoneny() {
        return moneny;
    }

    public void setMoneny(BigDecimal moneny) {
        this.moneny = moneny;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public List<Bill> getBillList() {
        return billList;
    }

    public void setBillList(List<Bill> billList) {
        this.billList = billList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(month);
        dest.writeString(payTime);
    }

    public static class Bill implements Parcelable {
        long rid;
        String name;
        BigDecimal billAmount;//子账单金额
        int billNper;//所属账期
        int nper;//总分期数；
        long payDate;//支付时间

        public Bill() {
        }

        protected Bill(Parcel in) {
            rid = in.readLong();
            name = in.readString();
            billNper = in.readInt();
            nper = in.readInt();
            payDate = in.readLong();
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

        public BigDecimal getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(BigDecimal billAmount) {
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
            dest.writeLong(rid);
            dest.writeString(name);
            dest.writeInt(billNper);
            dest.writeInt(nper);
            dest.writeLong(payDate);
        }
    }


}
