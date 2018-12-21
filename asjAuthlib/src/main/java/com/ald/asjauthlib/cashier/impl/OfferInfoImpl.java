package com.ald.asjauthlib.cashier.impl;

import com.ald.asjauthlib.cashier.model.MyTicketModel;
import com.ald.asjauthlib.cashier.utils.IRepaymentModule;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 优惠信息模型
 * Created by sean yu on 2017/4/14.
 */
public class OfferInfoImpl<T extends MyTicketModel> implements IRepaymentModule {
    //账户余额
    private List<T> offerList = new ArrayList<>();
    //
    private List<T> AvailableOfferList = new ArrayList<>();
    //
    private T offerModel;
    //是否可用
    private boolean isUse;

    public void setOfferInfo(List<T> offerList) {
        if (offerList == null) {
            return;
        }
        this.offerList.addAll(offerList);
        sortCouponList(this.offerList);
        calAvailableCoupon(BigDecimal.ZERO);
    }


    /**
     * 对优惠券列表进行排序
     *
     * @param couponList 优惠券列表
     */
    private void sortCouponList(List<T> couponList) {
        Collections.sort(couponList, new Comparator<T>() {
            @Override
            public int compare(T couponListItemModel, T t1) {
                return t1.getAmount().compareTo(couponListItemModel.getAmount());
            }
        });
    }

    public T getOfferModel() {
        return offerModel;
    }

    public void setOfferModel(T offerModel) {
        this.offerModel = offerModel;
    }

    /**
     * 计算可用优惠券
     */
    public T calAvailableCoupon(BigDecimal repaymentMoney) {
        AvailableOfferList.clear();
        this.offerModel = null;
        for (T model : offerList) {
            if (model.getLimitAmount().compareTo(repaymentMoney) <= 0) {
                AvailableOfferList.add(model);
            }
        }
        if (MiscUtils.isNotEmpty(AvailableOfferList)) {
            this.offerModel = AvailableOfferList.get(0);
            return offerModel;
        }
        return null;
    }

    @Override
    public BigDecimal getValueAmount() {
        if (isUse()) {
            return offerModel != null ? offerModel.getAmount() : BigDecimal.ZERO;
        } else {
            return BigDecimal.ZERO;
        }

    }

    public boolean isUse() {
        if (offerModel != null) {
            return true;
        }
        return false;
    }

    public List<T> getAvailableOfferList() {
        return AvailableOfferList;
    }
}
