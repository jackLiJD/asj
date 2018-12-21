package com.ald.asjauthlib.auth.model;

/*
 * Created by liangchen on 2018/4/9.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class AuthUserBasicInfoModel implements Parcelable {

    private boolean isRealNameAuth;//是否实名认证
    private boolean isPayPwd;//是否设置支付密码
    private boolean isIdCard;//是否实名认证
    private boolean isBindIcCard;//是否绑定了银行卡

    public AuthUserBasicInfoModel() {

    }

    protected AuthUserBasicInfoModel(Parcel in) {
        isRealNameAuth = in.readByte() != 0;
        isPayPwd = in.readByte() != 0;
        isIdCard = in.readByte() != 0;
        isBindIcCard = in.readByte() != 0;
    }

    public static final Creator<AuthUserBasicInfoModel> CREATOR = new Creator<AuthUserBasicInfoModel>() {
        @Override
        public AuthUserBasicInfoModel createFromParcel(Parcel in) {
            return new AuthUserBasicInfoModel(in);
        }

        @Override
        public AuthUserBasicInfoModel[] newArray(int size) {
            return new AuthUserBasicInfoModel[size];
        }
    };

    public boolean isRealNameAuth() {
        return isRealNameAuth;
    }

    public void setRealNameAuth(boolean realNameAuth) {
        isRealNameAuth = realNameAuth;
    }

    public boolean isBindIcCard() {
        return isBindIcCard;
    }

    public void setBindIcCard(boolean bindIcCard) {
        isBindIcCard = bindIcCard;
    }

    public static Creator<AuthUserBasicInfoModel> getCREATOR() {
        return CREATOR;
    }

    public boolean isPayPwd() {
        return isPayPwd;
    }

    public void setPayPwd(boolean payPwd) {
        isPayPwd = payPwd;
    }

    public boolean isIdCard() {
        return isIdCard;
    }

    public void setIdCard(boolean idCard) {
        isIdCard = idCard;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isRealNameAuth ? 1 : 0));
        dest.writeByte((byte) (isPayPwd ? 1 : 0));
        dest.writeByte((byte) (isIdCard ? 1 : 0));
        dest.writeByte((byte) (isBindIcCard ? 1 : 0));
    }
}
