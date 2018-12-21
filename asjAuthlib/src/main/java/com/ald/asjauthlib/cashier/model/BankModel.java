package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 银行卡支付
 * Created by ywd on 2017/10/30.
 */

public class BankModel implements Parcelable {
    private String reasonType;//详见CashierConstant
    private String status;

    public BankModel() {

    }

    protected BankModel(Parcel in) {
        reasonType = in.readString();
        status = in.readString();
    }

    public static final Creator<BankModel> CREATOR = new Creator<BankModel>() {
        @Override
        public BankModel createFromParcel(Parcel in) {
            return new BankModel(in);
        }

        @Override
        public BankModel[] newArray(int size) {
            return new BankModel[size];
        }
    };

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reasonType);
        dest.writeString(status);
    }
}
