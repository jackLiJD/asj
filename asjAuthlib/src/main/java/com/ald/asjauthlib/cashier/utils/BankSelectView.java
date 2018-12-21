package com.ald.asjauthlib.cashier.utils;

import android.content.Context;

import com.ald.asjauthlib.auth.UserApi;
import com.ald.asjauthlib.auth.model.BankCardModel;
import com.ald.asjauthlib.auth.model.BankListModel;
import com.ald.asjauthlib.dialog.PayBankSelectDialog;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 银行卡选择视图
 * Created by sean yu on 2017/8/1.
 */

public class BankSelectView implements IPaymentView<BankCardModel, BankCardModel> {
    private final String BANK_SELECT_TITLE = "选择银行卡";
    private Context context;
    private IViewResultCallBack<BankCardModel> callBack;
    private List<BankCardModel> viewParams;
    private BankCardModel selectModel;

    private PayBankSelectDialog.Builder selectBuilder;
    private PayBankSelectDialog dialog;


    public BankSelectView(Context context) {
        this.context = context;
    }

    /**
     * 请求银行卡信息
     */
    private void requestBankList() {
        JSONObject jsonObject=new JSONObject();
        //jsonObject.put("cardType","3");
        Call<BankListModel> call = RDClient.getService(UserApi.class).getBankCardList(jsonObject);
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<BankListModel>() {
            @Override
            public void onSuccess(Call<BankListModel> call, Response<BankListModel> response) {
                viewParams = response.body().getBankCardList();
                createBankView(viewParams);
            }
        });
    }

    @Override
    public void createView(BankCardModel viewParams) {
        this.selectModel = viewParams;
        if (this.viewParams != null) {
            createBankView(this.viewParams);
        } else {
            requestBankList();
        }
    }

    @Override
    public void notifyView(BankCardModel viewParams) {
        requestBankList();
    }

    /**
     * 创建其他支付方式视图
     *
     * @param viewParams List<BankCardModel>
     */
    private void createBankView(List<BankCardModel> viewParams) {
        if (selectBuilder == null) {
            selectBuilder = new PayBankSelectDialog.Builder(context);
            selectBuilder.setData(viewParams);
            selectBuilder.setDefaultBankCardModel(selectModel);
            //  selectBuilder.setAnimResId(R.style.Animation_LeftRight);
            selectBuilder.setTitle(BANK_SELECT_TITLE);
            selectBuilder.setOnSelectedListener(new PayBankSelectDialog.OnSelectedListener() {
                @Override
                public void onItemSelected(int position, BankCardModel selectItem) {
                    if (callBack != null) {
                        callBack.onViewResult(selectItem);
                    }
                }
            });
            selectBuilder.setData(viewParams);
            dialog = selectBuilder.create();
            dialog.show();
        } else {
            selectBuilder.setData(viewParams);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            dialog = selectBuilder.create();
            dialog.show();
        }
    }


    @Override
    public void destroyView() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
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
