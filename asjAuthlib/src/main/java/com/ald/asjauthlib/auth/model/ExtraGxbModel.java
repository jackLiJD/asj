package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;


/*
 * Created by liangchen on 2018/3/3.
 */

public class ExtraGxbModel implements Parcelable {
    private String url;

    protected ExtraGxbModel(Parcel in) {
        url = in.readString();
    }

    public ExtraGxbModel() {
        
    }

    public static final Creator<ExtraGxbModel> CREATOR = new Creator<ExtraGxbModel>() {
        @Override
        public ExtraGxbModel createFromParcel(Parcel in) {
            return new ExtraGxbModel(in);
        }

        @Override
        public ExtraGxbModel[] newArray(int size) {
            return new ExtraGxbModel[size];
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
