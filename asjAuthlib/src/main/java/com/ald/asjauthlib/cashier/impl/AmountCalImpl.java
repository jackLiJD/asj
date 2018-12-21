package com.ald.asjauthlib.cashier.impl;

import android.support.annotation.NonNull;


import com.ald.asjauthlib.cashier.utils.IAccountIRepayment;
import com.ald.asjauthlib.cashier.utils.IRepaymentModule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 金额计算
 * Created by sean yu on 2017/4/14.
 */
public class AmountCalImpl {

	//还款金额，EditText中输入的还款金额
    private BigDecimal payAmount = new BigDecimal("0");

	/**
	 * 还款金额 减去 优惠金额(优惠券、集分宝...)
	 */
    private BigDecimal actualAmount = new BigDecimal("0");

    //需要支付的金额
    private BigDecimal extraAmount = new BigDecimal("0");

    private List<IAccountIRepayment> accountList = new ArrayList<>();
    private List<IAccountIRepayment> useAccountList = new ArrayList<>();

    private List<IRepaymentModule> offerList = new ArrayList<>();
    private List<IRepaymentModule> useOfferList = new ArrayList<>();


    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = new BigDecimal(payAmount);
    }

    public <T extends IAccountIRepayment> void setAccountInfo(@NonNull T accountInfo) {
        accountList.add(accountInfo);
    }

    /**
     * 修改账户信息
     *
     * @param accountInfo
     * @param <T>
     */
    public <T extends IAccountIRepayment> void changAccountInfo(@NonNull T accountInfo) {
        if (accountList.contains(accountInfo)) {
            int position = accountList.indexOf(accountInfo);
            accountList.add(position, accountInfo);
        } else {
            accountList.add(accountInfo);
        }
    }

    public <T extends IAccountIRepayment> void setAccountList(@NonNull List<T> accountInfo) {
        accountList.addAll(accountInfo);
    }

    public <T extends IRepaymentModule> void settOfferInfo(@NonNull T offerInfo) {
        offerList.add(offerInfo);
    }

    /**
     * 修改列表中的优惠信息
     *
     * @param offerInfo
     * @param <T>
     */
    public <T extends IRepaymentModule> void changeOffer(@NonNull T offerInfo) {
        if (offerList.contains(offerInfo)) {
            int position = offerList.indexOf(offerInfo);
            offerList.add(position, offerInfo);
        } else {
            offerList.add(offerInfo);
        }
    }

    public <T extends IRepaymentModule> void settOfferList(@NonNull List<T> offerList) {
        this.offerList.addAll(offerList);
    }

    /**
     * 计算金额信息
     */
    public void calAmountInfo(String payAmount) {
        setPayAmount(payAmount);
        calAmountInfo();
    }

    /**
	 * 设置还款金额，计算其他的金额
     */
    public void calAmountInfo(BigDecimal payAmount) {
        setPayAmount(payAmount);
        calAmountInfo();
    }


    /**
     * 计算金额信息
     */
    public void calAmountInfo() {
        calActualAmount();
        calExtraAmount();
    }

    /**
     * 计算优惠后的金额以及使用的优惠信息
     */
    private void calActualAmount() {
        int len = offerList.size();
        actualAmount = payAmount;
        for (int i = 0; i < len; i++) {
            IRepaymentModule offerInfo = offerList.get(i);
            if (offerInfo.isUse()) {	// 使用优惠券
                actualAmount = actualAmount.subtract(offerInfo.getValueAmount());
                useOfferList.add(offerInfo);
            }
            if (actualAmount.compareTo(BigDecimal.ZERO) < 1) {
                actualAmount = BigDecimal.ZERO;
               // return;
            }
        }
    }

    /**
     * 计算实际最终需支付金额
     */
    private void calExtraAmount() {
        int len = accountList.size();
        extraAmount = actualAmount;
        for (int i = 0; i < len; i++) {
            IAccountIRepayment accountInfo = accountList.get(i);
            if (accountInfo.isUse()) {
                accountInfo.setTotalPayAmount(extraAmount);
                extraAmount = extraAmount.subtract(accountInfo.getValueAmount());
                useAccountList.add(accountInfo);
            }
            if (extraAmount.compareTo(BigDecimal.ZERO) < 1) {
                extraAmount = BigDecimal.ZERO;
                //return;
            }
        }
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

	/**
	 * 还款金额 减去 优惠金额 减去 余额
	 *
	 * @return 还需支付的金额
	 */
	public BigDecimal getExtraAmount() {
        return extraAmount;
    }

    public List<IAccountIRepayment> getAccountList() {
        return accountList;
    }

    public List<IRepaymentModule> getOfferList() {
        return offerList;
    }

    public List<IRepaymentModule> getUseOfferList() {
        return useOfferList;
    }

    public List<IAccountIRepayment> getUseAccountList() {
        return useAccountList;
    }
}
