package com.ald.asjauthlib.authframework.core.config;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;


/**
 * Created by Jacky Yu on 2015/11/17.
 */
public class AlaActivityInterceptor {

    protected Activity activity;
    private StatNameProvider statNameProvider;
    private SystemBarTintManager tintManager;
    private ILeftCycleListener listener;

    public AlaActivityInterceptor(Activity activity, StatNameProvider statNameProvider) {
        this.activity = activity;
        this.statNameProvider = statNameProvider;
    }

    //沉浸式状态栏的兼容api19时
    public void tintMangerInterceptor() {
        tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintColor(ContextCompat.getColor(AlaConfig.getContext(), R.color.colorPrimary_auth));
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
    }

    public void onCreate(Bundle savedInstanceState) {
        ActivityUtils.push(activity);
        AlaConfig.setCurrentActivity(activity);
        //默认强制竖屏
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void setListener(ILeftCycleListener listener) {
        this.listener = listener;
    }

    public void onNewIntent(Intent intent) {
        AlaConfig.setCurrentActivity(activity);
    }

    public void onStart() {

    }

    public void onRestart() {
    }

    public void onResume() {
        AlaConfig.setCurrentActivity(activity);

        if (this.listener != null) {
            this.listener.onResume();
        }
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroy() {
        ActivityUtils.remove(activity);
        if (activity.equals(AlaConfig.getCurrentActivity())) {
            AlaConfig.setCurrentActivity(null);
        }

    }

    public void finish() {
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    /**
     * 生命周期回调监听
     */
    public interface ILeftCycleListener {
        void onResume();
    }
}
