package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/11.
 * 历史账单详情接口返回
 */
public class HistoryBillMonthDtlModel implements Parcelable {
    String str = "";//账单起点
    String end = "";//账单重点
    List<SubBill> billList;//月账单列表

    public HistoryBillMonthDtlModel() {
    }

    protected HistoryBillMonthDtlModel(Parcel in) {
        str = in.readString();
        end = in.readString();
    }

    public static final Creator<HistoryBillMonthDtlModel> CREATOR = new Creator<HistoryBillMonthDtlModel>() {
        @Override
        public HistoryBillMonthDtlModel createFromParcel(Parcel in) {
            return new HistoryBillMonthDtlModel(in);
        }

        @Override
        public HistoryBillMonthDtlModel[] newArray(int size) {
            return new HistoryBillMonthDtlModel[size];
        }
    };

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

    public List<SubBill> getBillList() {
        return billList;
    }

    public void setBillList(List<SubBill> billList) {
        this.billList = billList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(str);
        dest.writeString(end);
    }

    public static class SubBill implements Parcelable {
        long rid;
        String name;//账单名称
        BigDecimal billAmount;//子账单金额
        int billNper;//所属账期
        int nper;//总分期数
        Date payDate;//支付时间

        public SubBill() {
        }

        protected SubBill(Parcel in) {
            rid = in.readLong();
            name = in.readString();
            billNper = in.readInt();
            nper = in.readInt();
        }

        public static final Creator<SubBill> CREATOR = new Creator<SubBill>() {
            @Override
            public SubBill createFromParcel(Parcel in) {
                return new SubBill(in);
            }

            @Override
            public SubBill[] newArray(int size) {
                return new SubBill[size];
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

        public Date getPayDate() {
            return payDate;
        }

        public void setPayDate(Date payDate) {
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
        }
    }

}
