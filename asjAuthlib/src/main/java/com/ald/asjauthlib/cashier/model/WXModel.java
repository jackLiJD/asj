package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 微信支付
 * Created by ywd on 2017/10/30.
 */

public class WXModel extends BaseModel implements Parcelable {
    private String reasonType;
    private String status;

    public WXModel() {

    }

    protected WXModel(Parcel in) {
        reasonType = in.readString();
        status = in.readString();
    }

    public static final Creator<WXModel> CREATOR = new Creator<WXModel>() {
        @Override
        public WXModel createFromParcel(Parcel in) {
            return new WXModel(in);
        }

        @Override
        public WXModel[] newArray(int size) {
            return new WXModel[size];
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
