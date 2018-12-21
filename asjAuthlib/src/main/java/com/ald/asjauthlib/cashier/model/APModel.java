package com.ald.asjauthlib.cashier.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 代付实体类
 * Created by ywd on 2017/10/30.
 */

public class APModel implements Parcelable {
    private String payAmount;
    private String reasonType;//详见CashierConstant
    private String status;
    private String useableAmount;
    private String isVirtualGoods;//是否是虚拟商品
    private String totalVirtualAmount;//虚拟商品每月总额度
    private String categoryName;//虚拟类目名称
    private String billId;//当ovderduedCode=103时，才有
    private String jfbAmount;//集分宝 当ovderduedCode=102时，才有
    private String userRebateAmount;//用户返利金额 当ovderduedCode=102时，才有
    private String repaymentAmount;//用户应还金额 当ovderduedCode=102时，才有
    private String virtualGoodsUsableAmount;//虚拟商品可用额度
    //Code值 判断是分期逾期还是借钱逾期
    //102风控逾期借款控制 跳转订单详情 getBillDetailInfo
    //103风控逾期分期控制 跳转借钱首页 getBorrowHomeInfo
    //0000 代表可以借钱
    private String overduedCode;
    private String borrowId;//

    protected APModel(Parcel in) {
        payAmount = in.readString();
        reasonType = in.readString();
        status = in.readString();
        useableAmount = in.readString();
        isVirtualGoods = in.readString();
        totalVirtualAmount = in.readString();
        categoryName = in.readString();
        billId = in.readString();
        jfbAmount = in.readString();
        userRebateAmount = in.readString();
        repaymentAmount = in.readString();
        virtualGoodsUsableAmount = in.readString();
        overduedCode = in.readString();
        borrowId = in.readString();
    }

    public APModel() {

    }

    public static final Creator<APModel> CREATOR = new Creator<APModel>() {
        @Override
        public APModel createFromParcel(Parcel in) {
            return new APModel(in);
        }

        @Override
        public APModel[] newArray(int size) {
            return new APModel[size];
        }
    };

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getReasonType() {
        return reasonType;
    }

    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUseableAmount() {
        return useableAmount;
    }

    public void setUseableAmount(String useableAmount) {
        this.useableAmount = useableAmount;
    }

    public String getIsVirtualGoods() {
        return isVirtualGoods;
    }

    public void setIsVirtualGoods(String isVirtualGoods) {
        this.isVirtualGoods = isVirtualGoods;
    }

    public String getTotalVirtualAmount() {
        return totalVirtualAmount;
    }

    public void setTotalVirtualAmount(String totalVirtualAmount) {
        this.totalVirtualAmount = totalVirtualAmount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getJfbAmount() {
        return jfbAmount;
    }

    public void setJfbAmount(String jfbAmount) {
        this.jfbAmount = jfbAmount;
    }

    public String getUserRebateAmount() {
        return userRebateAmount;
    }

    public void setUserRebateAmount(String userRebateAmount) {
        this.userRebateAmount = userRebateAmount;
    }

    public String getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(String repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public String getVirtualGoodsUsableAmount() {
        return virtualGoodsUsableAmount;
    }

    public void setVirtualGoodsUsableAmount(String virtualGoodsUsableAmount) {
        this.virtualGoodsUsableAmount = virtualGoodsUsableAmount;
    }

    public String getOverduedCode() {
        return overduedCode;
    }

    public void setOverduedCode(String overduedCode) {
        this.overduedCode = overduedCode;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(payAmount);
        dest.writeString(reasonType);
        dest.writeString(status);
        dest.writeString(useableAmount);
        dest.writeString(isVirtualGoods);
        dest.writeString(totalVirtualAmount);
        dest.writeString(categoryName);
        dest.writeString(billId);
        dest.writeString(jfbAmount);
        dest.writeString(userRebateAmount);
        dest.writeString(repaymentAmount);
        dest.writeString(virtualGoodsUsableAmount);
        dest.writeString(overduedCode);
        dest.writeString(borrowId);
    }
}
