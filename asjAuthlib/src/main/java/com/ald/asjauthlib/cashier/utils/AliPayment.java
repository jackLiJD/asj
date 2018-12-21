package com.ald.asjauthlib.cashier.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ald.asjauthlib.cashier.PaymentApi;
import com.ald.asjauthlib.cashier.model.OrderStatusModel;
import com.ald.asjauthlib.cashier.model.PaymentModel;
import com.ald.asjauthlib.utils.ModelEnum;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.config.AlaActivityInterceptor;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;

import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 支付宝支付
 * Created by sean yu on 2017/9/2.
 */

public abstract class AliPayment implements IPayment {

    private IPaymentCallBack callBack;
    private PaymentModel model;
    private AlaActivity context;

    private boolean isSubmitPayment = false;
    private String sourceType;

    /**
     * @param context
     * @param sourceType 支付来源(续期、还款等)
     */
    public AliPayment(Context context,String sourceType) {
        if (context instanceof AlaActivity) {
            this.context = (AlaActivity) context;
            this.sourceType=sourceType;
            /**
             * 因为微信支付没有回调 在onResume中重新拉去
             */
            AlaActivityInterceptor interceptor = this.context.getInterceptor();
            interceptor.setListener(new AlaActivityInterceptor.ILeftCycleListener() {
                @Override
                public void onResume() {
                    handleAliPayCallBack();
                }
            });
        }
    }

    /**
     * 主动请求后台判断支付宝是否支付成功
     */
    private void handleAliPayCallBack() {
        if (model != null && isSubmitPayment) {
            isSubmitPayment = false;
            JSONObject object = new JSONObject();
            object.put("orderNo", model.getOrderId());
            Call<OrderStatusModel> call = RDClient.getService(PaymentApi.class).getRepayCashByOrderId(object);
            NetworkUtil.showCutscenes(context, call);
            call.enqueue(new RequestCallBack<OrderStatusModel>() {
                @Override
                public void onSuccess(Call<OrderStatusModel> call, Response<OrderStatusModel> response) {
                    OrderStatusModel result = response.body();
                    if(ModelEnum.SOURCE_TYPE_RENEWAL.getModel().equals(sourceType)) {
                        if(callBack!=null){
                            if (ModelEnum.Y.getModel().equals(result.getStatus())) {
                                callBack.onSuccess(model);
                            } else if (ModelEnum.P.getModel().equals(result.getStatus())) {
                                callBack.onHandle();
                            } else if (ModelEnum.N.getModel().equals(result.getStatus())) {
                                callBack.onCancel(null);
                            }
                        }
                        return;
                    }

                    if (ModelEnum.Y.getModel().equals(result.getStatus()) ||
                            ModelEnum.P.getModel().equals(result.getStatus()) ||
                            ModelEnum.N.getModel().equals(result.getStatus())) {
                        if (callBack != null) {
                            callBack.onSuccess(model);
                        }
                    }

                }

                @Override
                public void onFailure(Call<OrderStatusModel> call, Throwable t) {
                    super.onFailure(call, t);
                    if (callBack != null) {
                        callBack.onCancel(t);
                    }
                }
            });
        }
    }

    @Override
    public void initPayment(PaymentParams params) {
        setPaymentParams(params);
    }


    @Override
    public void ObserverPayInfo(IPaymentCallBack callBack) {
        this.callBack = callBack;
    }

    private void handleAliPay(PaymentModel result) {
        //PaymentObject paymentObject = new PaymentObject(context);
        //
        ////服务器端的信息
        //paymentObject.userNo = result.getUserNo();
        //paymentObject.userType = result.getUserType();
        //
        ////需要修改
        ////paymentObject.wxAppId = responseMap.get("wxAppId");
        ////paymentObject.wxAppId = "2016091901923280";
        //
        //paymentObject.token = result.getToken();
        //paymentObject.merchantNo = result.getMerchantNo();
        //paymentObject.timeStamp = result.getTimeStamp();
        //paymentObject.directPayType = result.getDirectPayType();
        //
        //if (LocationUtils.getCurrentLocation() != null) {
        //    String longitude = LocationUtils.getCurrentLocation().getLongitude() + "";
        //    String latitude = LocationUtils.getCurrentLocation().getLatitude() + "";
        //    //前台信息，需要传给后端
        //    paymentObject.longitude = longitude;
        //    paymentObject.latitude = latitude;
        //}
        //
        //paymentObject.sign = result.getSign();
        //YPCashierPay.createPayment(context, paymentObject, this);

        try {
            final String url = result.getUrlscheme();
            final String urlscheme = "alipays" + "://platformapi/startApp?appId=10000011&url=" + URLEncoder.encode(url, "UTF-8");
            //Intent intent = new Intent();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlscheme));
            context.startActivity(intent);
            //intent.putExtra(HTML5WebView.INTENT_BASE_URL, urlscheme);
            //ActivityUtils.push(HTML5WebView.class, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        isSubmitPayment = true;
    }

    @Override
    public void submit() {
        Call<PaymentModel> call = generateRDClient();
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<PaymentModel>() {
            @Override
            public void onSuccess(Call<PaymentModel> call, Response<PaymentModel> response) {
                //WxOrAlaPayModel model = response.body();
                model = response.body();
                // Log.d("asdasd", response.body().getData().toJSONString());
                handleAliPay(model);
            }

            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {
                super.onFailure(call, t);
                if (callBack != null) {
                    callBack.onCancel(t);
                }
            }
        });
    }



    @Override
    public void UnObserverPayInfo(IPaymentCallBack callBack) {
        if (this.callBack != null) {
            this.callBack = null;
        }
    }


    @Override
    public IPaymentView getPaymentView() {
        return null;
    }


    /**
     * 设置支付参数
     *
     * @param paymentParams 支付参数
     */
    protected abstract void setPaymentParams(PaymentParams paymentParams);

    /**
     * 生成支付请求
     *
     * @return Call<WxOrAlaPayModel>
     */
    protected abstract Call<PaymentModel> generateRDClient();
}
