package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wjy on 2018/1/24.
 */

public class ExtraFundModel implements Parcelable {

    private String url;

    public ExtraFundModel() {
    }

    protected ExtraFundModel(Parcel in) {
        url = in.readString();
    }

    public static final Creator<ExtraFundModel> CREATOR = new Creator<ExtraFundModel>() {
        @Override
        public ExtraFundModel createFromParcel(Parcel in) {
            return new ExtraFundModel(in);
        }

        @Override
        public ExtraFundModel[] newArray(int size) {
            return new ExtraFundModel[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }
}
