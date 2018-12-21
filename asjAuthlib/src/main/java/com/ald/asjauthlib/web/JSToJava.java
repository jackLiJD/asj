package com.ald.asjauthlib.web;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.ald.asjauthlib.auth.AuthApi;
import com.ald.asjauthlib.utils.MXAuthUtils;
import com.ald.asjauthlib.utils.MxAuthCallBack;
import com.ald.asjauthlib.auth.model.ExtraFundModel;
import com.ald.asjauthlib.auth.model.ExtraUserIdModel;
import com.ald.asjauthlib.auth.model.FundModel;
import com.ald.asjauthlib.utils.AppUtils;
import com.ald.asjauthlib.utils.Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bqs.risk.df.android.BqsDF;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.location.LocationResult;
import com.ald.asjauthlib.authframework.core.location.LocationUtils;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.RequestCallBack;
import com.ald.asjauthlib.authframework.core.network.entity.ApiResponse;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;

import cn.tongdun.android.shell.FMAgent;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Js交互
 * Created by sean yu on 2017/5/23.
 */
public class JSToJava {
    private static final String TAG = "JSToJava";
    private WebView webView;
    private MXAuthUtils authUtils;

    public JSToJava(WebView webView) {
        this.webView = webView;
    }

    @JavascriptInterface
    public void appLogin() {
        if (!AlaConfig.isLand()) {
            Utils.jumpToLoginNoResult();
        } else {
            webView.post(() -> webView.loadUrl(JavaToJS.LOGIN_SUCCESS));
        }
    }

    @JavascriptInterface
    public void closeWebView() {
        if (webView.getContext() instanceof Activity) {
            ((Activity) webView.getContext()).finish();
        }
    }

    @JavascriptInterface
    public void openActivity(String openInfo) {
        JSONObject object = JSONObject.parseObject(openInfo);
        String className = object.getString("className");
        try {
            Class openClass = Class.forName(className);
            ActivityUtils.push(openClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void loadUrl(final String url) {
        webView.post(() -> webView.loadUrl(url));
    }

    /**
     * 提供给h5白领贷获取定位及设备指纹信息
     */
    @JavascriptInterface
    public void getNativeBorrowInfo() {
        LocationResult result = LocationUtils.getCurrentLocation();
        if (result != null) {
            generateBorrowInfo(result);
        } else {
            AlaConfig.execute(new Runnable() {
                @Override
                public void run() {
                    LocationResult result = LocationUtils.requestLocation(50000L);
                    if (result != null) {
                        generateBorrowInfo(result);
                    }
                }
            });
        }
    }

    private void generateBorrowInfo(LocationResult result) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("latitude", result.getLatitude() + "");
        jsonObject.put("longitude", result.getLongitude() + "");
        jsonObject.put("province", result.getProvince());
        jsonObject.put("city", result.getCityName());
        jsonObject.put("county", result.getDistrict());
        jsonObject.put("address", result.getAddress());
        //同盾风控
        String blackBox = FMAgent.onEvent(webView.getContext());
        jsonObject.put("blackBox", blackBox);
        //白骑士设备指纹
        String bqsBlackBox = BqsDF.getTokenKey();
        jsonObject.put("bqsBlackBox", bqsBlackBox);
        webView.post(() -> {
            //获取到数据后调用h5方法将数据传递给h5
            webView.loadUrl("javascript:getNativeBorrowInfoMsg('" + JSON.toJSONString(jsonObject) + "')");
        });
    }



    @JavascriptInterface
    public void getBlackBox() {
        //同盾风控
        final String blackBox = FMAgent.onEvent(webView.getContext());
        webView.post(() -> webView.loadUrl("javascript:getBlackBox('" + blackBox + "')"));
    }

    /**
     * 跳转单项认证社保、公积金、网银
     */
    @JavascriptInterface
    public void jumpToAuth(String type) {
        if (authUtils == null) {
            initAuthUtils();
        }
        Call<ExtraUserIdModel> call;
        switch (type) {
            case "jinpo":
                //社保
                call = RDClient.getService(AuthApi.class).authSocialSecurity();
                call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
                    @Override
                    public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                        String authInfo = response.body().getTransPara();
                        authUtils.configMxParams(authInfo);
                        authUtils.handleSecurity();
                    }
                });
                break;
            case "fund":
                //公积金
                judgeAuthFund();
                break;
            case "onlinebank":
                //网银
                call = RDClient.getService(AuthApi.class).authOnlineBank();
                call.enqueue(new RequestCallBack<ExtraUserIdModel>() {
                    @Override
                    public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                        String authInfo = response.body().getTransPara();
                        authUtils.configMxParams(authInfo);
                        authUtils.handleOnlineBank();
                    }
                });
                break;
        }
        webView.reload();
    }


    /**
     * 支付成功页（H5）跳转到订单详情
     */
    @JavascriptInterface
    public void openOrderDetail() {
        ActivityUtils.pop();
    }

    private void judgeAuthFund() {
        Call<FundModel> fundCall = RDClient.getService(AuthApi.class).authFundSwitch();
        fundCall.enqueue(new RequestCallBack<FundModel>() {
            @Override
            public void onSuccess(Call<FundModel> call, Response<FundModel> response) {
                FundModel model = response.body();
                if ("1".equals(model.getFundSwitch())) handleFundInfo(); // 1: 51公积金管家
                else {
                    Call<ExtraUserIdModel> mXCall = RDClient.getService(AuthApi.class).authFund();
                    mXCall.enqueue(new RequestCallBack<ExtraUserIdModel>() {
                        @Override
                        public void onSuccess(Call<ExtraUserIdModel> call, Response<ExtraUserIdModel> response) {
                            String authInfo = response.body().getTransPara();
                            authUtils.configMxParams(authInfo);
                            authUtils.handleFund();
                        }
                    });
                }
            }
        });
    }

    private void handleFundInfo() {
        Call<ExtraFundModel> call = RDClient.getService(AuthApi.class).authFundNew();
        call.enqueue(new RequestCallBack<ExtraFundModel>() {
            @Override
            public void onSuccess(Call<ExtraFundModel> call, Response<ExtraFundModel> response) {
                String url = response.body().getUrl();
                webView.loadUrl(url);
            }
        });
    }

    private void initAuthUtils() {
        authUtils = new MXAuthUtils((Activity) webView.getContext()).setCallBack(new MxAuthCallBack() {
            @Override
            public void authSuccess(String authCode, String msg) {
                if (MXAuthUtils.PARAM_TASK_FUND.equals(authCode)) {
                    //公积金
                    handleAuthInfo("FUND");
                } else if (MXAuthUtils.PARAM_TASK_SECURITY.equals(authCode)) {
                    //社保
                    handleAuthInfo("SOCIAL_SECURITY");
                } else if (MXAuthUtils.PARAM_TASK_ONLINEBANK.equals(authCode)) {
                    //网银
                    handleAuthInfo("ONLINEBANK");
                }
            }

            @Override
            public void authError(String authCode, String errorMsg) {
                UIUtils.showToast(errorMsg);
            }
        });
    }

    /**
     * 请求认证
     */
    private void handleAuthInfo(final String authType) {
        JSONObject object = new JSONObject();
        object.put("authType", authType);
        Call<ApiResponse> call = RDClient.getService(AuthApi.class).authSupplyVerifying(object);
        call.enqueue(new RequestCallBack<ApiResponse>() {
            @Override
            public void onSuccess(Call<ApiResponse> call, Response<ApiResponse> response) {
                //刷新H5
                webView.reload();
            }
        });
    }



    /**
     * 跳转到其他h5
     * 由于iPhone没有返回键，在当前webview跳转到其他URL后无法返回到h5首页tab
     * 故添加此方法跳转到新的h5页面
     *
     * @param url 跳转地址
     */
    @JavascriptInterface
    public void jumpToOtherH5(String url) {
        if (MiscUtils.isNotEmpty(url)) {
            Intent intent = new Intent();
            intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
            ActivityUtils.push(HTML5WebView.class, intent);
        } else {
            UIUtils.showToast("获取信息失败，请重试");
        }
    }


    public void onRequestPermissionsResult(final Activity activity, final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) return;
        // 权限被用户拒绝时被调用
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])) { // 用户拒绝权限但未勾选不再询问
            new AlertDialog.Builder(activity)
                    .setTitle("权限提示")
                    .setMessage("该功能需要权限支持,请点击允许")
                    .setPositiveButton("允许", (dialog, which) -> ActivityCompat.requestPermissions(activity, permissions, requestCode))
                    .setNegativeButton("拒绝", null)
                    .create().show();
        } else { // 用户拒绝权限并勾选不再询问
            new AlertDialog.Builder(activity)
                    .setTitle("权限提示")
                    .setMessage("该功能所需权限被拒绝,请在手机设置中打开应用所需相机权限")
                    .setPositiveButton("现在就去", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("不去", null)
                    .create().show();
        }
    }

    /**
     * 关闭当前页面
     */
    @JavascriptInterface
    public void closeNative() {
        ActivityUtils.pop();
    }

    /**
     * h5调用原生设置h5页面标题
     *
     * @param title 标题
     */
    @JavascriptInterface
    public void getWebViewTitle(String title) {
        if (MiscUtils.isNotEmpty(title)) {
            ((AlaActivity) webView.getContext()).setTitle(title);
        }
    }


    @JavascriptInterface
    public void getCouponCenter(String centerUrl) {
        if (TextUtils.isEmpty(centerUrl)) return;
        if (onListener != null) {
            onListener.dealURL(centerUrl);
        }
    }

    @JavascriptInterface
    public boolean getNoticeStatus() {
        return UIUtils.getNoticeStatus();
    }

    private OnListener onListener;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public interface OnListener {
        void dealURL(String url);
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }


    /**
     * 发送短信
     *
     * @param phone 号码
     * @param msg   短信内容
     */
    @JavascriptInterface
    public void smsto(String phone, String msg) {
//        SmsManager manager = SmsManager.getDefault();
//        PendingIntent sentIntent = PendingIntent.getBroadcast(webView.getContext(), 0,
//                new Intent(HTML5WebView.ACTION_REFRESH_CONTACTS), 0);
//        manager.sendTextMessage(phone, null, msg, sentIntent, null);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone.trim()));
        intent.putExtra("sms_body", msg);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityUtils.peek().startActivity(intent);
    }



    /**
     * 特别注意h5是否被刷新
     * webview的goback不能在jsBradge线程上触发
     */
    @JavascriptInterface
    public void goBackAction() {
        AlaConfig.postOnUiThread(() -> {
            if (webView != null && webView.canGoBack()) {
                webView.goBack();
            } else {
                if (webView.getContext() instanceof Activity) {
                    ((Activity) webView.getContext()).finish();
                }
            }
        });
    }


    @JavascriptInterface
    public void getMaidianInfo() {
        String maidianJson = AppUtils.getNativeMaidianInfo().toJSONString();
        webView.post(() -> webView.loadUrl(String.format(JavaToJS.POST_MAIDIAN_JSON, maidianJson)));

    }
}
