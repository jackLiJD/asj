package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wjy on 2018/3/14.
 */

public class FundModel implements Parcelable {

    private String fundSwitch;

    public FundModel() {
    }

    protected FundModel(Parcel in) {
        fundSwitch = in.readString();
    }

    public static final Creator<FundModel> CREATOR = new Creator<FundModel>() {
        @Override
        public FundModel createFromParcel(Parcel in) {
            return new FundModel(in);
        }

        @Override
        public FundModel[] newArray(int size) {
            return new FundModel[size];
        }
    };

    public String getFundSwitch() {
        return fundSwitch;
    }

    public void setFundSwitch(String fundSwitch) {
        this.fundSwitch = fundSwitch;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fundSwitch);
    }
}
