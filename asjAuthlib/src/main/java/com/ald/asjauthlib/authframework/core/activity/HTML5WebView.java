package com.ald.asjauthlib.authframework.core.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.protocol.data.ProtocolData;
import com.ald.asjauthlib.authframework.core.ui.AlaWebView;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaUrl;
import com.ald.asjauthlib.authframework.core.protocol.AlaProtocol;
import com.ald.asjauthlib.authframework.core.protocol.AlaProtocolHandler;
import com.ald.asjauthlib.authframework.core.protocol.webclient.AlaWebChromeClient;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.BASE64Encoder;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;

import java.util.HashMap;
import java.util.Map;

import static com.ald.asjauthlib.authframework.core.utils.PermissionCheck.REQUEST_CODE_ALL;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/15 11:28
 * 描述：H5页面
 * 修订历史：
 */
public class HTML5WebView extends AlaActivity implements OnClickListener {
    public static final String CALL_PHONE_TEL_GROUP = "tel_group";
    public static final String LOCAL_ERROR_PAGE_URL = "file:///android_asset/fw/error_page/error.htm";
    public static final String INTENT_BASE_URL = "urlString";// 要访问的网址，不带参数的
    public static final String INTENT_TITLE = "title";// 要访问的网址，不带参数的
    public static final String INTENT_DEFAULT_TITLE = "defaultTitle";// 默认的title
    public static final String INTENT_STATISTICS_NAME = "statisticsName";// 是否需要开启页面显示时长统计
    public static final String INTENT_STATISTICS_ID = "statisticsId";// 是否需要开启页面显示时长统计
    public static final String ORIENTATION_VERTICAL = "portrait";
    public static final String ORIENTATION_HORIZONTAL = "landscape";
    public static final String ORIENTATION_AUTO = "auto";
    public static final String SHARE_NAME = "html5_share";
    private static final String TAG = "HTML5WebView";
    private static final int SELECT_PICTURE_REQUEST_CODE = 2017; // 来自H5直接调用,WebChromeClient中回调
    private static final int SELECT_PICTURES_REQUEST_CODE = 2018; // 来自H5直接调用,WebChromeClient中回调
    private static final String ORIENTATION_NEW = "ala-web-orientation";//新版的屏幕显示方向的url参数
    private static final String HARDWARE = "ala-web-hardware";// 是否开启硬件加速，默认true
    private boolean offLine;
    private volatile boolean netFirst;
    private volatile boolean webViewReceivedError;
    private volatile boolean showProgress = true;
    private boolean lastHardwareStatus;
    private StringBuilder loadBuilder;
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;
    //设置Schemes白名单(公信宝)
    String[] whiteList = {"taobao://", "alipayqr://", "alipays://", "wechat://", "weixin://", "mqq://", "mqqwpa://", "openApp.jdMobile://", "aldnews://news", "maopp://mapgo", "edspay://edspay"};
    private AlaWebChromeClient alaWebChromeClient;
    private ProtocolData data;
    private FormInjectHelper formInjectHelper;
    private LinearLayout rootView;
    private AlaWebView webView;
    private View errorNetworkView;
    private ProgressBar progressBar;
    private TextView titleView;
    private String loadURL;
    private String pageName;
    private AlaProtocolHandler protocol;
    private String getTitle;//页面标题
    private boolean isError;
    private boolean notifyStatus;

    @Override
    public String getStatName() {
        return "H5页面";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fw__html5);
        rootView = findViewById(R.id.root);
        webView = findViewById(R.id.web_view);
        errorNetworkView = findViewById(R.id.ll_error_network);
        progressBar = findViewById(R.id.webview_progress);
        titleView = findViewById(R.id.top_title);
        pageName = getIntent().getStringExtra(INTENT_DEFAULT_TITLE);
        initData();
        updateParamsFromUrl(loadURL);
        initUI();
    }

    public void showRightOption(OnClickListener listener) {
        if (findViewById(R.id.btn_browser_more) != null) {
            findViewById(R.id.btn_browser_more).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_browser_more).setOnClickListener(listener);
        }
    }

    public void hiddenRightOption() {
        if (findViewById(R.id.btn_browser_more) != null) {
            findViewById(R.id.btn_browser_more).setVisibility(View.INVISIBLE);
        }
        if (findViewById(R.id.tv_browser_more) != null) {
            findViewById(R.id.tv_browser_more).setVisibility(View.INVISIBLE);
        }
        if (findViewById(R.id.btn_browser_more_two) != null) {
            findViewById(R.id.btn_browser_more_two).setVisibility(View.INVISIBLE);
        }
    }

    public void showRightOption(int resId, OnClickListener listener) {
        if (findViewById(R.id.btn_browser_more) != null) {
            findViewById(R.id.btn_browser_more).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.btn_browser_more)).setImageResource(resId);
            findViewById(R.id.btn_browser_more).setOnClickListener(listener);
        }
        if (findViewById(R.id.tv_browser_more) != null) {
            findViewById(R.id.tv_browser_more).setVisibility(View.INVISIBLE);
        }
        if (findViewById(R.id.btn_browser_more_two) != null) {
            findViewById(R.id.btn_browser_more_two).setVisibility(View.INVISIBLE);
        }

    }

    public void showRightTextOption(String text, OnClickListener listener) {
        if (findViewById(R.id.tv_browser_more) != null) {
            findViewById(R.id.tv_browser_more).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_browser_more)).setText(text);
            findViewById(R.id.tv_browser_more).setOnClickListener(listener);
        }
        if (findViewById(R.id.btn_browser_more) != null) {
            findViewById(R.id.btn_browser_more).setVisibility(View.INVISIBLE);
        }
        if (findViewById(R.id.btn_browser_more_two) != null) {
            findViewById(R.id.btn_browser_more_two).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置字体颜色
     *
     * @param textColor 字体颜色
     */
    public void setRightTextColor(int textColor) {
        if (textColor != 0) {
            if (findViewById(R.id.tv_browser_more) != null) {
                TextView textView = findViewById(R.id.tv_browser_more);
                textView.setTextColor(textColor);
            }
        }
    }

    public void showRightOptionTwo(int resId, OnClickListener listener) {
        if (findViewById(R.id.btn_browser_more_two) != null) {
            findViewById(R.id.btn_browser_more_two).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.btn_browser_more_two)).setImageResource(resId);
            findViewById(R.id.btn_browser_more_two).setOnClickListener(listener);
        }
        if (findViewById(R.id.tv_browser_more) != null) {
            findViewById(R.id.tv_browser_more).setVisibility(View.INVISIBLE);
        }
    }

    public void hideTitleBar() {
        if (findViewById(R.id.top_panel) != null) {
            findViewById(R.id.top_panel).setVisibility(View.GONE);
        }
    }

    public void showTitleBar() {
        if (findViewById(R.id.top_panel) != null) {
            findViewById(R.id.top_panel).setVisibility(View.VISIBLE);
        }
    }


    private Uri updateParamsFromUrl(String url) {
        if (MiscUtils.isEmptyOrLiterallyNull(url)) {
            return null;
        }
        try {
            Uri uri = Uri.parse(url);
            if (uri == null) {
                return null;
            }
            initHardware(uri);
            initOrientation(uri);
            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initHardware(Uri uri) {
        String param = uri.getQueryParameter(HARDWARE);
        boolean hardware = lastHardwareStatus;
        if (MiscUtils.isNotEmpty(param)) {
            hardware = Boolean.parseBoolean(param);
            lastHardwareStatus = hardware;
        }
        if (hardware) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }


    private void initOrientation(Uri uri) {
        //先获取新版的屏幕方向的参数，如果没有则尝试获取老版的参数，最后做相应处理
        String param = uri.getQueryParameter(ORIENTATION_NEW);
        if (MiscUtils.isNotEmpty(param)) {
            if (ORIENTATION_VERTICAL.equals(param)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (ORIENTATION_HORIZONTAL.equals(param)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (ORIENTATION_AUTO.equals(param)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        formInjectHelper = new FormInjectHelper();
        formInjectHelper.init();
        protocol = new AlaProtocolHandler();
        loadURL = getIntent().getStringExtra(INTENT_BASE_URL);
        //url去空格操作
        if (loadURL == null) {
            UIUtils.showToast("url异常");
            return;
        }
        loadURL = loadURL.trim();
        getTitle = getIntent().getStringExtra(INTENT_TITLE);
        while (loadURL.startsWith("　")) {
            loadURL = loadURL.substring(1, loadURL.length()).trim();
        }
        while (loadURL.endsWith("　")) {
            loadURL = loadURL.substring(0, loadURL.length() - 1).trim();
        }
        //如果不是文件协议(或者使用的人说不加)才加后面一坨，否则不加
        loadBuilder = addAppInfo(loadURL);
        formInjectHelper.setLoadURL(loadURL);
        offLine = MiscUtils.getSharepreferenceValue(SHARE_NAME, loadURL, true);
        netFirst = MiscUtils.getSharepreferenceValue(SHARE_NAME, loadURL, true);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initUI() {
        findViewById(R.id.top_panel).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_browser_back).setOnClickListener(this);
        findViewById(R.id.btn_browser_close).setOnClickListener(this);
        findViewById(R.id.tv_error_retry).setOnClickListener(this);
        if (!MiscUtils.isEmptyOrLiterallyNull(pageName)) {
            updateTitle(pageName);
        }
        /*
         * 初始化进度条
         */
        initProgressBar();
        webView.addWebJS(netFirst);
        webView.addJavascriptInterface(formInjectHelper, "alaInject");
        data = new ProtocolData();
        data.webView = webView;
        data.shareName = SHARE_NAME;
        data.showTitleBar = true;
        data.stateMap = new HashMap<>();
        data.webUrl = loadBuilder == null ? "" : loadBuilder.toString();
        webView.setProtocolData(data);
        alaWebChromeClient = getAlaWebChromeClient(data);
        webView.setWebChromeClient(alaWebChromeClient);
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> MiscUtils.goToSite(HTML5WebView.this, url));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Logger.i(TAG, "onPageStarted url" + url);
                overrideUrlLoading(view, url);
                if (!offLine || showProgress) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Logger.i(TAG, "onLoadResource url=" + url);
                formInjectHelper.doInjectIfNeed(url, "");
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);
                onWebViewFinished(view, url);
                Logger.i(TAG, "onPageFinished url=" + url);
                //这里用完全匹配，因为任何本地网页都是file:///开头的，只用file:///匹配会拦截所有本地网页
                if (LOCAL_ERROR_PAGE_URL.equals(url)) {
                    webViewReceivedError = true;
                } else if (webViewReceivedError) {
                    webViewReceivedError = false;
                    view.clearHistory();
                }
                if (MiscUtils.isSimpleEmpty(getTitle)) {
                    updateTitle(view.getTitle());
                } else {
                    updateTitle(getTitle);
                }
                if (!offLine || showProgress) {
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
                //返回true不需要在当前webView中处理,表示事件已经消耗了
                Log.d(TAG, "shouldOverrideUrlLoading: " + url);
                //公信宝开始
                //判断url是不是native://save_img?img=<图片地址>的格式来判断
                /*if(url.contains("save_img")){
                    //保存图片
                }*/
                for (String s : whiteList) {
                    //判断url如果是在Schemes的白名单里，就启动对于的app，如果不是直接加载url
                    if (url.startsWith(s)) {
                        try {
                            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            startActivity(intent);
                        } catch (Exception e) {
                            //让h5跳下载链接
                            e.printStackTrace();
                        } finally {
                            return true;
                        }
                    }
                }
                url = addAppInfo(url).toString();
                Uri uri = updateParamsFromUrl(url);
                if (uri != null && ((uri.getHost() != null && uri.getHost().contains(AlaProtocol.ROOT_H5_HOST))
                        || (uri.getHost() != null && uri.getHost().contains(AlaProtocol.ROOT_IP)))) {
                    if (interceptFanBeiUrl(url)) return true;
                }
                if (handleAlaProtocol(url)) {
                    notifyStatus = url.contains("OPEN_NOTIFICATION");
                    return true;
                } else if (url.startsWith("tel:")) {
                    String mobile = url.substring(url.lastIndexOf("/") + 1);
                    Uri telUri = Uri.parse("tel:" + mobile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, telUri);
                    startActivity(intent);
                    //返回true不需要在当前webView中处理
                    return true;
                } else if (url.startsWith("sms:")) {
                    String mobile = url.substring(url.lastIndexOf("/") + 1);
                    Uri smsUri = Uri.parse("sms:" + mobile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                    startActivity(intent);
                    return true;
                } else if (url.contains(AlaProtocol.Protocol.GXB_BACK)) {
                    data.alaUri = Uri.parse(url);
                    protocol.handleProtocol(AlaProtocol.Protocol.GXB_BACK, data);
                    return true;
                }
                //如果不是文件协议(或者使用的人说不加)才加后面一坨，否则不加
                if (loadBuilder != null) {
                    Uri uri1 = Uri.parse(loadBuilder.toString());
                    if ((uri1.getHost().contains(AlaProtocol.ROOT_H5_HOST) || uri1.getHost().contains(AlaProtocol.ROOT_IP))) {
                        //重新load,让后台捕捉appInfo
                        webView.loadUrl(url);
                    }
                }
                return super.shouldOverrideUrlLoading(webView, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed(); // 接受网站证书
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isError = true;
                if (webView != null) {
                    loadBlank();
                    webView.setVisibility(View.GONE);
                }
//                if (errorNetworkView != null) {
//                    errorNetworkView.setVisibility(View.VISIBLE);
//                }
            }

        });
        if (loadBuilder != null)
            webView.loadUrl(loadBuilder.toString());
    }

    public void onWebViewFinished(WebView view, String url) {
    }

    private boolean interceptFanBeiUrl(String url) {
        if (url.contains(AlaProtocol.Protocol.WUYAO_FUND_STATUS)) {
            data.alaUri = Uri.parse(url);
            protocol.handleProtocol(AlaProtocol.Protocol.WUYAO_FUND_STATUS, data);
            return true;
        }
        return false;
    }

    private void loadBlank() {
        if (webView != null) {
            String data = " ";
            webView.loadUrl("javascript:document.body.innerText=\"" + data + "\"");
        }
    }


    /**
     * 重定向URL
     */
    protected boolean overrideUrlLoading(WebView view, String url) {
        url = addAppInfo(url).toString();
        if (handleAlaProtocol(url)) {
            return true;
        } else if (url.startsWith("tel:")) {
            String mobile = url.substring(url.lastIndexOf("/") + 1);
            Uri telUri = Uri.parse("tel:" + mobile);
            Intent intent = new Intent(Intent.ACTION_VIEW, telUri);
            startActivity(intent);
            //返回true不需要在当前webView中处理
            return true;
        }
        //公信宝开始
        for (String s : whiteList) {
            //判断url如果是在Schemes的白名单里，就启动对于的app，如果不是直接加载url
            if (url.startsWith(s)) {
                try {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    startActivity(intent);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //公信宝结束
        return false;
    }


    private boolean handleAlaProtocol(String url) {
        Uri uri = Uri.parse(url);
        if (url.contains(AlaProtocol.Protocol.ZMXY_KEY)) {
            data.alaUri = uri;
            protocol.handleProtocol(AlaProtocol.Protocol.ZMXY_KEY, data);
            return true;
        } else if (url.contains(AlaProtocol.Protocol.MOBILE_SUCCESS_KEY)) {
            //手机运营商提交数据
            data.alaUri = uri;
            protocol.handleProtocol(AlaProtocol.GROUP_MOBILE_OPERATOR, data);
            return false;
        } else if (url.contains(AlaProtocol.Protocol.MOBILE_BACK_KEY)) {
            //手机运营商提交后的返回
            data.alaUri = uri;
            protocol.handleProtocol(AlaProtocol.GROUP_MOBILE_OPERATOR, data);
            return true;
        } else {
            if (!url.startsWith("file:")) {
                if (url.contains(AlaProtocol.ROOT_WEB_PATH)) {
                    data.alaUri = uri;
                    String resultUrl = protocol.handleProtocol(AlaProtocol.GROUP_SELF, data);
                    return MiscUtils.isEmpty(resultUrl);
                } else //  使用自定义界面启动器来尝试启动activity
                    return url.contains(AlaProtocol.ROOT_WEB_PATH);
            } else {
                return false;
            }
        }
    }

    /**
     * AlaWebChromeClient
     */
    private AlaWebChromeClient getAlaWebChromeClient(ProtocolData data) {
        return new AlaWebChromeClient(data, progressBar) {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                boolean isAppAlert = onJsAlertResult(view, url, message, result);
                return isAppAlert || super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      JsPromptResult promptResult) {
                if (MiscUtils.isNotEmpty(defaultValue)) {
                    Uri uri = Uri.parse(defaultValue);
                    if (AlaProtocol.Protocol4Js.CLOSE.equals(uri.getPath())) {
                        promptResult.confirm("");
                        finish();
                        return true;
                    }
                }
                return super.onJsPrompt(view, url, message, defaultValue, promptResult);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (MiscUtils.isEmptyOrLiterallyNull(HTML5WebView.this.pageName)) {
                    updateTitle(title);
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                startSelectFiles(filePathCallback, fileChooserParams);
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
                if ("image/*".equals(acceptType) || acceptType.startsWith("image/")) {
                    startSelectFile(uploadFile);
                } else {
                    uploadFile.onReceiveValue(null);
                }
            }

            public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
                if ("image/*".equals(acceptType) || acceptType.startsWith("image/")) {
                    startSelectFile(uploadFile);
                } else {
                    uploadFile.onReceiveValue(null);
                }
            }

            public void openFileChooser(ValueCallback<Uri> uploadFile) {
                startSelectFile(uploadFile);
            }

            private void startSelectFile(ValueCallback<Uri> uploadFile) {
                HTML5WebView.this.uploadFile = uploadFile;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PICTURE_REQUEST_CODE);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            private void startSelectFiles(ValueCallback<Uri[]> uploadFiles, FileChooserParams fileChooserParams) {
                if (HTML5WebView.this.uploadFiles != null) {
                    HTML5WebView.this.uploadFiles.onReceiveValue(null);
                    HTML5WebView.this.uploadFiles = null;
                }
                HTML5WebView.this.uploadFiles = uploadFiles;
                Intent intent = fileChooserParams.createIntent();
                startActivityForResult(intent, SELECT_PICTURES_REQUEST_CODE);
            }
        };
    }

    /**
     * js交互实现
     */
    protected boolean onJsAlertResult(WebView view, String url, String message, JsResult result) {
        return false;
    }

    /**
     * 返回
     * ios的goBack=false为关闭页面,所以要注意协商,后台可配的只能让h5再次重定向,不用后台可配的h5可区分开android ios
     */
    private void doBack() {
        if (webView.canGoBack() && !webViewReceivedError && !webView.getUrl().contains("goBack=false")) {
            if (findViewById(R.id.btn_browser_close).getVisibility() != View.VISIBLE) {
                findViewById(R.id.btn_browser_close).setVisibility(View.VISIBLE);
            }
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.goBack();
        } else {
            if (!TextUtils.isEmpty(webView.getUrl())) {
                //如果当前页面是支付结果页，则同时关闭订单详情页
                if (webView.getUrl().contains("/h5/asj/asjBrand/payResult.html?javaurl=ctestapp.51fanbei.com&port=80&httptype=http")) {
                    try {
                        Class clazz = Class.forName("com.alfl.www.user.ui.OrderDetailActivity");
                        ActivityUtils.finish(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            setResult(RESULT_OK);
            ActivityUtils.pop(this, intent);
        }
    }

    /**
     * 初始化进度条
     */

    private void initProgressBar() {
        if (offLine) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新title标题
     */
    private void updateTitle(String titleContent) {
        if (MiscUtils.isSimpleEmpty(titleContent)) {
            return;
        }
        titleView.setText(titleContent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
            if (uploadFile != null) {
                uploadFile.onReceiveValue((resultCode != RESULT_OK || data == null) ? null : data.getData());
            }
        } else if (requestCode == SELECT_PICTURES_REQUEST_CODE) {
            if (uploadFiles != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    uploadFiles.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    uploadFiles = null;
                }
            }
        } else if (requestCode == 0x0101) {
            //h5跳转扫描页面，扫描结果解析 BundleKey.REQUEST_CODE_MIAN_SCAN=0x0101
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                String content = bundle.getString("result_content");
                if (MiscUtils.isNotEmpty(content)) {
                    Intent intent = new Intent();
                    Activity context = AlaConfig.getCurrentActivity();
                    if (context == null)
                        context = ActivityUtils.peek();
                    try {
                        String decodeContent = BASE64Encoder.decodeString(content);
                        JSONObject jsonObject = JSONObject.parseObject(decodeContent);
                        String decodeType = jsonObject.getString("type");
                        String tradeUrl = jsonObject.getString("url");

                        if ("TRADE".equals(decodeType)) {
                            String host = AlaConfig.getServerProvider().getAppServer();
                            String url = host.substring(0, host.length()) + tradeUrl;
                            intent.putExtra(HTML5WebView.INTENT_BASE_URL, url);
                            ActivityUtils.push(context, HTML5WebView.class, intent);
                        } else {
                            intent.putExtra(HTML5WebView.INTENT_BASE_URL, tradeUrl);
                            ActivityUtils.push(context, HTML5WebView.class, intent);
                        }
                    } catch (Exception ex) {
                        UIUtils.showToast("商家信息获取失败，请重新扫描");
                        if (PermissionCheck.getInstance().checkPermission(context, Manifest.permission.CAMERA)) {
                            Class clazz = null;
                            try {
                                clazz = Class.forName("com.alfl.www.main.ui.QRCodeScanActivity");
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            Intent intentScan = new Intent(context, clazz);
                            ActivityUtils.push(clazz, intentScan, 0x0101);
                        } else {
                            PermissionCheck.getInstance()
                                    .askForPermissions(context, new String[]{Manifest.permission.CAMERA},
                                            REQUEST_CODE_ALL);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            if (notifyStatus)
                webView.loadUrl("javascript:openRemind(" + UIUtils.getNoticeStatus() + ")");
        }
        if (uploadFile != null) {
            uploadFile = null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (webView != null) {
            webView.loadUrl("javascript:viewWillAppear()");
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    public void destroyWebView() {
        rootView.removeAllViews();
        if (webView != null) {
            webView.loadUrl("javascript:viewWillDisappear()");
            webView.stopLoading();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
            rootView = null;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.btn_browser_back) {
            doBack();
            return;
        }
        if (id == R.id.btn_browser_close) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (id == R.id.tv_error_retry) {
            if (webView != null) {
                loadBlank();
                webView.setVisibility(View.VISIBLE);
            }
            refreshWebView();
            if (errorNetworkView != null) {
                errorNetworkView.setVisibility(View.GONE);
            }
        }
    }

    public AlaWebView getWebView() {
        return webView;
    }

    public AlaWebChromeClient getAlaWebChromeClient() {
        return alaWebChromeClient;
    }


    /**
     * 刷新h5
     */
    public void refreshWebView() {
        isError = false;
        String url = webView.getUrl();
        if (MiscUtils.isNotEmpty(url)) {
            if (url.contains("refreshUrl=false") ) {
                return;
            }
            String cutUrl = clearAppInfo(url);
            //如果不是文件协议(或者使用的人说不加)才加后面一坨，否则不加
            loadBuilder = addAppInfo(cutUrl);
            webView.loadUrl(loadBuilder.toString());
            webView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (webView != null) {
                        webView.clearHistory();
                        updateTitle(webView.getTitle());
                    }
                }
            }, 1000);
        }
    }

    /**
     * url清除appInfo
     */
    public static String clearAppInfo(String url) {
        String cutUrl = "";
        if (MiscUtils.isNotEmpty(url)) {
            if (url.contains("?_appInfo")) cutUrl = url.substring(0, url.indexOf("?_appInfo"));
            else if (url.contains("&_appInfo")) cutUrl = url.substring(0, url.indexOf("&_appInfo"));
            else cutUrl = url;
        }
        return cutUrl;
    }

    private StringBuilder addAppInfo(String url) {
        StringBuilder sb = new StringBuilder(url);
        Uri uri = updateParamsFromUrl(url);
        if (uri != null && uri.getHost() != null && ((uri.getHost().contains(AlaProtocol.ROOT_H5_HOST))
                || (uri.getHost().contains(AlaProtocol.ROOT_IP)) || (uri.getHost().contains(AlaProtocol.ROOT_IP1)))) {
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