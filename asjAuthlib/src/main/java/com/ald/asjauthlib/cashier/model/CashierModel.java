package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;


/**
 * 请求收银台返回实体类
 * Created by ywd on 2017/10/30.
 */

public class CashierModel implements Parcelable {
    private AliModel ali;//支付宝支付
    private String amount;//订单金额
    private APModel ap;//分期支付
    private BankModel bank;//银行卡支付
    private boolean countDown;//是否需要倒计时
    private CPModel cp;//组合支付
    private CreditModel credit;//信用支付
    private long currentTime;//当前时间，用于倒计时
    private String faceStatus;//人脸识别状态
    private long gmtPayEnd;//支付结束时间
    private String idNumber;//身份证号
    private String isSupplyCertify;//
    private CashierMainBankCardModel mainBankCard;//主卡信息
    private long orderId;//订单id
    private String orderType;//订单类型
    private String realName;//真实姓名
    private String realNameScore;
    private String rebatedAmount;//返利金额
    private String riskStatus;//风控状态
    private WXModel wx;//微信支付
    private List<BankCardModel> bankCardList;
    private String scene;
    private CpaliModel cpali;
    private String mobileStatus;
    private String onlineStatus;
    private String orderTypeExt;//有值表示是自营中的拼团类型
    private int isExist;
    private String orderFrom;

    public String getOrderFrom() {
        return orderFrom == null ? "" : orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }

    public int getIsExist() {
        return isExist;
    }

    public void setIsExist(int isExist) {
        this.isExist = isExist;
    }

    public String getOrderTypeExt() {
        return orderTypeExt;
    }

    public void setOrderTypeExt(String orderTypeExt) {
        this.orderTypeExt = orderTypeExt;
    }

    public List<BankCardModel> getBankCardList() {
        return bankCardList;
    }

    public void setBankCardList(List<BankCardModel> bankCardList) {
        this.bankCardList = bankCardList;
    }

    public static Creator<CashierModel> getCREATOR() {
        return CREATOR;
    }

    protected CashierModel(Parcel in) {
        amount = in.readString();
        ap = in.readParcelable(APModel.class.getClassLoader());
        countDown = in.readByte() != 0;
        cp = in.readParcelable(CPModel.class.getClassLoader());
        credit = in.readParcelable(CreditModel.class.getClassLoader());
        currentTime = in.readLong();
        faceStatus = in.readString();
        gmtPayEnd = in.readLong();
        idNumber = in.readString();
        isSupplyCertify = in.readString();
        mainBankCard = in.readParcelable(CashierMainBankCardModel.class.getClassLoader());
        orderId = in.readLong();
        orderType = in.readString();
        realName = in.readString();
        realNameScore = in.readString();
        rebatedAmount = in.readString();
        riskStatus = in.readString();
        wx = in.readParcelable(WXModel.class.getClassLoader());
        bankCardList = in.readArrayList(BankCardModel.class.getClassLoader());
        scene = in.readString();
        cpali = in.readParcelable(CpaliModel.class.getClassLoader());
        mobileStatus = in.readString();
        onlineStatus = in.readString();
        orderTypeExt = in.readString();
        isExist = in.readInt();
        orderFrom=in.readString();
    }

    public CashierModel() {
    }

    public static final Creator<CashierModel> CREATOR = new Creator<CashierModel>() {
        @Override
        public CashierModel createFromParcel(Parcel in) {
            return new CashierModel(in);
        }

        @Override
        public CashierModel[] newArray(int size) {
            return new CashierModel[size];
        }
    };

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public AliModel getAli() {
        return ali;
    }

    public void setAli(AliModel ali) {
        this.ali = ali;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public APModel getAp() {
        return ap;
    }

    public void setAp(APModel ap) {
        this.ap = ap;
    }

    public BankModel getBank() {
        return bank;
    }

    public void setBank(BankModel bank) {
        this.bank = bank;
    }

    public boolean isCountDown() {
        return countDown;
    }

    public void setCountDown(boolean countDown) {
        this.countDown = countDown;
    }

    public CPModel getCp() {
        return cp;
    }

    public void setCp(CPModel cp) {
        this.cp = cp;
    }


    public CreditModel getCredit() {
        return credit;
    }

    public void setCredit(CreditModel credit) {
        this.credit = credit;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(String faceStatus) {
        this.faceStatus = faceStatus;
    }

    public long getGmtPayEnd() {
        return gmtPayEnd;
    }

    public void setGmtPayEnd(long gmtPayEnd) {
        this.gmtPayEnd = gmtPayEnd;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIsSupplyCertify() {
        return isSupplyCertify;
    }

    public void setIsSupplyCertify(String isSupplyCertify) {
        this.isSupplyCertify = isSupplyCertify;
    }

    public CashierMainBankCardModel getMainBankCard() {
        return mainBankCard;
    }

    public void setMainBankCard(CashierMainBankCardModel mainBankCard) {
        this.mainBankCard = mainBankCard;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealNameScore() {
        return realNameScore;
    }

    public void setRealNameScore(String realNameScore) {
        this.realNameScore = realNameScore;
    }

    public String getRebatedAmount() {
        return rebatedAmount;
    }

    public void setRebatedAmount(String rebatedAmount) {
        this.rebatedAmount = rebatedAmount;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public WXModel getWx() {
        return wx;
    }

    public void setWx(WXModel wx) {
        this.wx = wx;
    }

    public CpaliModel getCpali() {
        return cpali;
    }

    public void setCpali(CpaliModel cpali) {
        this.cpali = cpali;
    }

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount);
        dest.writeParcelable(ap, flags);
        dest.writeByte((byte) (countDown ? 1 : 0));
        dest.writeParcelable(cp, flags);
        dest.writeParcelable(credit, flags);
        dest.writeLong(currentTime);
        dest.writeString(faceStatus);
        dest.writeLong(gmtPayEnd);
        dest.writeString(idNumber);
        dest.writeString(isSupplyCertify);
        dest.writeParcelable(mainBankCard, flags);
        dest.writeLong(orderId);
        dest.writeString(orderType);
        dest.writeString(realName);
        dest.writeString(realNameScore);
        dest.writeString(rebatedAmount);
        dest.writeString(riskStatus);
        dest.writeParcelable(wx, flags);
        dest.writeList(bankCardList);
        dest.writeString(scene);
        dest.writeParcelable(cpali, flags);
        dest.writeString(mobileStatus);
        dest.writeString(onlineStatus);
        dest.writeString(orderTypeExt);
        dest.writeInt(isExist);
        dest.writeString(orderFrom);
    }

    public static class BankCardModel implements Parcelable {
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
        private String bankChannel;//KUAIJIE,代付
        private int cardType;
        private double creditRate;

        public BankCardModel() {

        }

        protected BankCardModel(Parcel in) {
            bankCode = in.readString();
            bankIcon = in.readString();
            bankName = in.readString();
            bankStatus = in.readParcelable(BankStatus.class.getClassLoader());
            cardNumber = in.readString();
            gmtCreate = in.readLong();
            gmtModified = in.readLong();
            isMain = in.readString();
            isValid = in.readString();
            mobile = in.readString();
            rid = in.readLong();
            status = in.readString();
            userId = in.readLong();
            bankChannel = in.readString();
            cardType = in.readInt();
            creditRate = in.readDouble();
        }

        public static final Creator<BankCardModel> CREATOR = new Creator<BankCardModel>() {
            @Override
            public BankCardModel createFromParcel(Parcel in) {
                return new BankCardModel(in);
            }

            @Override
            public BankCardModel[] newArray(int size) {
                return new BankCardModel[size];
            }
        };

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

        public double getCreditRate() {
            return creditRate;
        }

        public void setCreditRate(double creditRate) {
            this.creditRate = creditRate;
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

        public String getBankChannel() {
            return bankChannel;
        }

        public void setBankChannel(String bankChannel) {
            this.bankChannel = bankChannel;
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
            dest.writeParcelable(bankStatus, flags);
            dest.writeString(cardNumber);
            dest.writeLong(gmtCreate);
            dest.writeLong(gmtModified);
            dest.writeString(isMain);
            dest.writeString(isValid);
            dest.writeString(mobile);
            dest.writeLong(rid);
            dest.writeString(status);
            dest.writeLong(userId);
            dest.writeString(bankChannel);
            dest.writeInt(cardType);
            dest.writeDouble(creditRate);
        }
    }

    public static class BankStatus implements Parcelable {
        private BigDecimal dailyLimit;
        private Integer isMaintain;
        private BigDecimal limitDown;
        private BigDecimal limitUp;
        private String maintainEndtime;
        private String maintainStarttime;

        private BankStatus() {
        }

        protected BankStatus(Parcel in) {
            dailyLimit = new BigDecimal(in.readString());
            if (in.readByte() == 0) {
                isMaintain = null;
            } else {
                isMaintain = in.readInt();
            }
            limitDown = new BigDecimal(in.readString());
            limitUp = new BigDecimal(in.readString());
            maintainEndtime = in.readString();
            maintainStarttime = in.readString();
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

        public BigDecimal getDailyLimit() {
            return dailyLimit;
        }

        public void setDailyLimit(BigDecimal dailyLimit) {
            this.dailyLimit = dailyLimit;
        }

        public Integer getIsMaintain() {
            return isMaintain;
        }

        public void setIsMaintain(Integer isMaintain) {
            this.isMaintain = isMaintain;
        }

        public BigDecimal getLimitDown() {
            return limitDown;
        }

        public void setLimitDown(BigDecimal limitDown) {
            this.limitDown = limitDown;
        }

        public BigDecimal getLimitUp() {
            return limitUp;
        }

        public void setLimitUp(BigDecimal limitUp) {
            this.limitUp = limitUp;
        }

        public String getMaintainEndtime() {
            return maintainEndtime;
        }

        public void setMaintainEndtime(String maintainEndtime) {
            this.maintainEndtime = maintainEndtime;
        }

        public String getMaintainStarttime() {
            return maintainStarttime;
        }

        public void setMaintainStarttime(String maintainStarttime) {
            this.maintainStarttime = maintainStarttime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(dailyLimit == null ? "0" : dailyLimit.toString());
            if (isMaintain == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(isMaintain);
            }
            dest.writeString(limitDown == null ? "0" : limitDown.toString());
            dest.writeString(limitUp == null ? "0" : limitUp.toString());
            dest.writeString(maintainEndtime);
            dest.writeString(maintainStarttime);

        }
    }
}
