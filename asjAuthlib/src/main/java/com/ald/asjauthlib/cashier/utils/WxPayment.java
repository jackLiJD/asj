package com.ald.asjauthlib.cashier.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.web.HTML5WebView;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.NetworkUtil;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 微信支付
 * Created by sean yu on 2017/7/31.
 */

public abstract class WxPayment implements IPayment {

    private static final String BROAD_CAST_WX_SUCCESS = "__broad_cast_wx_success";
    private static final String BROAD_CAST_WX_CANCEL = "__broad_cast_wx_cancel";

//    private IWXAPI api;
    private IPaymentCallBack callBack;
    private WxOrAlaPayModel model;
    private Context context;
    /**
     * 微信支付广播接收器
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BROAD_CAST_WX_SUCCESS.equals(action)) {
                if (callBack != null) {
                    callBack.onSuccess(model);
                }
            } else if (BROAD_CAST_WX_CANCEL.equals(action)) {
                if (callBack != null) {
                    callBack.onCancel(null);
                }
            }
            AlaConfig.getLocalBroadcastManager().unregisterReceiver(receiver);
        }
    };

    /**
     * @param context
     */
    public WxPayment(Context context) {
        this.context = context;
//        api = WXAPIFactory.createWXAPI(context, Constant.WX_KEY);
//        api.registerApp(Constant.WX_KEY);
    }

    @Override
    public void initPayment(PaymentParams params) {
        setPaymentParams(params);
    }


    @Override
    public void ObserverPayInfo(IPaymentCallBack callBack) {
        this.callBack = callBack;
        //registerReceiver();
    }

    /**
     * 注册微信广播
     */
    //public void registerReceiver() {
    //    /**
    //     * 防止广播被多次注册
    //     */
    //    //AlaConfig.getLocalBroadcastManager().unregisterReceiver(receiver);
    //    IntentFilter intentFilter = new IntentFilter();
    //    intentFilter.addAction(BROAD_CAST_WX_SUCCESS);
    //    intentFilter.addAction(BROAD_CAST_WX_CANCEL);
    //    //context.registerReceiver(receiver, intentFilter);
    //    AlaConfig.getLocalBroadcastManager().registerReceiver(receiver, intentFilter);
    //}

    /**
     * 销毁监听
     */
    //public void unregisterReceiver() {
    //    AlaConfig.getLocalBroadcastManager().unregisterReceiver(receiver);
    //    receiver = null;
    //}

    /**
     * 微信支付
     */
    private void handleWeChatPay(WxOrAlaPayModel result) {
        //PayReq req = new PayReq();
        //req.appId = result.getAppid();
        //req.partnerId = result.getPartnerid();
        //req.prepayId = result.getPrepayid();
        //req.nonceStr = result.getNoncestr();
        //req.timeStamp = result.getTimestamp();
        //req.packageValue = result.getWxpackage();
        //req.sign = result.getSign();
        //api.sendReq(req);

		//Map<String, String> map = new HashMap<>();
		//map.put("state", result.getToken());
		//map.put("code", result.getCode());
		//Call<String> call0 = RDClient.getService(LoanApi.class).redirectUri(result.getGetopenusrl(), map);
		//call0.enqueue(new RequestCallBack<String>() {
		//	@Override
		//	public void onSuccess(Call<String> call, Response<String> response) {
		//		Logger.i("yf", "string == " + response.body().toString());
		//	}
		//});

		Intent intent = new Intent();
		intent.putExtra(HTML5WebView.INTENT_BASE_URL, result.getUrlscheme());
		ActivityUtils.push(HTML5WebView.class, intent);
    }

    @Override
    public void submit() {
        Call<WxOrAlaPayModel> call = generateRDClient();
        NetworkUtil.showCutscenes(context, call);
        call.enqueue(new RequestCallBack<WxOrAlaPayModel>() {
            @Override
            public void onSuccess(Call<WxOrAlaPayModel> call, Response<WxOrAlaPayModel> response) {
                model = response.body();
                handleWeChatPay(model);
            }

            @Override
            public void onFailure(Call<WxOrAlaPayModel> call, Throwable t) {
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
        //unregisterReceiver();
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
    protected abstract Call<WxOrAlaPayModel> generateRDClient();
}
