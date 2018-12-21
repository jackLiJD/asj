package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/6.
 * 退还款记录返回
 * getMyRepaymentHistoryV1 返回
 */

public class RefundModel implements Parcelable {

    //repayment 还款
    //refund 退款
    private String status;
    private List<Month> list;

    public RefundModel() {
    }

    protected RefundModel(Parcel in) {
        status = in.readString();
        list = in.createTypedArrayList(Month.CREATOR);
    }

    public static final Creator<RefundModel> CREATOR = new Creator<RefundModel>() {
        @Override
        public RefundModel createFromParcel(Parcel in) {
            return new RefundModel(in);
        }

        @Override
        public RefundModel[] newArray(int size) {
            return new RefundModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Month> getList() {
        return list;
    }

    public void setList(List<Month> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeTypedList(list);
    }

    public static class Month implements Parcelable {
        private int month;
        private int year;

        private List<Amount> amountList;

        public Month() {
        }

        protected Month(Parcel in) {
            month = in.readInt();
            amountList = in.createTypedArrayList(Amount.CREATOR);
        }

        public static final Creator<Month> CREATOR = new Creator<Month>() {
            @Override
            public Month createFromParcel(Parcel in) {
                return new Month(in);
            }

            @Override
            public Month[] newArray(int size) {
                return new Month[size];
            }
        };

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public List<Amount> getAmountList() {
            return amountList;
        }

        public void setAmountList(List<Amount> amountList) {
            this.amountList = amountList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(month);
            dest.writeTypedList(amountList);
        }
    }

    public static class Amount implements Parcelable {
        private long id;//主键
        private String remark;//退还款名称
        private BigDecimal amount;//金额
        private long gmtModified;//时间
        private int status;//0 新建 ，1处理中，2成功。3失败


        public Amount() {
        }


        protected Amount(Parcel in) {
            id = in.readLong();
            remark = in.readString();
            gmtModified = in.readLong();
            status = in.readInt();
        }

        public static final Creator<Amount> CREATOR = new Creator<Amount>() {
            @Override
            public Amount createFromParcel(Parcel in) {
                return new Amount(in);
            }

            @Override
            public Amount[] newArray(int size) {
                return new Amount[size];
            }
        };

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public long getGmtModified() {
            return gmtModified;
        }

        public void setGmtModified(long gmtModified) {
            this.gmtModified = gmtModified;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeLong(id);
            dest.writeString(remark);
            dest.writeLong(gmtModified);
            dest.writeInt(status);
        }


    }
}
