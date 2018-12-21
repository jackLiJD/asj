package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 版权：XXX公司 版权所有
 * 作者：TonyChen
 * 版本：1.0
 * 创建日期：2017/3/4
 * 描述：
 * 修订历史：
 */
public class MyTicketListModel implements Parcelable {
    private ArrayList couponList;

    private int pageNo;
    private int nextPageNo;//下一页页码
    private int totalCount;//优惠券总数
    private String couponCenterUrl;//领券中心URL,返回则显示领券中心,否则不显示

    //-------------------------------------


    public MyTicketListModel() {

    }

    protected MyTicketListModel(Parcel in) {
        couponList = in.readArrayList(MyTicketModel.class.getClassLoader());
        pageNo = in.readInt();
        nextPageNo = in.readInt();
        totalCount = in.readInt();
        couponCenterUrl = in.readString();
    }

    public static final Creator<MyTicketListModel> CREATOR = new Creator<MyTicketListModel>() {
        @Override
        public MyTicketListModel createFromParcel(Parcel in) {
            return new MyTicketListModel(in);
        }

        @Override
        public MyTicketListModel[] newArray(int size) {
            return new MyTicketListModel[size];
        }
    };

    public ArrayList<MyTicketModel> getCouponList() {
        return couponList;
    }

    public void setCouponList(ArrayList<MyTicketModel> couponList) {
        this.couponList = couponList;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getNextPageNo() {
        return nextPageNo;
    }

    public void setNextPageNo(int nextPageNo) {
        this.nextPageNo = nextPageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getCouponCenterUrl() {
        return couponCenterUrl;
    }

    public void setCouponCenterUrl(String couponCenterUrl) {
        this.couponCenterUrl = couponCenterUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(couponList);
        dest.writeInt(pageNo);
        dest.writeInt(nextPageNo);
        dest.writeInt(totalCount);
        dest.writeString(couponCenterUrl);

    }
}
