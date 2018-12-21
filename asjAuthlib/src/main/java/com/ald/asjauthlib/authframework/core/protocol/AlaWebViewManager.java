package com.ald.asjauthlib.authframework.core.protocol;

import com.ald.asjauthlib.authframework.core.ui.AlaWebView;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/15 11:32
 * 描述：
 * 修订历史：
 */
public class AlaWebViewManager {
    private final static String TAG = "AlaWebViewManager";
    public final static int HIDDEN_WEB_VIEW_COUNT = 2;

    private static Map<String, AlaWebView> staticWebViews = new ConcurrentHashMap<>(2);

   /* public static String newAlaWebView(final Activity activity, final String loadURL) {
        if (staticWebViews.size() >= HIDDEN_WEB_VIEW_COUNT) {
            return "false";
        }
        if (!(activity instanceof HTML5WebView)) {
            return "false";
        }
        final HTML5WebView currentActivity = (HTML5WebView) activity;
        final String webviewId = UUID.randomUUID().toString().replaceAll("-", "");
        AlaConfig.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlaWebView webView = new AlaWebView(activity);
                currentActivity.addHiddenWebView(webView);
                webView.setWebViewId(webviewId);
                webView.setWebViewType(AlaWebView.WebViewType.HIDDEN);
                webView.setShow(false);
                boolean netFirst = MiscUtils.getSharepreferenceValue(HTML5WebView.SHARE_NAME, loadURL, true);
                webView.addWebJS(netFirst);

                final StringBuilder loadBuilder = new StringBuilder(loadURL);
                //如果不是文件协议(或者使用的人说不加)才加后面一坨，否则不加
                if (!loadBuilder.toString().startsWith("file:")) {
                    AlaUrl.buildJsonUrl(loadBuilder, "4.3", null, false, null);
                }

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        currentActivity.removeHiddenWebView(view);
                    }
                });

                webView.loadUrl(loadBuilder.toString());

                staticWebViews.put(webviewId, webView);
            }
        });
        return webviewId;
    }*/

    public static void destroyAlaWebView(final String webviewId) {
        AlaConfig.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (MiscUtils.isNotEmpty(webviewId)) {
                        final AlaWebView webView = staticWebViews.get(webviewId);
                        if (webView != null) {
                            webView.destroy();
                            staticWebViews.remove(webviewId);
                        }
                    }
                } catch (Exception e) {
                    Logger.w(TAG, "webviewId: " + webviewId, e);
                }
            }
        });
    }

    public static void destroyAlaWebView(final AlaWebView webView) {
        AlaConfig.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!webView.isShow()) {
                    webView.destroy();
                    staticWebViews.remove(webView.getWebViewId());
                }
            }
        });
    }

    public static void destroyAllAlaWebView() {
        AlaConfig.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (final AlaWebView alaWebView : staticWebViews.values()) {
                    alaWebView.destroy();
                }
                staticWebViews.clear();
            }
        });
    }

    public static boolean hasTheWebView(String webviewId) {
        return webviewId != null && staticWebViews.containsKey(webviewId);
    }

    public static AlaWebView getWebView(String webviewId) {
        return staticWebViews.get(webviewId);
    }
}
