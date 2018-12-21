package com.ald.asjauthlib.widget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.ald.asjauthlib.authframework.core.ui.AlaWebView;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;

/*
 * Created by sean yu on 2017/5/23.
 */

public class HTML5WebView extends AlaWebView {
    private static final String TAG = "HTML5WebView";
    public static final String ORIENTATION_VERTICAL = "portrait";
    public static final String ORIENTATION_HORIZONTAL = "landscape";

    private static final String ORIENTATION_AUTO = "auto";
    private static final String ORIENTATION = "orientation";//老版的屏幕显示方向的url参数
    private static final String ORIENTATION_NEW = "ala-web-orientation";//新版的屏幕显示方向的url参数
    private static final String HARDWARE = "ala-web-hardware";// 是否开启硬件加速，默认true
    public static final String LOCAL_ERROR_PAGE_URL = "file:///android_asset/fw/error_page/error.htm";

    private boolean lastHardwareStatus;

    private volatile boolean webViewReceivedError;

    public HTML5WebView(Context context) {
        super(context);
        initView();
    }

    public HTML5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        addWebJS(true);
    }


    private void updateParamsFromUrl(String url) {
        if (MiscUtils.isEmptyOrLiterallyNull(url)) {
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            if (uri == null) {
                return;
            }
            initHardware(uri);
            initOrientation(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHardware(Uri uri) {
        String param = uri.getQueryParameter(HARDWARE);
        boolean hardware = lastHardwareStatus;
        if (MiscUtils.isNotEmpty(param)) {
            hardware = Boolean.parseBoolean(param);
            lastHardwareStatus = hardware;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            if (hardware) {
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
        }
    }


    private void initOrientation(Uri uri) {
        if (getContext() instanceof Activity) {
            //先获取新版的屏幕方向的参数，如果没有则尝试获取老版的参数，最后做相应处理
            String param = uri.getQueryParameter(ORIENTATION_NEW);
            if (MiscUtils.isEmpty(param)) {
                param = uri.getQueryParameter(ORIENTATION);
            }
            if (MiscUtils.isNotEmpty(param)) {
                if (ORIENTATION_VERTICAL.equals(param)) {
                    ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (ORIENTATION_HORIZONTAL.equals(param)) {
                    ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (ORIENTATION_AUTO.equals(param)) {
                    ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            } else {
                ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }
}
