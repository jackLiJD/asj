package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

/**
 * 信用支付
 * Created by ywd on 2017/10/30.
 */

public class CreditModel extends BaseModel implements Parcelable {
    private String payAmount;
    private String reasonType;
    private String status;
    private String useableAmount;

    public CreditModel() {
        
    }

    protected CreditModel(Parcel in) {
        payAmount = in.readString();
        reasonType = in.readString();
        status = in.readString();
        useableAmount = in.readString();
    }

    public static final Creator<CreditModel> CREATOR = new Creator<CreditModel>() {
        @Override
        public CreditModel createFromParcel(Parcel in) {
            return new CreditModel(in);
        }

        @Override
        public CreditModel[] newArray(int size) {
            return new CreditModel[size];
        }
    };

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

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

    public String getUseableAmount() {
        return useableAmount;
    }

    public void setUseableAmount(String useableAmount) {
        this.useableAmount = useableAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payAmount);
        dest.writeString(reasonType);
        dest.writeString(status);
        dest.writeString(useableAmount);
    }
}
