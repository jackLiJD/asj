package com.ald.asjauthlib.web;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import com.ald.asjauthlib.AuthConfig;
import com.ald.asjauthlib.utils.BundleKeys;
import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.cashier.CashierApi;
import com.ald.asjauthlib.cashier.CashierConstant;
import com.ald.asjauthlib.cashier.model.CashierSubmitResponseModel;
import com.ald.asjauthlib.cashier.ui.MyTicketActivity;
import com.ald.asjauthlib.cashier.ui.RepaymentActivity;
import com.ald.asjauthlib.cashier.ui.SelectPaymentActivity;
import com.ald.asjauthlib.cashier.ui.StageRefundActivity;
import com.ald.asjauthlib.utils.ModelEnum;
import com.ald.asjauthlib.utils.StageJumpEnum;
import com.ald.asjauthlib.utils.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bqs.risk.df.android.BqsDF;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.network.BaseObserver;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RxUtils;
import com.ald.asjauthlib.authframework.core.protocol.AlaProtocol;
import com.ald.asjauthlib.authframework.core.protocol.ProtocolUtils;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.BASE64Encoder;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.moxie.client.MainActivity;

import cn.tongdun.android.shell.FMAgent;

import static com.ald.asjauthlib.authframework.core.utils.UIUtils.showToast;

/**
 * 自定义接收器
 */
public class NativeReceiver extends BroadcastReceiver {
    public static final IntentFilter INTENT_FILTER_OPEN = new IntentFilter(ProtocolUtils.NATIVE_ACTION);
    private static final String TAG = NativeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction == null || !intentAction.equals(ProtocolUtils.NATIVE_ACTION)) {
            Logger.d(TAG, "收到action不一致的广播");
            return;
        }
        String name = intent.getStringExtra(ProtocolUtils.NATIVE_NAME);
        final String param = intent.getStringExtra(ProtocolUtils.NATIVE_PARAM);
        final String config = intent.getStringExtra(ProtocolUtils.NATIVE_CONFIG);
        String query = intent.getStringExtra(ProtocolUtils.NATIVE_URL_QUERY);
        JSONObject parseObject = JSONObject.parseObject(param);
        String scene = "";
        if (parseObject != null) {
            scene = parseObject.getString("scene");
        }
        if (!AlaConfig.isLand() && MiscUtils.isNotEmpty(config)) {
            JSONObject configJsonObject = JSONObject.parseObject(config);
            String configType = configJsonObject.getString("configType");
            if ("login".equals(configType)) {
                Utils.jumpToLoginNoResult();
                return;
            }
        }
        //登录
        if (AlaProtocol.NATIVE_LOGIN_FOR_WEB.equals(name)) {
            AlaConfig.setLand(false);
            Utils.jumpToLoginNoResult();
        }  else if ("BRAND_ORDER_CONFIRM".equals(name)) {
            //菠萝觅订单支付详情
            Intent brandIntent = new Intent();
            JSONObject jsonObject = JSONObject.parseObject(param);
            if (jsonObject != null) {
                String orderId = jsonObject.getString("orderId");
                String platform = jsonObject.getString("plantform");

                brandIntent.putExtra("orderId", orderId);
                brandIntent.putExtra("orderType", "BOLUOME");
                brandIntent.putExtra(BundleKeys.BRAND_PLATFORM, platform);
                brandIntent.putExtra("isBrandOrder", true);
                brandIntent.putExtra(CashierConstant.CASHIER_PAY_TYPE, CashierConstant.CASHIER_PAY_TYPE_NORMAL);
                brandIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE);
                ActivityUtils.push(SelectPaymentActivity.class, brandIntent);
            }
        }  else if ("JUMP_BOLUOMI_PAGE".equals(name)) {    // 跳转逛逛,已无
            Intent i = new Intent();
            i.putExtra(BundleKeys.MAIN_DATA_TAB, 1);
            ActivityUtils.push(MainActivity.class, i, BundleKeys.REQUEST_CODE_BRAND);
            ActivityUtils.pop();
        } else if ("MINE_COUPON_LIST".equals(name)) {
            ActivityUtils.push(MyTicketActivity.class);
        } else if ("APP_HOME".equals(name)) {
            //首页
            AuthConfig.goHomeAction();
        }
        //商圈支付
        else if ("APP_TRADE_PAY".equals(name)) {
            final JSONObject jsonObject = JSONObject.parseObject(param);
            if (jsonObject != null) {
                String amount = jsonObject.getString("tradeAmount");
                String tradeId = jsonObject.getString("tradeId");
                String tradeName = jsonObject.getString("tradeName");

                JSONObject reqObj = new JSONObject();
                reqObj.put("businessName", tradeName);
                reqObj.put("businessId", tradeId);
                reqObj.put("actualAmount", amount);
                reqObj.put("orderType", "TRADE");
                //同盾风控
                String blackBox = FMAgent.onEvent(context);
                reqObj.put("blackBox", blackBox);
                //白骑士设备指纹
                String bqsBlackBox = BqsDF.getTokenKey();
                jsonObject.put("bqsBlackBox", bqsBlackBox);
                getPayInfo(reqObj);
            }
        }
        //租赁或者H5跳收银台
        else if ("APP_RENT".equals(name)) {
            final JSONObject rentJSONObject = JSONObject.parseObject(param);
            if (rentJSONObject != null) {
                String orderId = rentJSONObject.getString("orderId");
                if (MiscUtils.isEmpty(orderId)) {
                    JSONObject reqObj = new JSONObject();
                    reqObj.put("goodsId", rentJSONObject.getString("goodsId"));//商品Id
                    reqObj.put("goodsPriceId", rentJSONObject.getString("goodsPriceId"));//商品价格Id
                    reqObj.put("addressId", rentJSONObject.getString("addressId"));
                    reqObj.put("nper", rentJSONObject.getString("nper"));
                    reqObj.put("lc", rentJSONObject.getString("lc"));//订单来源
                    reqObj.put("orderType", CashierConstant.ORDER_TYPE_RENT);//orderType=LEASE
                    //同盾风控
                    String blackBox = FMAgent.onEvent(context);
                    reqObj.put("blackBox", blackBox);
                    //白骑士设备指纹
                    String bqsBlackBox = BqsDF.getTokenKey();
                    rentJSONObject.put("bqsBlackBox", bqsBlackBox);

                    getPayInfo(reqObj);
                } else {
                    intent = new Intent();
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("orderType", rentJSONObject.getString("orderType"));
                    String orderFrom = rentJSONObject.getString("orderFrom");//0表示来自确认订单,1表示订单列表
                    intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE,  BundleKeys.CREDIT_PROMOTE_SCENE_RENT );
                    intent.putExtra(CashierConstant.CASHIER_PAY_TYPE, CashierConstant.CASHIER_PAY_TYPE_NORMAL);
                    intent.putExtra("orderFrom",orderFrom);
                    ActivityUtils.push(SelectPaymentActivity.class, intent);
                }
            }
        }
        //商圈去提升额度
        else if ("APP_TRADE_PROMOTE".equals(name)) {
            final JSONObject jsonObject = JSONObject.parseObject(param);
            if (jsonObject != null) {
                String action = jsonObject.getString("action");
                if (MiscUtils.isEmpty(scene))
                    scene = BundleKeys.CREDIT_PROMOTE_SCENE_TRAIN;
                if ("DO_FACE".equals(action)) {
                    Intent i = new Intent();
                    i.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                    i.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_TRADE_SCAN.getModel());
                    ActivityUtils.push(RRIdAuthActivity.class);
                } else if ("DO_BIND_CARD".equals(action)) {
                    Intent actionIntent = new Intent();
                    String idNumber = jsonObject.getString("idNumber");
                    String realName = jsonObject.getString("realName");
                    actionIntent.putExtra(BundleKeys.BANK_CARD_NAME, realName);
                    actionIntent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, BASE64Encoder.decodeString(idNumber));
                    actionIntent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_TRADE_SCAN.getModel());
                    actionIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                    ActivityUtils.push(BankCardAddIdActivity.class, actionIntent);
                } else if ("DO_PROMOTE_BASIC".equals(action)) {
                    Intent actionIntent = new Intent();
                    actionIntent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_TRADE_SCAN.getModel());
                    actionIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                    ActivityUtils.push(CreditPromoteActivity.class, actionIntent);
                } else if ("DO_PROMOTE_EXTRA".equals(action)) {
                    Intent i = new Intent();
                    i.putExtra(BundleKeys.JUMP_AUTH_CODE, 1);
                    i.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_TRADE_SCAN.getModel());
                    i.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                    ActivityUtils.push(CreditPromoteActivity.class, i);
                } else {
                    Intent actionIntent = new Intent();
                    actionIntent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_TRADE_SCAN.getModel());
                    actionIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                    ActivityUtils.push(CreditPromoteActivity.class, actionIntent);
                }
            } else {
                Intent actionIntent = new Intent();
                actionIntent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_TRADE_SCAN.getModel());
                actionIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, scene);
                ActivityUtils.push(CreditPromoteActivity.class, actionIntent);
            }
        }

        //立即借钱按钮跳转
        else if ("DO_SCAN_ID".equals(name)) {
            Intent scanIdIntent = new Intent();
            scanIdIntent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
            scanIdIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, MiscUtils.isEmpty(scene) ? BundleKeys.CREDIT_PROMOTE_SCENE_CASH : scene);
            ActivityUtils.push(RRIdAuthActivity.class, scanIdIntent);
        } else if ("DO_FACE".equals(name)) {
            Intent scanFaceIntent = new Intent();
            scanFaceIntent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
            scanFaceIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, MiscUtils.isEmpty(scene) ? BundleKeys.CREDIT_PROMOTE_SCENE_CASH : scene);
            ActivityUtils.push(BankCardAddIdActivity.class, scanFaceIntent);
        } else if ("DO_BIND_CARD".equals(name)) {
            JSONObject jsonObject = JSON.parseObject(param);
            Intent bindIntent = new Intent();
            String idNumber = jsonObject.getString("idNumber");
            String realName = jsonObject.getString("realName");
            String backSpace = jsonObject.getString("backSpace");
            bindIntent.putExtra(BundleKeys.STAGE_JUMP, (MiscUtils.isNotEmpty(backSpace) && backSpace.equals("0")) ?
                    StageJumpEnum.STAGE_H5_BANK_CARD.getModel() : StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
            bindIntent.putExtra(BundleKeys.BANK_CARD_NAME, realName);
            if (idNumber != null && idNumber.length() % 4 == 0) {
                idNumber = BASE64Encoder.decodeString(idNumber);
            }
            bindIntent.putExtra(BundleKeys.SETTING_PAY_ID_NUMBER, idNumber);
            bindIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, MiscUtils.isEmpty(scene) ? BundleKeys.CREDIT_PROMOTE_SCENE_CASH : scene);
            ActivityUtils.push(BankCardAddIdActivity.class, bindIntent);
        } else if ("DO_PROMOTE_BASIC".equals(name)) {
            Intent basicIntent = new Intent();
            basicIntent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
            basicIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, MiscUtils.isEmpty(scene) ? BundleKeys.CREDIT_PROMOTE_SCENE_CASH : scene);
            ActivityUtils.push(CreditPromoteActivity.class, basicIntent);
        } else if ("DO_PROMOTE_EXTRA".equals(name)) {
            Intent extraIntent = new Intent();
            extraIntent.putExtra(BundleKeys.JUMP_AUTH_CODE, 1);
            extraIntent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_ORAL_ACTIVITY.getModel());
            extraIntent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, MiscUtils.isEmpty(scene) ? BundleKeys.CREDIT_PROMOTE_SCENE_CASH : scene);
            ActivityUtils.push(CreditPromoteActivity.class, extraIntent);
        }
        else if ("APP_CLOSE_H5".equals(name)) {
            ActivityUtils.pop();
        } else if ("APP_BACK_ROOT".equals(name)) {
            ActivityUtils.pop();
        }
        //去还款
        else if ("APP_TOPAY".equals(name)) {
            ActivityUtils.push(RepaymentActivity.class);
            ActivityUtils.pop();
        }
        //去分期,信用中心页
        else if ("APP_MOVE".equals(name)) {
//            ActivityUtils.push(CreditCenterActivity.class);
        } else if ("JUMP_SEARCHPAGE".equals(name)) { // 商品搜索页面
//            ActivityUtils.push(GoodsSearchActivity.class);
        } else if (AlaProtocol.NATIVE_H5_ACTIVITY.equals(name.split("_")[0])) { // H5配置页面绝对路径 注: 如果是com.alfl.www.main.ui.MainActivity则后缀拼接跳转哪个fragment
            if (AlaConfig.isLand()) {
                String[] actName = name.split("_");
                try {
                    if (MainActivity.class.getName().equals(actName[1])) {
                        int mainTab = Integer.valueOf(actName[2]);
                        Intent i = new Intent();
                        i.putExtra(BundleKeys.MAIN_DATA_TAB, mainTab);
                        ActivityUtils.push(MainActivity.class, i);
                    } else {
                        Class clazz = Class.forName(actName[1]);
                        if (actName[1].contains("QRCodeScanActivity")) {
//                            Intent i = new Intent(ActivityUtils.peek(), QRCodeScanActivity.class);
//                            ActivityUtils.push(QRCodeScanActivity.class, i, BundleKeys.REQUEST_CODE_MIAN_SCAN);
                        } else {
                            JSONObject jsonObject = JSONObject.parseObject(param);
                            if (jsonObject==null||jsonObject.isEmpty())
                                ActivityUtils.push(clazz);
                            else {
                                Intent intent1 = new Intent();
                                for (String s : jsonObject.keySet()) {
                                    Object obj = jsonObject.get(s);
                                    // 只支持boolean, int, long, String类型
                                    if (obj instanceof Boolean)
                                        intent1.putExtra(s, (boolean) obj);
                                    else if (obj instanceof Integer)
                                        intent1.putExtra(s, (int) obj);
                                    else if (obj instanceof Long)
                                        intent1.putExtra(s, (long) obj);
                                    else if (obj instanceof String)
                                        intent1.putExtra(s, (String) obj);
                                }
                                ActivityUtils.push(clazz, intent1);
                        }
                        }

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.jumpToLoginNoResult();
            }
        } else if ("BLD_REPAYMENT_INFO".equals(name)) {
            //白领贷还款
            Intent i = new Intent();
            JSONObject jsonObject = JSONObject.parseObject(param);
            if (jsonObject != null) {
                String borrowId = jsonObject.getString("borrowId");
                String loanPeriodsId = jsonObject.getString("loanPeriodsId");
                String whiteCollarRepaymentType = jsonObject.getString("repaymentTypeWhiteCollar");
                String repaymentAmount = jsonObject.getString("repaymentAmount");
                String periodsUnChargeAmount = jsonObject.getString("periodsUnChargeAmount");//提前结清的金额
                String rebateAmount = jsonObject.getString("rebateAmount");
                String repaymentType = jsonObject.getString("repayment_type");
                i.putExtra(BundleKeys.BORROW_ID, borrowId);
                i.putExtra(BundleKeys.LOAN_PERIODS_ID, loanPeriodsId);
                //i.putExtra(BundleKeys.REPAYMENT_TYPE, Constant.LOAN_REPAYMENT_TYPE_WHITE_COLLAR);
                if (repaymentType == null)
                    i.putExtra(BundleKeys.REPAYMENT_TYPE, Constant.LOAN_REPAYMENT_TYPE_PETTY);
                else
                    i.putExtra(BundleKeys.REPAYMENT_TYPE, Integer.valueOf(repaymentType));
                i.putExtra(BundleKeys.REPAYMENT_TYPE_WHITE_COLLAR, whiteCollarRepaymentType);
                i.putExtra(BundleKeys.MAX_REPAYMENT_AMOUNT, repaymentAmount);
                i.putExtra(BundleKeys.REPAYMENT_AMOUNT_BLD_ADVANCE, periodsUnChargeAmount);
                i.putExtra(BundleKeys.REBATE_AMOUNT, rebateAmount);
//                i.putExtra(BundleKeys.JIFENG_COUNT, model.getJfbAmount());
                i.putExtra(BundleKeys.CASH_LOAN_REPAYMENT_FROM_PAGE, ModelEnum.CASH_LOAN_REPAYMENT_FROM_PAGE_INDEX.getModel());
                ActivityUtils.push(StageRefundActivity.class, i, BundleKeys.REQUEST_CODE_LOAN);
            }
        } else if ("openAlipay".equals(name)) {
            try {
                PackageManager packageManager = AlaConfig.getContext().getPackageManager();
                Intent i = packageManager.getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                Activity act = ActivityUtils.peek();
                if (null != act) act.startActivity(i);
            } catch (Exception e) {
                showToast("请先下载支付宝app");
            }
        } else if ("BRAND".equals(name)) {         //品牌H5跳品牌tab
            ActivityUtils.pop();
          /*  Intent i = new Intent();
            i.putExtra(BundleKeys.MAIN_DATA_TAB, 1);
            ActivityUtils.push(MainActivity.class, i);
            EventBusUtil.sendEvent(new Event(EventCode.SORT_TO_BRAND));*/
        } else if ("OPEN_NOTIFICATION".equals(name)) {
            UIUtils.openNoticeStatus();
        }
    }

    /**
     * 收银台下单
     */
    private void getPayInfo(final JSONObject object) {
        RDClient.getService(CashierApi.class).pre(object).compose(RxUtils.io_main())
                .subscribe(new BaseObserver<CashierSubmitResponseModel>() {
                    @Override
                    public void onSuccess(CashierSubmitResponseModel cashierSubmitResponseModel) {
                        String orderId = cashierSubmitResponseModel.getOrderId();
                        String orderType = object.getString("orderType");
                        if (MiscUtils.isEmpty(orderType)) {
                            orderType = "TRADE";
                        }
                        Intent intent = new Intent();
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("orderType", orderType);
                        intent.putExtra(CashierConstant.CASHIER_PAY_TYPE, CashierConstant.CASHIER_PAY_TYPE_NORMAL);
                        ActivityUtils.push(SelectPaymentActivity.class, intent);
                    }
                });
    }


    /**
     * 提交分享成功信息
     */
    private void submitShareInfo(String sharePage) {
        requestShareSuccess(generateParams(sharePage));
    }


    private JSONObject generateParams(String sharePage) {
        JSONObject object = new JSONObject();
        object.put("sharePage", sharePage);
        return object;
    }

    /**
     * 提交分享成功信息
     */
    private void requestShareSuccess(JSONObject object) {
//        Call<ApiResponse> call = RDClient.getService(WebApi.class).submitShareAction(object);
//        call.enqueue(new RequestCallBack<ApiResponse>() {
//            @Override
//            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
//                UIUtils.showToast(response.body().getMsg());
//                //
//                sendBroadcast(HTML5WebView.ACTION_REFRESH);
//            }
//        });
    }


}
