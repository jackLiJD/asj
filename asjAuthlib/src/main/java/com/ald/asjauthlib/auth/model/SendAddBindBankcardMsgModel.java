package com.ald.asjauthlib.auth.model;

/*
 * Created by liangchen on 2018/4/12.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class SendAddBindBankcardMsgModel implements Parcelable {
    private long bankId;//银行卡记录ID

    public SendAddBindBankcardMsgModel() {

    }

    protected SendAddBindBankcardMsgModel(Parcel in) {
        bankId = in.readLong();
    }

    public static final Creator<SendAddBindBankcardMsgModel> CREATOR = new Creator<SendAddBindBankcardMsgModel>() {
        @Override
        public SendAddBindBankcardMsgModel createFromParcel(Parcel in) {
            return new SendAddBindBankcardMsgModel(in);
        }

        @Override
        public SendAddBindBankcardMsgModel[] newArray(int size) {
            return new SendAddBindBankcardMsgModel[size];
        }
    };

    public long getBankId() {
        return bankId;
    }

    public void setBankId(long bankId) {
        this.bankId = bankId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(bankId);
    }
}
