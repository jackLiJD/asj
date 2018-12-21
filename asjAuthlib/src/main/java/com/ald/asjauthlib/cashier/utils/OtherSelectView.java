package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.auth.model.BankListModel;
import com.ald.asjauthlib.cashier.LoanApi;
import com.ald.asjauthlib.cashier.model.RefundResponse;
import com.ald.asjauthlib.cashier.params.FinancePayParams;
import com.ald.asjauthlib.cashier.params.LoanPayParams;
import com.ald.asjauthlib.cashier.params.OrderPayParams;
import com.ald.asjauthlib.cashier.params.RenewalParams;
import com.ald.asjauthlib.cashier.params.SettlePayParams;
import com.ald.asjauthlib.cashier.params.StagePayParams;
import com.ald.asjauthlib.dialog.BottomSelectDialog;
import com.ald.asjauthlib.utils.AppUtils;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 其他付款方式
 * Created by sean yu on 2017/7/31.
 */

public class OtherSelectView implements IPaymentView<PaymentParams, BankCardModel> {

    private BottomSelectDialog.Builder builder;
    private BottomSelectDialog dialog;

    private Context context;
    private IViewResultCallBack<BankCardModel> callBack;
    private PaymentParams viewParams;
    private List<BankCardModel> bankCardModels;
    private String realName;
    private String payMoney = "0";
    private List<RefundResponse.RefundType> refundTypeList;

    public OtherSelectView(Context context) {
        this.context = context;
    }


    /**
     * 获取还款方式配置
     * CASH_RENEWAL(现金贷续期）
     * REPAYMENT(分期还款）
     * CASH_REPAYMENT(现金贷还款）
     * LOAN_REPAYMENT(白领贷还款）
     * SETTLE_PAYMENT(账单分期)
     */
    private void refundInit() {
        JSONObject jsonObject = new JSONObject();
        if (viewParams instanceof FinancePayParams) {
            payMoney = ((FinancePayParams) viewParams).repaymentAmount;
            if (viewParams instanceof LoanPayParams) {
                if (Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR == Integer.parseInt(((LoanPayParams) viewParams).repaymentType)) {
                    jsonObject.put("sceneType", "LOAN_REPAYMENT");//白领贷
                } else {
                    jsonObject.put("sceneType", "CASH_REPAYMENT");
                }

            } else if (viewParams instanceof StagePayParams) {
                jsonObject.put("sceneType", "REPAYMENT");
            } else if (viewParams instanceof SettlePayParams) {
                jsonObject.put("sceneType", "REPAYMENT");
            }
            if (viewParams instanceof SettlePayParams) {
                payMoney = ((SettlePayParams) viewParams).getRepayAmount();
            }
        } else if (viewParams instanceof OrderPayParams) {
            jsonObject.put("sceneType", "REPAYMENT");
            payMoney = ((OrderPayParams) viewParams).amount;
        } else if (viewParams instanceof RenewalParams) {
            jsonObject.put("sceneType", "CASH_RENEWAL");
            payMoney = ((RenewalParams) viewParams).amount;
        }

        Call<RefundResponse> call = RDClient.getService(LoanApi.class).getPayTypeStatus(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<RefundResponse>() {
            @Override
            public void onSuccess(Call<RefundResponse> call, Response<RefundResponse> response) {
                refundTypeList = response.body().getPayTypeStatusList();
                requestBankList();
            }
        });
    }

    /**
     * 请求银行卡信息
     */
    private void requestBankList() {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("cardType","4");
        Call<BankListModel> call = RDClient.getService(UserApi.class).getBankCardList(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<BankListModel>() {
            @Override
            public void onSuccess(Call<BankListModel> call, Response<BankListModel> response) {
                bankCardModels = response.body().getBankCardList();
                realName = response.body().getRealName();
                createOtherView(bankCardModels);
            }
        });
    }

    @Override
    public void createView(PaymentParams viewParams) {
        this.viewParams = viewParams;
        if (bankCardModels != null) {
            createOtherView(bankCardModels);
        } else {
            refundInit();
        }
    }

    /**
     * 创建其他支付方式视图
     *
     * @param bankCardModels List<BankCardModel>
     */
    private void createOtherView(List<BankCardModel> bankCardModels) {
        if (builder == null) {
            builder = new BottomSelectDialog.Builder(context);
            builder.setTitle(AlaConfig.getResources().getString(R.string.dialog_pwd_title_other_pay));
            String amount = String.format(AlaConfig.getResources().getString(R.string.dialog_pwd_title_other_pay_amount_format), AppUtils.formatAmount(payMoney));
            builder.setPayAmount(amount);
            if (viewParams instanceof LoanPayParams) {
                if(((LoanPayParams) viewParams).secretFree){
                    builder.setPayFailedTip(((LoanPayParams) viewParams).errMsg);
                }
            }
            if(viewParams instanceof RenewalParams){
                if(((RenewalParams) viewParams).secretFree){
                    builder.setPayFailedTip(((RenewalParams) viewParams).errMsg);
                }
            }
            if(viewParams instanceof SettlePayParams){
                if(((SettlePayParams) viewParams).secretFree){
                    builder.setPayFailedTip(((SettlePayParams) viewParams).errMsg);
                }
            }
            if(viewParams instanceof StagePayParams){
                if(((StagePayParams) viewParams).secretFree){
                    builder.setPayFailedTip(((StagePayParams) viewParams).errMsg);
                }
            }
            builder.setOnSelectedListener(new BottomSelectDialog.OnSelectedListener() {
                @Override
                public void onItemSelected(int position, BankCardModel selectItem) {
                    if (callBack != null) {
                        callBack.onViewResult(selectItem);
                    }
                }
            });
            builder.setRefundType(refundTypeList);
            dialog = builder.setData(bankCardModels).setRealName(realName).create();
        } else {
            builder.setRefundType(refundTypeList);
            builder.setData(bankCardModels);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            dialog = builder.create();
        }
        dialog.show();
    }


    @Override
    public void notifyView(PaymentParams viewParams) {
        requestBankList();//可以不换成refundInit()
    }

    @Override
    public void destroyView() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void ObserverView(IViewResultCallBack<BankCardModel> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void UnObserverView(IViewResultCallBack<BankCardModel> callBack) {
        if (this.callBack != null) {
            this.callBack = null;
        }
    }
}
