package com.ald.asjauthlib.authframework.core.protocol.webclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.protocol.AlaProtocol4JsHandler;
import com.ald.asjauthlib.authframework.core.protocol.data.ProtocolData;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;

import java.io.File;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/15 11:42
 * 描述：AlaWebChromeClient
 * 修订历史：
 */
public class AlaWebChromeClient extends WebChromeClient {
    private String Tag = "AlaWebChromeClient";
    private ProtocolData protocolData;
    private ProgressBar progressBar;
    private AlaProtocol4JsHandler protocol;


    /**
     * @param progressBar webview加载的进度条
     */
    public AlaWebChromeClient(ProtocolData protocolData, ProgressBar progressBar) {
        this.protocolData = protocolData;
        this.progressBar = progressBar;
        this.protocol = new AlaProtocol4JsHandler();
    }

    @Override
    public boolean onJsPrompt(final WebView view, String url, String message, String defaultValue,
            JsPromptResult promptResult) {
        Uri defUri = null;
        if (MiscUtils.isNotEmpty(defaultValue)) {
            defUri = Uri.parse(defaultValue);
        } else if (MiscUtils.isNotEmpty(message)) {
            defUri = Uri.parse(message);
        }
        final Uri uri = defUri;
        Logger.d(Tag, "uri:----" + uri);
        protocolData.alaUri = uri;
        if ("invoke".equals(message)) {
            new Thread() {
                @Override
                public void run() {
                    final String result = handleAlaProtocol(protocolData);
                    AlaConfig.postOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (MiscUtils.isNotEmpty(result) && view != null) {
                                view.loadUrl("javascript:" + result);
                            }
                        }
                    });
                }
            }.start();
            promptResult.cancel();
        } else if ("execute".equals(message)) {
            String script = handleAlaProtocol(protocolData);
            promptResult.confirm(script);
        }
        return true;
    }

    public String handleAlaProtocol(ProtocolData protocolData) {
        String script = null;
        script = protocol.handleProtocol("", protocolData);
        return script;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(final String origin,
            final GeolocationPermissions.Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AlaConfig.getCurrentActivity());
        builder.setTitle("温馨提示");
        builder.setMessage(origin + "想要使用您的手机的位置信息");
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("允许", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.invoke(origin, true, true);
            }
        });
        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.invoke(origin, false, false);
            }
        });
        AlertDialog dialog1 = builder.create();
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.show();
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Logger.i("info", "newProgress: " + newProgress);
        if (progressBar != null) {
            progressBar.setProgress(newProgress);
        }
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(requiredStorage * 2);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize,
            long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
    }

    // 支持文件上传的方法，需要重写该方法
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        uploadFile.onReceiveValue(null);
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
        uploadFile.onReceiveValue(null);
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile) {
        uploadFile.onReceiveValue(null);
    }

    /**
     * 获取ziphtml类型消息的detail.html文件
     */
    public static File getZipDetailFile(Context context, String msgId) {
        File extractDir = getZipHtmlDir(context, msgId);
        File popupFile = new File(extractDir, "/htmzip/detail.html");
        return popupFile;
    }

    /**
     * 获取ziphtml类型消息解压后的文件夹
     *
     * @param msgId 消息的消息id
     */
    static File getZipHtmlDir(Context context, String msgId) {
        File zipHtmlDir = new File(getZipHtmlTopDir(context), msgId);
        return zipHtmlDir;
    }

    /**
     * 获取存放ziphtml类型消息的父文件夹
     */
    static File getZipHtmlTopDir(Context context) {
        File file = new File(context.getFilesDir(), "htmlZip");
        file.mkdirs();
        return file;
    }

}
