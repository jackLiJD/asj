package com.ald.asjauthlib.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import com.ald.asjauthlib.utils.Constant;
import com.ald.asjauthlib.R;
import com.ald.asjauthlib.databinding.FragmentOperationBinding;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.config.AlaUrl;
import com.ald.asjauthlib.authframework.core.protocol.AlaProtocol;
import com.ald.asjauthlib.authframework.core.protocol.AlaProtocolHandler;
import com.ald.asjauthlib.authframework.core.protocol.data.ProtocolData;
import com.ald.asjauthlib.authframework.core.protocol.webclient.AlaWebChromeClient;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.vm.BaseVM;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Yangyang on 2018/4/18.
 * desc:
 */

public class FqShopVM extends BaseVM {
    private static final String TAG = "FqShopVM";
    private FragmentOperationBinding binding;
    private Fragment fragment;
    private String url;
    private final com.ald.asjauthlib.widget.HTML5WebView webView;
    private final AlaProtocolHandler protocol;
    private final ProtocolData data;
    private static final String SHARE_NAME = "html5_share";
    private final ProgressBar progressBar;
    private static final String LOCAL_ERROR_PAGE_URL = "file:///android_asset/fw/error_page/error.htm";
    private volatile boolean webViewReceivedError;
    private boolean showProgress = true;
    private View errorNetworkView;
    private boolean isError;

    public FqShopVM(FragmentOperationBinding binding, Fragment fragment) {
        url = AlaConfig.getServerProvider().getH5Server() + Constant.H5_FQ_SHOP;
        this.binding = binding;
        this.fragment = fragment;
        webView = binding.webLoanIndex;
        progressBar = binding.progressLoanIndex;
        errorNetworkView = binding.llErrorNetwork;
        initProgressBar();
        protocol = new AlaProtocolHandler();
        data = new ProtocolData();
        data.webView = webView;
        data.shareName = SHARE_NAME;
        data.stateMap = new HashMap<>();
        webView.addWebJS(true);
        webView.setProtocolData(data);
        webView.setWebViewClient(getWebClient());
        webView.setWebChromeClient(getAlaWebChromeClient(data));
        webView.addJavascriptInterface(new OperationJsMethod(), "loanJsMethod");
        webView.addJavascriptInterface(new JSToJava(webView), "alaAndroid");
        webView.addJavascriptInterface(new OTOJSToJava(webView), "otosaas");
        webView.addJavascriptInterface(new BannerPositionJSToJava(webView), "bannerPosition");
        loadUrl();
    }

    public void loadUrl() {
        if (webView != null && url != null) {
            String s = addAppInfo(url).toString();
            webView.loadUrl(s);
        }
    }

    /**
     * AlaWebChromeClient
     */
    private AlaWebChromeClient getAlaWebChromeClient(ProtocolData data) {
        return new AlaWebChromeClient(data, progressBar) {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        };
    }


    /**
     * WebViewClient
     */
    private WebViewClient getWebClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //binding.webviewRefreshLayout.finishRefresh();
                if (showProgress) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                Log.d(TAG, "onPageFinished befor: " + url);
                super.onPageFinished(view, url);
                //这里用完全匹配，因为任何本地网页都是file:///开头的，只用file:///匹配会拦截所有本地网页
                if (LOCAL_ERROR_PAGE_URL.equals(url)) {
                    webViewReceivedError = true;
                } else if (webViewReceivedError) {
                    webViewReceivedError = false;
                    view.clearHistory();
                }
                if (showProgress) {
                    progressBar.setVisibility(View.GONE);
                }
                if (isError) {
                    return;
                }
                if (webView != null) {
                    webView.setVisibility(View.VISIBLE);
                }
                if (errorNetworkView != null) {
                    errorNetworkView.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                if (handleAlaProtocol(url)) {
                    return true;
                }
                String s = addAppInfo(url).toString();
                webView.loadUrl(s);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed(); // 接受网站证书
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isError = true;
                if (webView != null) {
                    webView.setVisibility(View.GONE);
                }
                if (errorNetworkView != null) {
                    errorNetworkView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private boolean handleAlaProtocol(String url) {
        data.alaUri = Uri.parse(url);
        String script = protocol.handleProtocol("", data);
        return MiscUtils.isEmpty(script);
    }

    /**
     * 初始化进度条
     */
    private void initProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    class OperationJsMethod {
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
                UIUtils.showToast(AlaConfig.getResources().getString(R.string.js_to_java_info_null));
            }
        }
    }

    public void clickRetry(View view) {
        if (view.getId() == R.id.tv_error_retry) {
            /*if (webView != null) {
                webView.setVisibility(View.VISIBLE);
            }*/
            loadUrl();
            if (errorNetworkView != null) {
                errorNetworkView.setVisibility(View.GONE);
            }
        }
    }


    private StringBuilder addAppInfo(String url) {
        StringBuilder sb = new StringBuilder(url);
        if (url != null && (url.contains(AlaProtocol.ROOT_H5_HOST))
                || (url.contains(AlaProtocol.ROOT_IP) || (url.contains(AlaProtocol.ROOT_IP1)))) {
            Map<String, String> map = new HashMap<>();
            map.put(AlaUrl.USER_NAME, AlaConfig.getAccountProvider().getUserName());
            if (AlaConfig.isLand()) {
                map.put(AlaUrl.SIGN, AlaConfig.getAccountProvider().getUserToken());
            }
            AlaUrl.buildJsonUrl(sb, "4.3", map, false, null);
        }
        return sb;
    }

}
