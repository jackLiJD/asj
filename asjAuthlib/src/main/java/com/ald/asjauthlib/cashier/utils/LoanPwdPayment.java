package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.cashier.LoanApi;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.params.FinancePayParams;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;

import retrofit2.Call;

/**
 * 借钱还款密码支付
 * Created by sean yu on 2017/8/11.
 */
public class LoanPwdPayment extends PwdPayment {
    private FinancePayParams paymentParams;

    public LoanPwdPayment(Context context) {
        super(context);
    }

    @Override
    protected Call<WxOrAlaPayModel> generateRDClient() {
        //小贷
        if (Constant.LOAN_REPAYMENT_TYPE_PETTY == Integer.parseInt(paymentParams.repaymentType)) {
            //借钱合规V2
            if (ModelEnum.CASH_PAGE_TYPE_NEW_V2.getModel().equals(paymentParams.pageType)) {
                return RDClient.getService(LoanApi.class).repayDoV2(paymentParams.getParams());
            }
            //借钱合规V1
            if (ModelEnum.CASH_PAGE_TYPE_NEW_V1.getModel().equals(paymentParams.pageType)) {
                return RDClient.getService(LoanApi.class).repayDo(paymentParams.getParams());
            }
            return RDClient.getService(LoanApi.class).getConfirmRepayInfo(paymentParams.getParams());
            //白领贷
        } else {
            //正常还款
            if (Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR_COMMON.equals(paymentParams.repaymentTypeWhiteCollar)) {
                return RDClient.getService(LoanApi.class).loanRepayDo(paymentParams.getParams());
            }
            //提前结清
            else {
                return RDClient.getService(LoanApi.class).loanAllRepayDo(paymentParams.getParams());
            }
        }
    }

    @Override
    protected PwdInputView createPaymentView(PaymentParams params) {
        this.paymentParams = (FinancePayParams) params;
        this.paymentParams.cardId = "-2";

        PwdInputView inputView = new PwdInputView(getContext());
        inputView.createView("银行卡支付");
        return inputView;
    }

    @Override
    protected IViewResultCallBack<String> generateViewCallBack() {
        return new IViewResultCallBack<String>() {
            @Override
            public void onViewResult(String resultData) {
                paymentParams.payPwd = MD5Util.getMD5Str(resultData);
                submit();
            }
        };
    }
}
