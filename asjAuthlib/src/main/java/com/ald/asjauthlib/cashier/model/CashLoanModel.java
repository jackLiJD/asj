package com.ald.asjauthlib.cashier.model;

import com.ald.asjauthlib.auth.model.BannerModel;
import com.ald.asjauthlib.authframework.core.network.entity.BaseModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yushumin on 2017/3/24.
 */

public class CashLoanModel extends BaseModel {
    private List<BannerModel> bannerList;

    private long rid;
    private String minAmount;
    private String maxAmount;
    //可借款天数
    private String borrowCashDay;
    //服务费日利率
    //状态【0 DEFAULT:未发起借款申请，1 APPLY:审核中，2 WAITTRANSED:待打款， 4.TRANSED:已经打款/待还款,5 CLOSED:关闭, 6 FINSH:已结清, 】
    private String status;
    //打款时间
    private String gmtArrival;
    //逾期率
    private String overdueRate;
    //
    private String poundageRate;

    private String bankDoubleRate;

    private BigDecimal amount;
    private BigDecimal arrivalAmount;
    private BigDecimal returnAmount;
    private BigDecimal serviceFee;//服务费
    private BigDecimal interestFee;//利率
    private BigDecimal paidAmount;
    private BigDecimal overdueAmount;
    private String overdueDay;

    private String overdueStatus;
    //
    private String deadlineDay;
    //
    private long repaymentDay;

    private BigDecimal rebateAmount;

    private String lender;

    private String canBorrow;

    private String loanMoney;

    private String loanNum;

    private int jfbAmount;

    private String renewalStatus;

    private String supermanPopupSwitch;//是否打开下载借贷超人弹窗
    private String isCancel;//是否显示关闭图标
    private String showTopBanner;//是否显示顶部布局

    private String existRepayingMoney;//是否存在还款处理中金额(Y是 N否)

    private BigDecimal repayingMoney;//还款中处理金额(默认0)

    private String inRejectLoan;//是否拒绝在本平台发起借款,即在风控拒绝借款期限内(Y是 N否)

    private String jumpToRejectPage;//是否跳转至审核不通过页面(Y是 N否 即直接跳转到贷款超市列表页面)

    private String jumpPageBannerUrl;//借款审核不通过跳转页面中图片URL.依赖于jumpToRejectPage，当jumpToRejectPage值为Y时,会传递此参数

    private String riskStatus;//是否走过认证的强风控[A未审核 N未通过审核 P审核中 Y已通过审核]

    private String showPacket; //时是否显示奖励红包

    private String couponId;//优惠券Id

    private ScrollBarModel scrollbar;

    private String showRiskPacket;    // 是否显示风控拒绝红包，Y表示显示，N表示不显示

    private String showRatePopup; //显示用户分层率息变更弹窗

    private String companyName;//新版借钱公司名称

    public CashLoanModel() {
    }

    public String getIsCancel() {
        return isCancel;
    }

    public String getShowTopBanner() {
        return showTopBanner;
    }

    public void setShowTopBanner(String showTopBanner) {
        this.showTopBanner = showTopBanner;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

    public String getSupermanPopupSwitch() {
        return supermanPopupSwitch;
    }

    public void setSupermanPopupSwitch(String supermanPopupSwitch) {
        this.supermanPopupSwitch = supermanPopupSwitch;
    }

    public String getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(String overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public String getDeadlineDay() {
        return deadlineDay != null ? deadlineDay : "0";
    }

    public void setDeadlineDay(String deadlineDay) {
        this.deadlineDay = deadlineDay;
    }

    public long getRepaymentDay() {
        return repaymentDay;
    }

    public void setRepaymentDay(long repaymentDay) {
        this.repaymentDay = repaymentDay;
    }

    public List<BannerModel> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerModel> bannerList) {
        this.bannerList = bannerList;
    }

    public String getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(String overdueRate) {
        this.overdueRate = overdueRate;
    }

    public String getPoundageRate() {
        return poundageRate;
    }

    public void setPoundageRate(String poundageRate) {
        this.poundageRate = poundageRate;
    }

    public String getBankDoubleRate() {
        return bankDoubleRate;
    }

    public void setBankDoubleRate(String bankDoubleRate) {
        this.bankDoubleRate = bankDoubleRate;
    }

    public BigDecimal getAmount() {
        return amount != null ? amount : new BigDecimal(0);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getArrivalAmount() {
        return arrivalAmount != null ? arrivalAmount : new BigDecimal(0);
    }

    public void setArrivalAmount(BigDecimal arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount != null ? returnAmount : new BigDecimal(0);
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getInterestFee() {
        return interestFee;
    }

    public void setInterestFee(BigDecimal interestFee) {
        this.interestFee = interestFee;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount != null ? overdueAmount : new BigDecimal(0);
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getOverdueDay() {
        return overdueDay;
    }

    public void setOverdueDay(String overdueDay) {
        this.overdueDay = overdueDay;
    }

    public BigDecimal getActPoundageRate() {
        return poundageRate != null ? new BigDecimal(poundageRate) : new BigDecimal(0);
    }

    public BigDecimal getActBankDoubleRate() {
        return bankDoubleRate != null ? new BigDecimal(bankDoubleRate) : new BigDecimal(0);
    }

    public BigDecimal getActOverdueRate() {
        return overdueRate != null ? new BigDecimal(overdueRate) : new BigDecimal(0);
    }

    public BigDecimal getMaxAmount() {
        return maxAmount != null ? new BigDecimal(maxAmount) : new BigDecimal(0);
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public BigDecimal getMinAmount() {
        return minAmount != null ? new BigDecimal(minAmount) : new BigDecimal(0);
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getServceRate() {
        return getActPoundageRate();
    }

    public String getGmtArrival() {
        return gmtArrival;
    }

    public void setGmtArrival(String gmtArrival) {
        this.gmtArrival = gmtArrival;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public String getBorrowCashDay() {
        return borrowCashDay;
    }

    public void setBorrowCashDay(String borrowCashDay) {
        this.borrowCashDay = borrowCashDay;
    }

    public String[] getBorrowDay() {
        if (borrowCashDay != null) {
            return borrowCashDay.split(",");
        }
        return new String[]{"7", "8"};
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public BigDecimal getRebateAmount() {
        return rebateAmount != null ? rebateAmount : new BigDecimal(0f);
    }

    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public String getCanBorrow() {
        return canBorrow;
    }

    public void setCanBorrow(String canBorrow) {
        this.canBorrow = canBorrow;
    }

    public String getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(String loanMoney) {
        this.loanMoney = loanMoney;
    }

    public String getLoanNum() {
        return loanNum;
    }

    public void setLoanNum(String loanNum) {
        this.loanNum = loanNum;
    }

    public int getJfbAmount() {
        return jfbAmount;
    }

    public void setJfbAmount(int jfbAmount) {
        this.jfbAmount = jfbAmount;
    }

    public String getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(String renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public String getExistRepayingMoney() {
        return existRepayingMoney;
    }

    public void setExistRepayingMoney(String existRepayingMoney) {
        this.existRepayingMoney = existRepayingMoney;
    }

    public BigDecimal getRepayingMoney() {
        return repayingMoney;
    }

    public void setRepayingMoney(BigDecimal repayingMoney) {
        this.repayingMoney = repayingMoney;
    }

    public String getInRejectLoan() {
        return inRejectLoan;
    }

    public void setInRejectLoan(String inRejectLoan) {
        this.inRejectLoan = inRejectLoan;
    }

    public String getJumpToRejectPage() {
        return jumpToRejectPage;
    }

    public void setJumpToRejectPage(String jumpToRejectPage) {
        this.jumpToRejectPage = jumpToRejectPage;
    }

    public String getJumpPageBannerUrl() {
        return jumpPageBannerUrl;
    }

    public void setJumpPageBannerUrl(String jumpPageBannerUrl) {
        this.jumpPageBannerUrl = jumpPageBannerUrl;
    }

    public String getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }

    public String getShowPacket() {
        return showPacket;
    }

    public void setShowPacket(String showPacket) {
        this.showPacket = showPacket;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public ScrollBarModel getScrollbar() {
        return scrollbar;
    }

    public void setScrollbar(ScrollBarModel scrollbar) {
        this.scrollbar = scrollbar;
    }

    public String getShowRiskPacket() {
        return showRiskPacket;
    }

    public void setShowRiskPacket(String showRiskPacket) {
        this.showRiskPacket = showRiskPacket;
    }

    public String getShowRatePopup() {
        return showRatePopup;
    }

    public void setShowRatePopup(String showRatePopup) {
        this.showRatePopup = showRatePopup;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
