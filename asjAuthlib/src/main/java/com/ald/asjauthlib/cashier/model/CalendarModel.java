package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by luckyliang on 2017/12/6.
 */
public class CalendarModel implements Parcelable {

    //repayment 还款
    //refund 退款
    String status;
    List<Integer> monthList;

    public CalendarModel() {
    }

    protected CalendarModel(Parcel in) {
        status = in.readString();
    }

    public static final Creator<CalendarModel> CREATOR = new Creator<CalendarModel>() {
        @Override
        public CalendarModel createFromParcel(Parcel in) {
            return new CalendarModel(in);
        }

        @Override
        public CalendarModel[] newArray(int size) {
            return new CalendarModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<Integer> monthList) {
        this.monthList = monthList;
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
