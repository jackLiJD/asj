package com.ald.asjauthlib.web;

import android.content.res.Resources;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/*
 * Created by sean yu on 2017/6/1.
 */

public class BannerPositionJSToJava {
    private BannerBox mBannerBox = new BannerBox();

    private WebView mWebView;

    public BannerPositionJSToJava(WebView webView) {
        this.mWebView = webView;
        this.mWebView.setOnTouchListener((v, motionEvent) -> {
            float y = motionEvent.getY();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if (mBannerBox.canTouch(y)) {
                        mWebView.getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        mWebView.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return mWebView.onTouchEvent(motionEvent);
        });
    }


    @JavascriptInterface
    public void getH5ViewPagerInfo(float x, float y, float width, float height) {
        mBannerBox.mTopY = y * Resources.getSystem().getDisplayMetrics().density;
        mBannerBox.mHeight = height * Resources.getSystem().getDisplayMetrics().density;
    }


    private static class BannerBox {
        float mTopY;
        float mHeight;

        boolean canTouch(float touchY) {
            if (mTopY < 0) {
                if (Math.abs(mTopY) >= mHeight) {
                    return false;
                }
                return mHeight - Math.abs(mTopY) > touchY;
            }
            return touchY > mTopY && touchY < mTopY + mHeight;
        }
    }
}
