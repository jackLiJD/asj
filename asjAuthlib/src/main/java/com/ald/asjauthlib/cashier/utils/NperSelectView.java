package com.ald.asjauthlib.cashier.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.cashier.SteadBuyApi;
import com.ald.asjauthlib.cashier.model.SteadBuyItemModel;
import com.ald.asjauthlib.cashier.model.SteadBuyModel;
import com.ald.asjauthlib.cashier.params.OrderPayParams;
import com.ald.asjauthlib.dialog.SteadBuyNperDialog;
import com.ald.asjauthlib.dialog.TipsDialog;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 分期选择视图
 * Created by sean yu on 2017/8/1.
 */
public class NperSelectView implements IPaymentView<OrderPayParams, SteadBuyItemModel> {
    private SteadBuyNperDialog dialog;
    private SteadBuyModel steadModel;
    private IViewResultCallBack<SteadBuyItemModel> callBack;

    private Context context;

    public NperSelectView(Context context) {
        this.context = context;
    }

    @Override
    public void createView(OrderPayParams viewParams) {
        if (steadModel != null) {
            createNperView(steadModel);
        } else {
            requestNperView(viewParams);
        }

    }

    @Override
    public void notifyView(OrderPayParams viewParams) {
        requestNperView(viewParams);
    }

    /**
     * 请求分期视图
     */
    private void requestNperView(OrderPayParams payParams) {
        if (payParams.checkNperParams()) {
            JSONObject object = new JSONObject();
            object.put("numId", payParams.numId);
            object.put("amount", payParams.amount);
            object.put("goodsName", payParams.goodName);

            Call<SteadBuyModel> call = RDClient.getService(SteadBuyApi.class).getAgencyNperInfo(object);
            NetworkUtil.showCutscenes(context, call);
            call.enqueue(new RequestCallBack<SteadBuyModel>() {
                @Override
                public void onSuccess(Call<SteadBuyModel> call, Response<SteadBuyModel> response) {
                    steadModel = response.body();
                    createNperView(steadModel);
                }
            });
        }
    }

    /**
     * 创建分期视图
     */
    private void createNperView(SteadBuyModel model) {
        if (dialog == null) {
            dialog = new SteadBuyNperDialog((Activity) context);
            dialog.show();
            dialog.addNperList(steadModel.getNperList());
            dialog.setSteadBuyModel(steadModel);
            dialog.setListener(new SteadBuyNperDialog.IClickListener() {
                @Override
                public void onSureClick(SteadBuyItemModel steadBuyItemModel) {
                    if (callBack != null) {
                        callBack.onViewResult(steadBuyItemModel);
                    }
                }

                @Override
                public void onTipClick(SteadBuyModel steadBuyModel, View view) {
                    TipsDialog dialog = new TipsDialog(context);
                    dialog.setTitle(steadBuyModel.getCategoryName());
                    String content = String.format(AlaConfig.getResources().getString(R.string.fanbei_pay_category_info),
                            steadBuyModel.getGoodsTotalAmount(), steadBuyModel.getGoodsUseableAmount());
                    dialog.setContent(content);
                    dialog.show();
                }
            });
        } else {
            // dialog.setNperList(steadBuyItemModels);
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
    public void ObserverView(IViewResultCallBack<SteadBuyItemModel> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void UnObserverView(IViewResultCallBack<SteadBuyItemModel> callBack) {
        if (this.callBack != null) {
            this.callBack = null;
        }
    }
}
