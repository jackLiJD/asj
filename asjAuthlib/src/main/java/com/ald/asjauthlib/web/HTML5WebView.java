package com.ald.asjauthlib.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.utils.Utils;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.immersionBar.NoImmersionBar;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;

import java.lang.ref.WeakReference;
import java.net.URLDecoder;



/*
 * Created by sean yu on 2017/6/3.
 */

public class HTML5WebView extends com.ald.asjauthlib.authframework.core.activity.HTML5WebView implements NoImmersionBar {
    public static final String ACTION_REFRESH = "auth__action_refresh__";
    public static final String ACTION_REFRESH_CONTACTS = "action_refresh_contacts";
    private InnerReceiver receiver;
    private JSToJava jsToJava;
    private boolean hasSetSystemFit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWebView().addWebJS(true);
        getWebView().addJavascriptInterface(jsToJava = new JSToJava(getWebView()), "alaAndroid");
        getWebView().addJavascriptInterface(new OTOJSToJava(getWebView()), "otosaas");
        //内部广播接收器
        receiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter(ACTION_REFRESH);
        registerReceiver(receiver, filter);
        setImmersionBar();
        hasSetSystemFit = true;

    }


    @Override
    protected boolean overrideUrlLoading(WebView view, String url) {
        //先隐藏右侧icon
        hiddenRightOption();
        if (url.contains("showTitle=false")) {
            hideTitleBar();
            if (hasSetSystemFit) {
                setTransImmersionBar();
                hasSetSystemFit = !hasSetSystemFit;
            }
        } else {
            showTitleBar();
            if (!hasSetSystemFit) {
                setImmersionBar();
                hasSetSystemFit = !hasSetSystemFit;
            }
        }

        //h5控制是否显示标题栏右侧按钮及定义样式
        if (url.contains("addUiName=subTitle")) {
            Uri uri = Uri.parse(url);
            if (uri != null) {
                String params = uri.getQueryParameter("params");
                if (MiscUtils.isNotEmpty(params)) {
                    JSONObject parseObject = JSONObject.parseObject(params);
                    String rightTitleText = parseObject.getString("titleText");
                    final String rightTitleUrl = parseObject.getString("titleUrl");
                    String rightTitleColor = parseObject.getString("titleColor");
                    String decodeUrl = "";
                    if (MiscUtils.isNotEmpty(rightTitleUrl)) {
                        decodeUrl = URLDecoder.decode(rightTitleUrl);
                    }
                    final String finalDecodeUrl = decodeUrl;
                    showRightTextOption(rightTitleText, view14 -> {
                        if (MiscUtils.isNotEmpty(finalDecodeUrl)) {
                            Intent intent = new Intent();
                            intent.putExtra(HTML5WebView.INTENT_BASE_URL, AlaConfig.getServerProvider().getAppServer() + finalDecodeUrl);
                            ActivityUtils.push(HTML5WebView.class, intent);
                        }
                    });
                    setRightTextColor(Utils.colorValue(rightTitleColor, R.color.fw_text_black));
                }
            }
        }

        return super.overrideUrlLoading(view, url);
    }

    private void statisticsShareClick(String share_media) {
        try {
            if (getWebView() != null) {
                getWebView().loadUrl("javascript:postshareex('" + share_media + "')");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void statisticsShareSuccess(String share_media) {
        try {
            if (getWebView() != null) {
                getWebView().loadUrl("javascript:postshareaf('" + share_media + "')");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * webview调用onPageFinished后的调用
     * 每个url界面返回一次
     */
    @Override
    public void onWebViewFinished(WebView view, String url) {
        if (view == null)
            view = getWebView();
        if (view == null)
            return;
        String clearUrl = clearAppInfo(view.getUrl());
        Logger.d("onWebViewFinished", clearUrl);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * 分享回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != jsToJava) jsToJava.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (null != jsToJava)
            jsToJava.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    /**
     * 内部类的广播接收器
     */
    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_REFRESH.equals(action)) {
                refreshWebView();
            } else if (ACTION_REFRESH_CONTACTS.equals(action)) {
                getWebView().loadUrl(JavaToJS.REFRESH_CONTACTS);
            }
        }
    }

}
