package com.ald.asjauthlib.authframework.core.protocol;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.view.View;

import com.ald.asjauthlib.authframework.core.activity.HTML5WebView;
import com.ald.asjauthlib.authframework.core.callphone.CallPhoneManager;
import com.ald.asjauthlib.authframework.core.callphone.PhoneCallRequest;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.protocol.data.ProtocolData;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;

import org.json.JSONObject;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/15 11:34
 * 描述：WebViewClient处理事件
 * 修订历史：
 */
public class AlaProtocolHandler implements ProtocolHandler {

    @Override
    public String handleProtocol(String group, final ProtocolData protocolData) {
        if (AlaConfig.getCurrentActivity() == null || protocolData == null) {
            return null;
        }
        final Uri uri = protocolData.alaUri;
        String path = uri.getPath();
        Logger.i("Ala", "path: " + path);
        //芝麻信用
        if (AlaProtocol.GROUP_ZMXY.equals(group)) {
            if (path.contains(AlaProtocol.Protocol.ZMXY_KEY)) {
                //芝麻信用回调
                if (ProtocolUtils.getOpenNativeHandler() != null) {
//                    String config = protocolData.alaUri.getQueryParameter("config");
                    String params = protocolData.alaUri.getQueryParameter("params");
                    String sign = protocolData.alaUri.getQueryParameter("sign");
                    ProtocolUtils.getOpenNativeHandler().onHandelOpenNative(AlaProtocol.Protocol.ZMXY_KEY, params, sign);
                }
            }
            return null;
        } else if (AlaProtocol.GROUP_MOBILE_OPERATOR.equals(group)) {
            if (path.contains(AlaProtocol.Protocol.MOBILE_SUCCESS_KEY)) {
                //手机运营商提交数据
                if (ProtocolUtils.getOpenNativeHandler() != null) {
                    String config = AlaProtocol.Protocol.MOBILE_SUCCESS_KEY;
                    String params = protocolData.alaUri.getQueryParameter("params");
                    ProtocolUtils.getOpenNativeHandler()
                            .onHandelOpenNative(AlaProtocol.GROUP_MOBILE_OPERATOR, params, config);
                }
            }
            if (path.contains(AlaProtocol.Protocol.MOBILE_BACK_KEY)) {
                //手机运营商提交数据
                if (ProtocolUtils.getOpenNativeHandler() != null) {
                    String config = AlaProtocol.Protocol.MOBILE_BACK_KEY;
                    String params = protocolData.alaUri.getQueryParameter("fbapiNeedRefreshCurrData");
                    ProtocolUtils.getOpenNativeHandler()
                            .onHandelOpenNative(AlaProtocol.GROUP_MOBILE_OPERATOR, params, config);
                }
            }
            return null;
        } else if (AlaProtocol.Protocol.WUYAO_FUND_STATUS.equals(group)) { // 51公积金管家回调
            if (ProtocolUtils.getOpenNativeHandler() != null) {
                String orderSn = protocolData.alaUri.getQueryParameter("orderSn");
                ProtocolUtils.getOpenNativeHandler().onHandelOpenNative(AlaProtocol.Protocol.WUYAO_FUND_STATUS, orderSn, "");
            }
            return null;
        } else if (AlaProtocol.Protocol.GXB_BACK.equals(group)) { // GXB回调
            if (ProtocolUtils.getOpenNativeHandler() != null) {
                ProtocolUtils.getOpenNativeHandler().onHandelOpenNative(AlaProtocol.Protocol.GXB_BACK, "", "");
            }
            return null;
        } else {
            if (AlaProtocol.Protocol.SHOW.equals(path)) {
                String timeOut = uri.getQueryParameter("timeout");
                ProtocolUtils.AlaProtocolData data = new ProtocolUtils.AlaProtocolData();
                data.alaUri = protocolData.alaUri;
                data.bottomWebView = protocolData.webView;
                ProtocolUtils.show(data, MiscUtils.parseInt(timeOut, 0));
                return null;
            } else if (AlaProtocol.Protocol.OPEN.equals(path)) {
                ProtocolUtils.AlaProtocolData data = new ProtocolUtils.AlaProtocolData();
                data.alaUri = protocolData.alaUri;
                data.bottomWebView = protocolData.webView;
                ProtocolUtils.open(data);
                return null;
            } else if (AlaProtocol.Protocol.CLOSE.equals(path)) {
                AlaConfig.postOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        protocolData.webView.setVisibility(View.GONE);
                    }
                });
                return null;
            } else if (AlaProtocol.Protocol.DIAL_PHONE.equals(path)) {
                final String phoneNumber = uri.getQueryParameter("phone");
                final String label = uri.getQueryParameter("label");
                AlaConfig.postOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String source = "";
                        if (protocolData.webView != null) {
                            source = protocolData.webView.getUrl();
                        }
                        PhoneCallRequest request = new PhoneCallRequest(phoneNumber, HTML5WebView.CALL_PHONE_TEL_GROUP,
                                source, label);
                        CallPhoneManager.getInstance().callPhone(request);
                    }
                });
                return null;
            } else if (AlaProtocol.Protocol.SHARE.equals(path)) {
                String shareMessage = uri.getQueryParameter("message");
                String shareType = ProtocolUtils.getShareType(uri.getQueryParameter("website"));
                Intent intent = new Intent(ProtocolUtils.SHARE_ACTION);
                intent.putExtra(ProtocolUtils.SHARE_MESSAGE, shareMessage);
                intent.putExtra(ProtocolUtils.SHARE_TYPE, shareType);
                Activity currentActivity = AlaConfig.getCurrentActivity();
                if (currentActivity != null)
                    currentActivity.sendOrderedBroadcast(intent, null);
                return null;
            } else if (AlaProtocol.Protocol.OPEN_NATIVE.equals(path)) {
                String name = protocolData.alaUri.getQueryParameter("name");
                String params = protocolData.alaUri.getQueryParameter("params");
                String config = protocolData.alaUri.getQueryParameter("config");
                try {
                    String paramsString = "";
                    if (MiscUtils.isNotEmpty(params)) {
                        if ("APP_SHARE".equals(name) || "APP_DIRECTIONAL_SHARE".equals(name)) {
                            params = new String(Base64.decode(params, Base64.URL_SAFE));
                        }
                        paramsString = new JSONObject(params).toString();
                    }
                    String configString = "";
                    if (MiscUtils.isNotEmpty(config)) {
                        configString = new JSONObject(config).toString();
                    }
                    /*
                     * 由于H5验签的问题，用户在H5页面时被踢出。所以去掉了isLand()判断
                     * */
                    if (AlaProtocol.NATIVE_LOGIN_FOR_WEB.equals(name)) {
//                        return String.valueOf(uri);
//                    } else {
                        if (ProtocolUtils.getOpenNativeHandler() != null) {
                            ProtocolUtils.getOpenNativeHandler().onHandelOpenNative(name, paramsString, configString);
                        }
                    }
                    Intent intent = new Intent(ProtocolUtils.NATIVE_ACTION);
                    intent.putExtra(ProtocolUtils.NATIVE_NAME, name);
                    intent.putExtra(ProtocolUtils.NATIVE_PARAM, paramsString);
                    intent.putExtra(ProtocolUtils.NATIVE_CONFIG, configString);
                    intent.putExtra(ProtocolUtils.NATIVE_URL_QUERY, protocolData.alaUri.getQuery());
                    AlaConfig.getLocalBroadcastManager().sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            return String.valueOf(uri);
        }
    }
}
