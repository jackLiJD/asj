package com.ald.asjauthlib.authframework.core.ui;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.protocol.AlaProtocolHandler;
import com.ald.asjauthlib.authframework.core.protocol.data.ProtocolData;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/15 11:37
 * 描述：
 * 修订历史：
 */
public class AlaWebView extends WebView {
    protected ProtocolData protocolData;
    protected AlaProtocolHandler alaProtocolHandler;
    // 用于存放callback，一但里面有数据，就要通知web来取（采用开关online offline的方式来通知）
    private List<String> callbackDataList = new ArrayList<>();
    private boolean online = true;
    private String webViewId;
    private boolean show;
    private WebViewType webViewType;
    private LoadUrlListener listener;
    private OnScrollChangedCallback callback;

    public AlaWebView(Context context) {
        super(context);
        init();
    }

    public AlaWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        webViewType = WebViewType.NORMAL;
        show = true;
        alaProtocolHandler = new AlaProtocolHandler();
    }

    public OnScrollChangedCallback getCallback() {
        return callback;
    }

    public void setCallback(OnScrollChangedCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != callback)
            callback.onScroll(l - oldl, t - oldt);
    }

    public void setProtocolData(ProtocolData protocolData) {
        this.protocolData = protocolData;
    }

    public void addWebJS(boolean netFirst) {
        MiscUtils.enableHTML5(this, netFirst);
        addJavascriptInterface(this, "ala");
        addJavascriptInterface(this, "alaWebCore");
        addJavascriptInterface(new AlaWebViewData(), "alaAndroidWebView1");
        addJavascriptInterface(new AlaWebViewCallbackData(), "alaAndroidWebView2");
    }

    private String getOneCallback() {
        if (MiscUtils.isNotEmpty(callbackDataList)) {
            return callbackDataList.remove(0);
        }
        return null;
    }

    private void convulsions() {
        AlaConfig.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlaWebView.this.setNetworkAvailable(online = !online);
            }
        });
    }

    private static String addCallbackName(String callbackName, String script) {
        try {
            JSONObject object = new JSONObject();
            object.put("callback", callbackName);
            object.put("data", script);
            return object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class AlaWebViewData {
        @JavascriptInterface
        public String getAlaWebViewData(String url, final String callback) {
            if (MiscUtils.isEmpty(url)) {
                return "";
            }
            final Uri uri = Uri.parse(url);
            protocolData.alaUri = uri;
            protocolData.callbackName = callback;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String script = alaProtocolHandler.handleProtocol("", protocolData);
                    if (MiscUtils.isEmpty(script)) {
                        return;
                    }
                    script = addCallbackName(callback, script);
                    callbackDataList.add(script);
                    convulsions();
                }
            }).start();
            if ("http".equals(uri.getHost())) {
                return protocolData.httpId;
            } else {
                return "";
            }
        }

        @JavascriptInterface
        public String getAlaWebViewData(String url) {
            if (MiscUtils.isEmpty(url)) {
                return "";
            }
            Uri uri = Uri.parse(url);
            protocolData.alaUri = uri;
            return alaProtocolHandler.handleProtocol("", protocolData);
        }
    }

    public void handleCallback(String callbackName, String script) {
        String ret = addCallbackName(callbackName, script);
        callbackDataList.add(ret);
        convulsions();
    }

    class AlaWebViewCallbackData {
        @JavascriptInterface
        public String getAlaWebViewCallbackData() {
            return getOneCallback();
        }
    }

    @JavascriptInterface
    public String getVersion() {
        return "4.3";
    }

    public void setWebViewId(String webViewId) {
        this.webViewId = webViewId;
    }

    public String getWebViewId() {
        return webViewId;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public WebViewType getWebViewType() {
        return webViewType;
    }

    public void setWebViewType(WebViewType webViewType) {
        this.webViewType = webViewType;
    }

    public enum WebViewType {
        NORMAL, HIDDEN
    }

    public void setListener(LoadUrlListener listener) {
        this.listener = listener;
    }

    @Override
    public void loadUrl(String url) {
        if (listener != null) {
            listener.loadUrl(url);
        }
        super.loadUrl(url);
    }

    public interface LoadUrlListener {
        public void loadUrl(String url);
    }

    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }
}
