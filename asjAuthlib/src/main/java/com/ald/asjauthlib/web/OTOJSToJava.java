package com.ald.asjauthlib.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.ald.asjauthlib.utils.Permissions;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.location.LocationUtils;
import com.ald.asjauthlib.authframework.core.utils.PermissionCheck;


/*
 * Created by sean yu on 2017/6/1.
 */

public class OTOJSToJava {

    private WebView webView;

    public OTOJSToJava(WebView webView) {
        this.webView = webView;
    }


    @JavascriptInterface
    public void requestLocation() {
        //定位权限
        PermissionCheck.getInstance().askForPermissions((Activity) webView.getContext(), Permissions.locationPermissions,
                PermissionCheck.REQUEST_CODE_ALL);
        AlaConfig.execute(new Runnable() {
            @Override
            public void run() {
                LocationUtils.requestLocation(100000L);
                if (LocationUtils.isLocationFailure()) {
                    return;
                }
                final String jsFunction = String.format(JavaToJS.OTO_RECEIVE_LOCATION,
                        LocationUtils.getCurrentLocation().getLongitude(),
                        LocationUtils.getCurrentLocation().getLatitude());
                if (webView != null) {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl(jsFunction);
                        }
                    });
                }
            }
        });
    }
}
