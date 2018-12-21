package com.ald.asjauthlib.auth.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/20 10:50
 * 描述：
 * 修订历史：
 */
public class BankCardModel implements Parcelable {


    private String bankCode;
    private String bankIcon;
    private String bankName;
    private BankStatus bankStatus;
    private String cardNumber;
    private long gmtCreate;
    private long gmtModified;
    private String isMain;
    private String isValid;
    private String mobile;
    private long rid;
    private String status;
    private long userId;
    private String message;
    private int drawableRes;//支付宝，微信，线下还款的icon
    private String loanURL;//线下还款的URL
    private String bankChannel;//代收：DAISHOU，快捷：KUAIJIE
    private String cardName;
    private long cardType;//0 其它1 借记卡2 信用卡7 识别失败
    private String showPlan;//还款计划是否展示Y:展示N:不展示
    private Number limitAmount;//卡额度
    private Number repaymentDate;//还款日
    private Number statementDate;//账单日
    private Number color;//0:红1:蓝色：2绿色

    public String getShowPlan() {
        return showPlan;
    }

    public void setShowPlan(String showPlan) {
        this.showPlan = showPlan;
    }

    public Number getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(Number limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Number getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(Number repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public Number getStatementDate() {
        return statementDate;
    }

    public void setStatementDate(Number statementDate) {
        this.statementDate = statementDate;
    }

    public Number getColor() {
        return color;
    }

    public void setColor(Number color) {
        this.color = color;
    }

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

    public BankStatus getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(BankStatus bankStatus) {
        this.bankStatus = bankStatus;
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

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public String getLoanURL() {
        return loanURL;
    }

    public void setLoanURL(String loanURL) {
        this.loanURL = loanURL;
    }

    public String getBankChannel() {
        return bankChannel;
    }

    public void setBankChannel(String bankChannel) {
        this.bankChannel = bankChannel;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public long getCardType() {
        return cardType;
    }

    public void setCardType(long cardType) {
        this.cardType = cardType;
    }

    public static class BankStatus implements Parcelable {

        Integer limitDown;//单笔最低限额
        Integer limitUp;//单笔最高限额
        Integer dailyLimit;//单日累计限额
        String maintainStarttime;//维护起始时间
        String maintainEndtime;//维护结束时间


        public BankStatus() {

        }

        BankStatus(Parcel in) {
            if (in.readByte() == 0) {
                limitDown = null;
            } else {
                limitDown = in.readInt();
            }
            if (in.readByte() == 0) {
                limitUp = null;
            } else {
                limitUp = in.readInt();
            }
            if (in.readByte() == 0) {
                dailyLimit = null;
            } else {
                dailyLimit = in.readInt();
            }
            maintainStarttime = in.readString();
            maintainEndtime = in.readString();
        }

        public static final Creator<BankStatus> CREATOR = new Creator<BankStatus>() {
            @Override
            public BankStatus createFromParcel(Parcel in) {
                return new BankStatus(in);
            }

            @Override
            public BankStatus[] newArray(int size) {
                return new BankStatus[size];
            }
        };

        public Integer getLimitDown() {
            return limitDown;
        }

        public void setLimitDown(Integer limitDown) {
            this.limitDown = limitDown;
        }

        public Integer getLimitUp() {
            return limitUp;
        }

        public void setLimitUp(Integer limitUp) {
            this.limitUp = limitUp;
        }

        public Integer getDailyLimit() {
            return dailyLimit;
        }

        public void setDailyLimit(Integer dailyLimit) {
            this.dailyLimit = dailyLimit;
        }

        public String getMaintainStarttime() {
            return maintainStarttime;
        }

        public void setMaintainStarttime(String maintainStarttime) {
            this.maintainStarttime = maintainStarttime;
        }

        public String getMaintainEndtime() {
            return maintainEndtime;
        }

        public void setMaintainEndtime(String maintainEndtime) {
            this.maintainEndtime = maintainEndtime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (limitDown == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(limitDown);
            }
            if (limitUp == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(limitUp);
            }
            if (dailyLimit == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(dailyLimit);
            }
            dest.writeString(maintainStarttime);
            dest.writeString(maintainEndtime);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bankCode);
        dest.writeString(this.bankIcon);
        dest.writeString(this.bankName);
        dest.writeParcelable(this.bankStatus, flags);
        dest.writeString(this.cardNumber);
        dest.writeLong(this.gmtCreate);
        dest.writeLong(this.gmtModified);
        dest.writeString(this.isMain);
        dest.writeString(this.isValid);
        dest.writeString(this.mobile);
        dest.writeLong(this.rid);
        dest.writeString(this.status);
        dest.writeLong(this.userId);
        dest.writeString(this.message);
        dest.writeInt(this.drawableRes);
        dest.writeString(this.loanURL);
        dest.writeString(this.bankChannel);
        dest.writeString(this.cardName);
        dest.writeLong(this.cardType);
        dest.writeString(this.showPlan);
        dest.writeSerializable(this.limitAmount);
        dest.writeSerializable(this.repaymentDate);
        dest.writeSerializable(this.statementDate);
        dest.writeSerializable(this.color);
    }

    public BankCardModel() {
    }

    protected BankCardModel(Parcel in) {
        this.bankCode = in.readString();
        this.bankIcon = in.readString();
        this.bankName = in.readString();
        this.bankStatus = in.readParcelable(BankStatus.class.getClassLoader());
        this.cardNumber = in.readString();
        this.gmtCreate = in.readLong();
        this.gmtModified = in.readLong();
        this.isMain = in.readString();
        this.isValid = in.readString();
        this.mobile = in.readString();
        this.rid = in.readLong();
        this.status = in.readString();
        this.userId = in.readLong();
        this.message = in.readString();
        this.drawableRes = in.readInt();
        this.loanURL = in.readString();
        this.bankChannel = in.readString();
        this.cardName = in.readString();
        this.cardType = in.readLong();
        this.showPlan = in.readString();
        this.limitAmount = (Number) in.readSerializable();
        this.repaymentDate = (Number) in.readSerializable();
        this.statementDate = (Number) in.readSerializable();
        this.color = (Number) in.readSerializable();
    }

    public static final Creator<BankCardModel> CREATOR = new Creator<BankCardModel>() {
        @Override
        public BankCardModel createFromParcel(Parcel source) {
            return new BankCardModel(source);
        }

        @Override
        public BankCardModel[] newArray(int size) {
            return new BankCardModel[size];
        }
    };
}
