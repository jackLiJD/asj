package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/14.
 * 退还款详情返回 getRepaymentDetailV1
 */

public class RefundDtlModel implements Parcelable {
    private String number;//退还款编号
    private String date;//时间
    private BigDecimal amount;//退款总金额、实际还款金额
    private String name;//商品名称；
    private BigDecimal bankAmount;//直接支付金額（退款）
    private BigDecimal priceAmount;//商品总价（退款）
    private int nper;//总期数（退款）
    private BigDecimal nperAmount;//每期金额（退款）
    private int nperRepayment;//已还期数（退款）
    private List<Detail> detailList;
    private List<Log> logList;


    //退还款明细
    public static class Detail implements Parcelable {
        String title;//标题 例如：本金
        int type;
        BigDecimal amount;//金额
        int count;//期数

        public Detail() {
        }


        Detail(Parcel in) {
            title = in.readString();
            type = in.readInt();
            count = in.readInt();
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

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }


        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(title);
            dest.writeInt(type);
            dest.writeInt(count);
        }

    }


    //退还款进度（还款）
    public static class Log implements Parcelable {

        int status;//状态 提交 处理中 处理成功
        long gmtCreate;//时间

        public Log() {
        }

        protected Log(Parcel in) {
            status = in.readInt();
            gmtCreate = in.readLong();
        }

        public static final Creator<Log> CREATOR = new Creator<Log>() {
            @Override
            public Log createFromParcel(Parcel in) {
                return new Log(in);
            }

            @Override
            public Log[] newArray(int size) {
                return new Log[size];
            }
        };

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(status);
            dest.writeLong(gmtCreate);
        }
    }

    public RefundDtlModel() {
        
    }

    private RefundDtlModel(Parcel in) {
        number = in.readString();
        date = in.readString();
        name = in.readString();
        nper = in.readInt();
        nperRepayment = in.readInt();
        detailList = in.createTypedArrayList(Detail.CREATOR);
        logList = in.createTypedArrayList(Log.CREATOR);
    }

    public static final Creator<RefundDtlModel> CREATOR = new Creator<RefundDtlModel>() {
        @Override
        public RefundDtlModel createFromParcel(Parcel in) {
            return new RefundDtlModel(in);
        }

        @Override
        public RefundDtlModel[] newArray(int size) {
            return new RefundDtlModel[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(BigDecimal bankAmount) {
        this.bankAmount = bankAmount;
    }

    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }

    public int getNper() {
        return nper;
    }

    public void setNper(int nper) {
        this.nper = nper;
    }

    public BigDecimal getNperAmount() {
        return nperAmount;
    }

    public void setNperAmount(BigDecimal nperAmount) {
        this.nperAmount = nperAmount;
    }

    public int getNperRepayment() {
        return nperRepayment;
    }

    public void setNperRepayment(int nperRepayment) {
        this.nperRepayment = nperRepayment;
    }


    public List<Log> getLogList() {
        return logList;
    }

    public void setLogList(List<Log> logList) {
        this.logList = logList;
    }

    public List<Detail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<Detail> detailList) {
        this.detailList = detailList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(number);
        dest.writeString(date);
        dest.writeString(name);
        dest.writeInt(nper);
        dest.writeInt(nperRepayment);
        dest.writeTypedList(detailList);
        dest.writeTypedList(logList);
    }


}
