package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 支付宝组合支付
 * Created by ywd on 2018/8/6.
 */

public class CpaliModel implements Parcelable {
    private String status;
    public CpaliModel() {
    }

    public CpaliModel(Parcel in) {
        this.status = in.readString();
    }

    public static final Creator<CpaliModel> CREATOR=new Creator<CpaliModel>() {
        @Override
        public CpaliModel createFromParcel(Parcel source) {
            return new CpaliModel(source);
        }

        @Override
        public CpaliModel[] newArray(int size) {
            return new CpaliModel[size];
        }
    };

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
        dest.writeString(status);
    }
}
