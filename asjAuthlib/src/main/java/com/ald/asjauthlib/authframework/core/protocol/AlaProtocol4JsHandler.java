package com.ald.asjauthlib.authframework.core.protocol;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.ald.asjauthlib.authframework.core.activity.HTML5WebView;
import com.ald.asjauthlib.authframework.core.callphone.CallPhoneManager;
import com.ald.asjauthlib.authframework.core.callphone.PhoneCallRequest;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.protocol.data.ProtocolData;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/15 11:34
 * 描述：AlaWebChromeClient处理事件
 * 修订历史：
 */
public class AlaProtocol4JsHandler implements ProtocolHandler {
//    private final static String TAG = "AlaProtocol4JsHandler";

    @Override
    public String handleProtocol(String group, final ProtocolData protocolData) {
        if (AlaConfig.getCurrentActivity() == null || protocolData == null) {
            return null;
        }
        final Uri uri = protocolData.alaUri;
        String path = uri.getPath();
        String script = uri.getQueryParameter("callback");
        Logger.i("Sevn", "path: " + path);
        if (AlaProtocol.Protocol4Js.HOST_INFO.equals(path)) {
            try {
                String name = uri.getQueryParameter("name");
                if ("mucang.version".equals(name)) {
                    if (MiscUtils.isNotEmpty(script)) {
                        JSONObject json = new JSONObject();
                        json.put(name, 4.0f);
                        JSONObject callback = new JSONObject();
                        callback.put("value", json.toString());
                        script = script.replace("$context", callback.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return script;
        }
        if (AlaProtocol.Protocol4Js.APP_ROOT_STORAGE.equals(uri.getAuthority())) {
            // 存储数据
            if ("/set".equals(path)) {
                String storageKey = uri.getQueryParameter("key");
                String storageValue = uri.getQueryParameter("value");
                MiscUtils.setSharedPreferenceValue(protocolData.shareName, storageKey, storageValue);
            }
        } else if (AlaProtocol.Protocol4Js.APPLET_CHECK.equals(path)) {
            script = ProtocolUtils.handleCheckInstallUri(uri, protocolData.stateMap);
        } else if (AlaProtocol.Protocol4Js.APPLET_INSTALL.equals(uri.getPath())) {
            ProtocolUtils.handleDownloadUri(uri, protocolData.stateMap, protocolData.webView);
            script = "";
        } else if (AlaProtocol.Protocol4Js.APPLET_START.equals(uri.getPath())) {
            ProtocolUtils.handleStartUri(uri);
            script = "";
        } else if (AlaProtocol.Protocol4Js.SHOW.equals(path)) {
            String timeOut = uri.getQueryParameter("timeout");
            ProtocolUtils.AlaProtocolData data = new ProtocolUtils.AlaProtocolData();
            data.alaUri = protocolData.alaUri;
            data.bottomWebView = protocolData.webView;
            ProtocolUtils.show(data, MiscUtils.parseInt(timeOut, 0));
        } else if (AlaProtocol.Protocol4Js.OPEN.equals(path)) {
            ProtocolUtils.AlaProtocolData data = new ProtocolUtils.AlaProtocolData();
            data.alaUri = protocolData.alaUri;
            data.bottomWebView = protocolData.webView;
            ProtocolUtils.open(data);
        } else if (AlaProtocol.Protocol4Js.CLOSE.equals(path)) {
            AlaConfig.postOnUiThread(new Runnable() {

                @Override
                public void run() {
                    protocolData.webView.setVisibility(View.GONE);
                }
            });
        } else if (AlaProtocol.Protocol4Js.DESTROY.equals(path)) {
            Activity currentActivity = AlaConfig.getCurrentActivity();
            if (currentActivity != null)
                currentActivity.finish();
        } else if (AlaProtocol.Protocol4Js.CHANGE_MODE.equals(path)) {
            String mode = uri.getQueryParameter("mode");
            if (protocolData.offLine != null) {
                if ("online".equals(mode)) {
                    if (protocolData.offLine) {
                        protocolData.offLine = false;
                    }
                } else if ("offline".equals(mode)) {
                    if (!protocolData.offLine) {
                        protocolData.offLine = true;
                    }
                }
                if (MiscUtils.isNotEmpty(protocolData.shareKey) && MiscUtils.isNotEmpty(protocolData.shareName)) {
                    MiscUtils.setSharedPreferenceValue(protocolData.shareName, protocolData.shareKey,
                            protocolData.offLine);
                }
            }
        } else if (AlaProtocol.Protocol4Js.NETWORK_MODE.equals(path)) {
            String mode = uri.getQueryParameter("mode");
            boolean netFirst;
            netFirst = "networkfirst".equals(mode);
            if (MiscUtils.isNotEmpty(protocolData.netWorkFirstKey) && MiscUtils.isNotEmpty(protocolData.shareName)) {
                MiscUtils.setSharedPreferenceValue(protocolData.shareName, protocolData.netWorkFirstKey, netFirst);
            }
        } else if (AlaProtocol.Protocol4Js.CALL_PHONE.equals(path)) {
            final String title = uri.getQueryParameter("title");
            final String event = uri.getQueryParameter("event");
            final List<String> labels = uri.getQueryParameters("label");
            final List<String> phones = uri.getQueryParameters("phone");
            Activity currentActivity = AlaConfig.getCurrentActivity();
            if (currentActivity != null && !currentActivity.isFinishing()) {
                AlaConfig.postOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        String currentUrl = "";
                        if (protocolData.webView != null) {
                            currentUrl = protocolData.webView.getUrl();
                        }
                        ProtocolUtils.showCallPhone(currentUrl, title, event, labels, phones);
                    }
                });
            }
        } else if (AlaProtocol.Protocol4Js.ALERT.equals(path)) {
            final String message = uri.getQueryParameter("message");
            final String title = uri.getQueryParameter("title");
            final String script0 = script;
            Activity currentActivity = AlaConfig.getCurrentActivity();
            if (currentActivity != null && !currentActivity.isFinishing()) {
                AlaConfig.postOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ProtocolUtils.showMyDialog(message, title, script0, protocolData.webView);
                    }
                });
            }
        } else if (AlaProtocol.Protocol4Js.TOAST.equals(path)) {
            final String message = uri.getQueryParameter("message");
            UIUtils.showToast(message);
        } else if (AlaProtocol.Protocol4Js.DIALOG.equals(path)) {
            final String message = uri.getQueryParameter("message");
            final String action = uri.getQueryParameter("action");
            final String cancel = uri.getQueryParameter("cancel");
            final String title = uri.getQueryParameter("title");
            final String script0 = script;
            Activity currentActivity = AlaConfig.getCurrentActivity();
            if (currentActivity != null && !currentActivity.isFinishing()) {
                AlaConfig.postOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ProtocolUtils
                                .showMyDialog(title, message, protocolData.openNewWindow, action, cancel, script0,
                                        protocolData.webView, protocolData.animStyle);
                    }
                });
            }
        } else if (AlaProtocol.Protocol4Js.DIAL_PHONE.equals(path)) {
//            final String event = uri.getQueryParameter("event");
            final String phoneNumber = uri.getQueryParameter("phone");
            final String label = uri.getQueryParameter("label");
            AlaConfig.postOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String source = "";
                    if (protocolData.webView != null) {
                        source = protocolData.webView.getUrl();
                    }
                    PhoneCallRequest request = new PhoneCallRequest(phoneNumber, HTML5WebView.CALL_PHONE_TEL_GROUP, source,
                            label);
                    CallPhoneManager.getInstance().callPhone(request);
                }
            });

        } else if (AlaProtocol.Protocol4Js.GO_BACK.equals(path)) {
            Activity currentActivity = AlaConfig.getCurrentActivity();
            if (currentActivity != null)
                currentActivity.finish();
        } else if (AlaProtocol.Protocol4Js.TOOL_BAR.equals(path)) {
            if (protocolData.toolBar != null) {
                boolean enable = Boolean.parseBoolean(uri.getQueryParameter("enable"));
                if (enable) {
                    protocolData.toolBar.setVisibility(View.VISIBLE);
                } else {
                    protocolData.toolBar.setVisibility(View.GONE);
                }
            }
        } else if (AlaProtocol.Protocol4Js.SHARE.equals(path)) {
            String shareMessage = uri.getQueryParameter("message");
            String shareType = ProtocolUtils.getShareType(uri.getQueryParameter("website"));
            Intent intent = new Intent(ProtocolUtils.SHARE_ACTION);
            intent.putExtra(ProtocolUtils.SHARE_MESSAGE, shareMessage);
            intent.putExtra(ProtocolUtils.SHARE_TYPE, shareType);
            Activity currentActivity = AlaConfig.getCurrentActivity();
            if (currentActivity != null)
                currentActivity.sendOrderedBroadcast(intent, null);
        } else if (AlaProtocol.Protocol4Js.OPEN_NATIVE.equals(path)) {
            if (ProtocolUtils.getOpenNativeHandler() != null) {
                String name = protocolData.alaUri.getQueryParameter("name");
                String params = protocolData.alaUri.getQueryParameter("params");
                String config = protocolData.alaUri.getQueryParameter("config");
                try {
                    String paramsString = "";
                    if (MiscUtils.isNotEmpty(params)) {
                        paramsString = new JSONObject(params).toString();
                    }
                    String configString = "";
                    if (MiscUtils.isNotEmpty(config)) {
                        configString = new JSONObject(config).toString();
                    }
                    ProtocolUtils.getOpenNativeHandler().onHandelOpenNative(name, paramsString, configString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return script;
    }
}
