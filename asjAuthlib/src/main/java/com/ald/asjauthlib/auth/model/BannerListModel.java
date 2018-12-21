package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by luckyliang on 2017/12/1.
 * 引流轮播图
 */
public class BannerListModel implements Parcelable {
    private List<BannerModel> bannerList;

    public BannerListModel() {
    }

    protected BannerListModel(Parcel in) {
        bannerList = in.createTypedArrayList(BannerModel.CREATOR);
    }

    public static final Creator<BannerListModel> CREATOR = new Creator<BannerListModel>() {
        @Override
        public BannerListModel createFromParcel(Parcel in) {
            return new BannerListModel(in);
        }

        @Override
        public BannerListModel[] newArray(int size) {
            return new BannerListModel[size];
        }
    };

    public List<BannerModel> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerModel> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(bannerList);
    }
}
