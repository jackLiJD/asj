package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luckyliang on 2017/12/11.
 * 消费明细接口返回 /borrow/getBorrowDetailV1
 */

public class ConsumeDtlModel implements Parcelable{

    private BigDecimal amount;//账单本金
    private BigDecimal interest;//手续费
    private BigDecimal overdueInterest;//逾期利息
    private List<StageDtl> billList;//分期詳情
    private Order orderDetail;//消费详情
    private String borrowNo;
    private long gmtBorrow;
    private long borrowId;

    public ConsumeDtlModel() {
    }

    protected ConsumeDtlModel(Parcel in) {
        amount = new BigDecimal(in.readString());
        interest = new BigDecimal(in.readString());
        overdueInterest = new BigDecimal(in.readString());
        billList = in.createTypedArrayList(StageDtl.CREATOR);
        orderDetail = in.readParcelable(Order.class.getClassLoader());
        borrowNo = in.readString();
        gmtBorrow = in.readLong();
        borrowId = in.readLong();
    }

    public static final Creator<ConsumeDtlModel> CREATOR = new Creator<ConsumeDtlModel>() {
        @Override
        public ConsumeDtlModel createFromParcel(Parcel in) {
            return new ConsumeDtlModel(in);
        }

        @Override
        public ConsumeDtlModel[] newArray(int size) {
            return new ConsumeDtlModel[size];
        }
    };

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(BigDecimal overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public List<StageDtl> getBillList() {
        return billList;
    }

    public void setBillList(List<StageDtl> billList) {
        this.billList = billList;
    }

    public Order getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(Order orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getBorrowNo() {
        return borrowNo;
    }

    public void setBorrowNo(String borrowNo) {
        this.borrowNo = borrowNo;
    }

    public long getGmtBorrow() {
        return gmtBorrow;
    }

    public void setGmtBorrow(long gmtBorrow) {
        this.gmtBorrow = gmtBorrow;
    }

    public long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(long borrowId) {
        this.borrowId = borrowId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(amount == null ? "0" : amount.toString());
        dest.writeString(interest == null ? "0" : interest.toString());
        dest.writeString(overdueInterest == null ? "0" : overdueInterest.toString());
        dest.writeTypedList(billList);
        dest.writeParcelable(orderDetail, flags);
        dest.writeString(borrowNo);
        dest.writeLong(gmtBorrow);
        dest.writeLong(borrowId);
    }

    public static class StageDtl implements Parcelable{
        private BigDecimal billAmount;//子账单金额
        private BigDecimal interestAmount;//手续费
        private String status;//还款状态
        private String overdueStatus;//逾期状态 Y:已逾期 N:未逾期
        private BigDecimal overdueInterestAmount;//逾期利息
        private long gmtPayTime; //最后还款日
        private int billNper; // 账单期数

        public StageDtl() {
        }

        public StageDtl(Parcel in) {
            billAmount = new BigDecimal(in.readString());
            interestAmount = new BigDecimal(in.readString());
            status = in.readString();
            overdueStatus = in.readString();
            overdueInterestAmount = new BigDecimal(in.readString());
            gmtPayTime = in.readLong();
            billNper = in.readInt();
        }

        public static final Creator<StageDtl> CREATOR = new Creator<StageDtl>() {
            @Override
            public StageDtl createFromParcel(Parcel in) {
                return new StageDtl(in);
            }

            @Override
            public StageDtl[] newArray(int size) {
                return new StageDtl[size];
            }
        };

        public BigDecimal getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(BigDecimal billAmount) {
            this.billAmount = billAmount;
        }

        public BigDecimal getInterestAmount() {
            return interestAmount;
        }

        public void setInterestAmount(BigDecimal interestAmount) {
            this.interestAmount = interestAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOverdueStatus() {
            return overdueStatus;
        }

        public void setOverdueStatus(String overdueStatus) {
            this.overdueStatus = overdueStatus;
        }

        public BigDecimal getOverdueInterestAmount() {
            return overdueInterestAmount;
        }

        public void setOverdueInterestAmount(BigDecimal overdueInterestAmount) {
            this.overdueInterestAmount = overdueInterestAmount;
        }

        public long getGmtPayTime() {
            return gmtPayTime;
        }

        public void setGmtPayTime(long gmtPayTime) {
            this.gmtPayTime = gmtPayTime;
        }

        public int getBillNper() {
            return billNper;
        }

        public void setBillNper(int billNper) {
            this.billNper = billNper;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(billAmount == null ? "0" : billAmount.toString());
            dest.writeString(interestAmount == null ? "0" : interestAmount.toString());
            dest.writeString(status);
            dest.writeString(overdueStatus);
            dest.writeString(overdueInterestAmount == null ? "0" : overdueInterestAmount.toString());
            dest.writeLong(gmtPayTime);
            dest.writeInt(billNper);
        }
    }

    public static class Order implements Parcelable{
        private String orderNo;
        private String shopName;
        private String payType;//付款方式
        private BigDecimal borrowAmount;//分期金额
        private BigDecimal bankAmount;//銀行支付金額
        private int nper;//分期數
        private long gmtPay;//账单创建时间
        private String goodsIcon;//商品图片
        private BigDecimal priceAmount;//原价
        private BigDecimal saleAmount;//支付价
        private BigDecimal rebateAmount;
        private String goodsName;

        public Order() {
        }

        public Order(Parcel in) {
            orderNo = in.readString();
            shopName = in.readString();
            payType = in.readString();
            borrowAmount = new BigDecimal(in.readString());
            bankAmount = new BigDecimal(in.readString());
            nper = in.readInt();
            gmtPay = in.readLong();
            goodsIcon = in.readString();
            priceAmount = new BigDecimal(in.readString());
            saleAmount = new BigDecimal(in.readString());
            rebateAmount = new BigDecimal(in.readString());
            goodsName = in.readString();
        }

        public static final Creator<Order> CREATOR = new Creator<Order>() {
            @Override
            public Order createFromParcel(Parcel in) {
                return new Order(in);
            }

            @Override
            public Order[] newArray(int size) {
                return new Order[size];
            }
        };

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public BigDecimal getBorrowAmount() {
            return borrowAmount;
        }

        public void setBorrowAmount(BigDecimal borrowAmount) {
            this.borrowAmount = borrowAmount;
        }

        public BigDecimal getBankAmount() {
            return bankAmount;
        }

        public void setBankAmount(BigDecimal bankAmount) {
            this.bankAmount = bankAmount;
        }

        public int getNper() {
            return nper;
        }

        public void setNper(int nper) {
            this.nper = nper;
        }

        public long getGmtPay() {
            return gmtPay;
        }

        public void setGmtPay(long gmtPay) {
            this.gmtPay = gmtPay;
        }

        public String getGoodsIcon() {
            return goodsIcon;
        }

        public void setGoodsIcon(String goodsIcon) {
            this.goodsIcon = goodsIcon;
        }

        public BigDecimal getPriceAmount() {
            return priceAmount;
        }

        public void setPriceAmount(BigDecimal priceAmount) {
            this.priceAmount = priceAmount;
        }

        public BigDecimal getSaleAmount() {
            return saleAmount;
        }

        public void setSaleAmount(BigDecimal saleAmount) {
            this.saleAmount = saleAmount;
        }

        public BigDecimal getRebateAmount() {
            return rebateAmount;
        }

        public void setRebateAmount(BigDecimal rebateAmount) {
            this.rebateAmount = rebateAmount;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(orderNo);
            dest.writeString(shopName);
            dest.writeString(payType);
            dest.writeString(borrowAmount == null ? "0" : borrowAmount.toString());
            dest.writeString(bankAmount == null ? "0" : bankAmount.toString());
            dest.writeInt(nper);
            dest.writeLong(gmtPay);
            dest.writeString(goodsIcon);
            dest.writeString(priceAmount == null ? "0" : priceAmount.toString());
            dest.writeString(saleAmount == null ? "0" : saleAmount.toString());
            dest.writeString(rebateAmount == null ? "0" : rebateAmount.toString());
            dest.writeString(goodsName);
        }
    }
}
