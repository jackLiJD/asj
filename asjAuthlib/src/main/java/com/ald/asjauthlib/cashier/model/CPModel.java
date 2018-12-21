package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 组合支付实体类
 * Created by ywd on 2017/10/30.
 */

public class CPModel implements Parcelable {
    private String payAmount;

    protected CPModel(Parcel in) {
        payAmount = in.readString();
        status = in.readString();
    }

    public CPModel() {
    }

    public static final Creator<CPModel> CREATOR = new Creator<CPModel>() {
        @Override
        public CPModel createFromParcel(Parcel in) {
            return new CPModel(in);
        }

        @Override
        public CPModel[] newArray(int size) {
            return new CPModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payAmount);
        dest.writeString(status);
    }

    private String status;

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
