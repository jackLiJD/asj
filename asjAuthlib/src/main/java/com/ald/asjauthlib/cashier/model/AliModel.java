package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 支付宝支付实体类
 * Created by ywd on 2017/10/30.
 */

public class AliModel implements Parcelable {
    private String status;

    public AliModel() {
    }

    protected AliModel(Parcel in) {
        status = in.readString();
    }

    public static final Creator<AliModel> CREATOR = new Creator<AliModel>() {
        @Override
        public AliModel createFromParcel(Parcel in) {
            return new AliModel(in);
        }

        @Override
        public AliModel[] newArray(int size) {
            return new AliModel[size];
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
