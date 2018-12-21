package com.ald.asjauthlib.cashier.order;

import android.content.Context;

import com.ald.asjauthlib.cashier.utils.AliPayment;
import com.ald.asjauthlib.cashier.utils.BankPayment;
import com.ald.asjauthlib.cashier.utils.LoanOutLinePayment;
import com.ald.asjauthlib.cashier.utils.OtherPayment;
import com.ald.asjauthlib.cashier.utils.OutLinePayment;
import com.ald.asjauthlib.cashier.utils.WxPayment;


/**
 * 订单模块其他支付方式
 * Created by sean yu on 2017/8/11.
 */
public class OrderOtherPayment extends OtherPayment {
    public OrderOtherPayment(Context context) {
        super(context);
    }

    @Override
    protected BankPayment generateBankPayment(Context context) {
        return new OrderBankPayment(context);
    }

    @Override
    protected WxPayment generateWxPayment(Context context) {
        return new OrderWxPayment(context);
    }

    @Override
    protected AliPayment generateAliPayment(Context context) {
        return new OrderAliPayment(context);
    }

    @Override
    protected OutLinePayment generateOutlinePayment(Context context) {
        return new LoanOutLinePayment(context);
    }
}
