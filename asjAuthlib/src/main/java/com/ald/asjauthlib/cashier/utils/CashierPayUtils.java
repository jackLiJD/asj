package com.ald.asjauthlib.cashier.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.cashier.BankChannel;
import com.ald.asjauthlib.cashier.BrandApi;
import com.ald.asjauthlib.cashier.CashierApi;
import com.ald.asjauthlib.cashier.CashierConstant;
import com.ald.asjauthlib.cashier.model.CashierModel;
import com.ald.asjauthlib.cashier.model.WxOrAlaPayModel;
import com.ald.asjauthlib.cashier.ui.CashierPayFailActivity;
import com.ald.asjauthlib.cashier.ui.CombPayActivity;
import com.ald.asjauthlib.cashier.ui.SelectPaymentActivity;
import com.ald.asjauthlib.dialog.ValidateDialog;
import com.ald.asjauthlib.event.Event;
import com.ald.asjauthlib.event.EventBusUtil;
import com.ald.asjauthlib.event.EventCode;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.utils.Utils;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.location.LocationResult;
import com.ald.asjauthlib.authframework.core.network.BaseObserver;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.RxUtils;
import com.ald.asjauthlib.authframework.core.network.exception.ApiException;
import com.ald.asjauthlib.authframework.core.network.exception.ApiExceptionEnum;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.encryption.MD5Util;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;

import static com.ald.asjauthlib.authframework.core.utils.UIUtils.sendBroadcast;


/*
 * Created by liangchen on 2018/3/6.
 */

public class CashierPayUtils {
    private static final int SDK_PAY_FLAG = 1;
    public static String TAG = CashierPayUtils.class.getSimpleName();

    public static class PayBuilder {

        AlaActivity context;
        long orderId;
        String orderType;
        String nper;
        String payType;
        String payAmount;
        String goodsName;
        CashierModel.BankCardModel bankCardModel;
        String stage;//权限包支付成功关闭页面需要
        OnPayFailedListener onPayFailedListener;
        CashierModel cashierModel;
        boolean isSuccessNeedDetail;

        public PayBuilder setSuccessNeedDetail(boolean successNeedDetail) {
            isSuccessNeedDetail = successNeedDetail;
            return this;
        }

        public PayBuilder setCashierModel(CashierModel cashierModel) {
            this.cashierModel = cashierModel;
            return this;
        }

        public PayBuilder(AlaActivity context) {
            this.context = context;
        }

        public PayBuilder setStage(String stage) {
            this.stage = stage;
            return this;
        }

        public PayBuilder setBankCardModel(CashierModel.BankCardModel bankCardModel) {
            this.bankCardModel = bankCardModel;
            return this;
        }

       /* public PayBuilder setOrderId(long orderId) {
            this.orderId = orderId;
            return this;
        }

        public PayBuilder setOrderType(String orderType) {
            this.orderType = orderType;
            return this;
        }*/

        public PayBuilder setNper(String nper) {
            this.nper = nper;
            return this;
        }

        public PayBuilder setPayType(String payType) {
            this.payType = payType;
            return this;
        }

        public PayBuilder setPayAmount(String payAmount) {
            this.payAmount = payAmount;
            return this;
        }

        public PayBuilder setGoodsName(String goodsName) {
            this.goodsName = goodsName;
            return this;
        }

        public PayBuilder setOnPayFailedListener(OnPayFailedListener onPayFailedListener) {
            this.onPayFailedListener = onPayFailedListener;
            return this;
        }

        /**
         * 输入支付密码后支付
         *
         * @param payId  支付类型(-2:支付宝 -1:微信 0:代付 >0:银行卡id)
         * @param pwd    支付密码
         * @param isComb 是否是组合支付
         */
        public void handlePay(String payId, String pwd,
                              LocationResult result, boolean isComb) {
            double lat = result == null ? 0.0 : result.getLatitude();
            double lng = result == null ? 0.0 : result.getLongitude();
            final JSONObject requestObj = new JSONObject();
            orderId = cashierModel.getOrderId();
            orderType = cashierModel.getOrderType();
            requestObj.put("orderId", String.valueOf(orderId));
            if ("-1".equals(payId) || "-2".equals(payId)) {
                requestObj.put("payId", payId);//支付类型(-1:微信 0:代付 >0银行卡id)
            } else {
                if (bankCardModel != null) {
                    requestObj.put("payId", String.valueOf(bankCardModel.getRid()));
                    if (BankChannel.KUAIJIE.equals(bankCardModel.getBankChannel())) {
                        requestObj.put(BankChannel.bankChannel, BankChannel.KUAIJIE);
                        requestObj.put(BankChannel.mobile, bankCardModel.getMobile());
                    }
                } else {
                    requestObj.put("payId", payId);
                }
            }
            if (!"0".equals(nper)) {
                requestObj.put("nper", String.valueOf(nper));
            }
            if (MiscUtils.isNotEmpty(pwd)) {
                requestObj.put("payPwd", MD5Util.getMD5Str(pwd));
            }
            requestObj.put("type", orderType);
            if (isComb) {
                requestObj.put("isCombinationPay", "Y");
            } else {
                requestObj.put("isCombinationPay", "N");
            }
            if (0.0 != lat && 0.0 != lng) {
                requestObj.put("lat", lat);
                requestObj.put("lng", lng);
            }
            if (null != result) {
                requestObj.put("province", result.getProvince());
                requestObj.put("city", result.getCityName());
                requestObj.put("county", result.getDistrict());
                requestObj.put("address", result.getAddress());
            } else {
                requestObj.put("province", "");
                requestObj.put("city", "");
                requestObj.put("county", "");
                requestObj.put("address", "");
            }
            Log.d(TAG, "cash_start");
            Observable<WxOrAlaPayModel> observable = RDClient.getService(BrandApi.class).payOrderNew(requestObj);
            observable.compose(RxUtils.io_main())
                    .subscribe(new BaseObserver<WxOrAlaPayModel>(context) {
                        @Override
                        public void onSuccess(WxOrAlaPayModel wxOrAlaPayModel) {
                            Log.d(TAG, "cash_finish");
                            if (wxOrAlaPayModel != null) {
                                if (MiscUtils.isEquals("10", wxOrAlaPayModel.getNeedCode())) {
                                    //弹框需要短信
                                    payNex(wxOrAlaPayModel, false);
                                } else if (MiscUtils.isNotEmpty(wxOrAlaPayModel.getOrderInfo())) {
                                    handleAliPay(context, wxOrAlaPayModel);
                                } else {
                                    if (MiscUtils.isNotEmpty(wxOrAlaPayModel.getWxpackage())) {
//                                            handleWeChatPay(wxOrAlaPayModel);
                                    } else {
                                        payNex(wxOrAlaPayModel, true);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(ApiException apiException) {
                            super.onFailure(apiException);
                            int code = apiException.getCode();
                            if (code != ApiExceptionEnum.USER_PWD_INPUT_ERROR.getErrorCode()
                                    && code != ApiExceptionEnum.USER_PWD_FORBID.getErrorCode()) {
                                if (onPayFailedListener != null) {
                                    onPayFailedListener.onPayFailed(apiException.getMsg());
                                }
                            }
                            String modelJsonStr = apiException.getDataJsonStr();
                            if (MiscUtils.isEmpty(modelJsonStr))
                                return;
                            JSONObject wxOrAlaPayModel = JSON.parseObject(modelJsonStr);
                            String goodsType = wxOrAlaPayModel.getString("goodsType");
                            if (MiscUtils.isEqualsIgnoreCase(goodsType, "COMMON")) {
//                                closeCashierAndOrder(context);
//                              打开订单详情页面待返回  (在失败页面点重新支付直接到收银台)
                                openOrderDtl();
//                              跳转失败页面
                                Intent intent = new Intent();
                                intent.putExtra(BundleKeys.CASHIER_FAIL_PARAMS_JSON, modelJsonStr);
                                intent.putExtra(BundleKeys.STAGE_JUMP, stage);
                                ActivityUtils.push(CashierPayFailActivity.class, intent);

                            } else {
                                if (code != ApiExceptionEnum.USER_PWD_INPUT_ERROR.getErrorCode()
                                        && code != ApiExceptionEnum.USER_PWD_FORBID.getErrorCode()) {
                                    if (onPayFailedListener != null) {
                                        onPayFailedListener.onPayFailed(apiException.getMsg());
                                    }
                                }
                            }
                        }
                    });
        }


        /**
         * @param isKuaijie 是否快捷支付
         */
        private void payNex(WxOrAlaPayModel wxOrAlaPayModel, boolean isKuaijie) {
            if (isKuaijie) {
                if (bankCardModel != null && wxOrAlaPayModel != null && (BankChannel.KUAIJIE.equals(bankCardModel.getBankChannel())
                        || BankChannel.XIEYI.equals(bankCardModel.getBankChannel()))) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("bankPayChannel", bankCardModel.getBankChannel());
                    jsonObject.put("tradeNo", wxOrAlaPayModel.getOrderNo());
                    new ValidateDialog.Builder(context)
                            .setPhone(bankCardModel.getMobile())
                            .setJsonObject(jsonObject)
                            .setOnFinishListener(apiResponse -> goToPaySuccess(apiResponse))
                            .create()
                            .show();
                } else {
                    goToPaySuccess(wxOrAlaPayModel);
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("bankPayChannel", "XIEYI_CONFIRM");
                jsonObject.put("tradeNo", (wxOrAlaPayModel == null) ? "" : wxOrAlaPayModel.getOutTradeNo());
                new ValidateDialog.Builder(context)
                        .setPhone(bankCardModel.getMobile())
                        .setJsonObject(jsonObject)
                        .setOnFinishListener(apiResponse -> goToPaySuccess(apiResponse))
                        .create()
                        .show();

            }
        }

        private void goToPaySuccess(WxOrAlaPayModel wxOrAlaPayModel) {


            if (MiscUtils.isEqualsIgnoreCase(wxOrAlaPayModel.getGoodsType(), "auth")) {
                //权限包购买成功后,权限包不会出现组合支付页面
                if (MiscUtils.isEqualsIgnoreCase(stage, StageJumpEnum.STAGE_ORDER_LIST.getModel())) {
                    //关闭至订单列表 注意订单详情页
                    ActivityUtils.pop();
                }
            } else {
                if (CashierConstant.CASHIER_PAY_TYPE_NORMAL.equals(payType)) {
                    if (Constant.ORDER_FROM_SUREORDER.equals(cashierModel.getOrderFrom())) {
                        //关闭确认订单h5
                        ActivityUtils.finish(HTML5WebView.class);
                    } else if (Constant.ORDER_FROM_ORDERLIST.equals(cashierModel.getOrderFrom())) {
                        //刷新订单列表h5
                        Intent intent = new Intent(HTML5WebView.ACTION_REFRESH);
                        sendBroadcast(intent);
                    }
                    //跳转支付成功
                    Utils.jumpH5(Utils.genH5Url(Constant.H5_PAY_SUCCESS, orderId));
                    ActivityUtils.finish(CombPayActivity.class);
                    ActivityUtils.finish(SelectPaymentActivity.class);
                } else {
                    handlePaySuccessSelf();
                    UIUtils.showToast("支付成功！");
                }
            }
        }

        /**
         * 处理自营支付成功跳转
         */
        private void handlePaySuccessSelf() {
            //关闭收银台打开支付完成页（H5）
            closeCashierAndOrder(context);
            //如果是来自订单列表或者详情的,只需要关闭详情页,前面已刷新
            if (MiscUtils.isEquals(stage, StageJumpEnum.STAGE_ORDER_LIST.getModel())) {
                EventBusUtil.sendEvent(new Event(EventCode.CLOSE_ORDER_DETAIL));
            }
            if (CashierConstant.ORDER_TYPE_GROUP.equals(cashierModel.getOrderTypeExt())) {
                //我的订单支付不需要拼团详情H5,拼团详情支付的也不需要
                if (!MiscUtils.isEquals(stage, StageJumpEnum.STAGE_ORDER_LIST.getModel()) && isSuccessNeedDetail)
                    Utils.jumpH5(String.format(AlaConfig.getServerProvider().getH5Server() + Constant.H5_PINTUAN_DETAIL, orderId));
                Utils.jumpH5(String.format(AlaConfig.getServerProvider().getH5Server() + Constant.H5_PINTUAN_SUCCESS, orderId));
            } else {
                //打开订单详情页,待跳转
                openOrderDtl();
                //打开支付结果
                Intent intent = new Intent();
                String url = Utils.genH5Url(Constant.H5_PAY_SUCCESS, orderId);
                intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                intent.putExtra(HTML5WebView.INTENT_TITLE, "  ");
                ActivityUtils.push(HTML5WebView.class, intent);
            }
        }


        /**
         * 关闭收银台和确认订单页面
         */
        private void closeCashierAndOrder(Context context) {
            if (context instanceof CombPayActivity) {
                ActivityUtils.pop();
            }
            ActivityUtils.pop();//SelectPaymentActivity.class;
//            ActivityUtils.finish(MallOrderActivity.class);
        }

        /**
         * 打开订单列表
         */
        private void openOrderDtl() {
            UIUtils.showToast("打开订单列表");
//            if (!MiscUtils.isEquals(stage, StageJumpEnum.STAGE_ORDER_LIST.getModel())) {
//                ActivityUtils.push(OrderListActivity.class);
//            } else {
//                EventBusUtil.sendEvent(new com.alfl.www.main.event.Event(EventCode.CLOSE_ORDER_DETAIL));
//            }

//            Intent intent = new Intent();
//            intent.putExtra("orderId", String.valueOf(orderId));
//            intent.putExtra(BundleKeys.RETURN_GOODS_TYPE, orderType);
//            intent.putExtra(BundleKeys.BUBBLE_OFFICIAL, 1);
//            ActivityUtils.push(ActivityUtils.peek(), OrderDetailActivity.class, intent, BundleKeys.REQUEST_ORDER_DETAILS);
        }

        /**
         * 处理支付宝支付
         *
         * @param context         context
         * @param wxOrAlaPayModel wxOrAlaPayModel
         */
        private void handleAliPay(Context context, WxOrAlaPayModel wxOrAlaPayModel) {

            @SuppressLint("HandlerLeak") Handler mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case SDK_PAY_FLAG:
                            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                            String resultInfo = payResult.getResult();//同步返回需要验证的信息
                            String resultStatus = payResult.getResultStatus();
                            if (TextUtils.equals(resultStatus, "9000")) {
                                //该笔订单是否支付成功，需要一栏服务端的异步通知
                                UIUtils.showToast("支付成功");
                                goToPaySuccess(wxOrAlaPayModel);
                            } else {
                                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                                UIUtils.showToast("支付失败");
                                JSONObject reqObj = new JSONObject();
                                reqObj.put("orderNo", String.valueOf(orderId));
                                Call<WxOrAlaPayModel> call = RDClient.getService(CashierApi.class).alipayQueryOrder(reqObj);
                                call.enqueue(new RequestCallBack<WxOrAlaPayModel>() {
                                    @Override
                                    public void onSuccess(Call<WxOrAlaPayModel> call, Response<WxOrAlaPayModel> response) {

                                    }
                                });
                            }
                            break;
                        default:
                            break;
                    }
                    super.handleMessage(msg);
                }
            };
            final String orderInfo = wxOrAlaPayModel.getOrderInfo();
            Runnable payRunnable = () -> {
                PayTask aliPay = new PayTask((Activity) context);
                Map<String, String> result = aliPay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            };
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }

        /**
         * 支付失败弹窗
         */
        public interface OnPayFailedListener {
            void onPayFailed(String msg);
        }
    }


}
