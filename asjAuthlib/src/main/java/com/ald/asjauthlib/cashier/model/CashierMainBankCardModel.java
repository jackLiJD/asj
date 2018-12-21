package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 主卡信息
 * Created by ywd on 2017/10/30.
 */

public class CashierMainBankCardModel implements Parcelable {
    private String bankCode;
    private String bankIcon;
    private String bankName;
    private String cardNumber;
    private long gmtCreate;
    private long gmtModified;
    private String isMain;
    private String isValid;
    private String mobile;
    private String rid;
    private String status;
    private String userId;

    public CashierMainBankCardModel() {

    }

    protected CashierMainBankCardModel(Parcel in) {
        bankCode = in.readString();
        bankIcon = in.readString();
        bankName = in.readString();
        cardNumber = in.readString();
        gmtCreate = in.readLong();
        gmtModified = in.readLong();
        isMain = in.readString();
        isValid = in.readString();
        mobile = in.readString();
        rid = in.readString();
        status = in.readString();
        userId = in.readString();
    }

    public static final Creator<CashierMainBankCardModel> CREATOR = new Creator<CashierMainBankCardModel>() {
        @Override
        public CashierMainBankCardModel createFromParcel(Parcel in) {
            return new CashierMainBankCardModel(in);
        }

        @Override
        public CashierMainBankCardModel[] newArray(int size) {
            return new CashierMainBankCardModel[size];
        }
    };

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankIcon() {
        return bankIcon;
    }

    public void setBankIcon(String bankIcon) {
        this.bankIcon = bankIcon;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getIsMain() {
        return isMain;
    }

    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankCode);
        dest.writeString(bankIcon);
        dest.writeString(bankName);
        dest.writeString(cardNumber);
        dest.writeLong(gmtCreate);
        dest.writeLong(gmtModified);
        dest.writeString(isMain);
        dest.writeString(isValid);
        dest.writeString(mobile);
        dest.writeString(rid);
        dest.writeString(status);
        dest.writeString(userId);
    }
}
