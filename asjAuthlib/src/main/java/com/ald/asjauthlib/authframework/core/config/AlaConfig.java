package com.ald.asjauthlib.authframework.core.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;

import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.network.RDClient;
import com.ald.asjauthlib.authframework.core.network.interceptor.BasicParamsInterceptor;
import com.ald.asjauthlib.authframework.core.utils.SPUtil;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/3
 * 描述：
 * 此类必须在程序刚刚启动的时候注册，这样在程序的
 * 运行中就可以通过此类来获取全局的Application了
 * 最好的实现是自定义一个Application的子类，然后在此子类
 * 的初始化中注册。
 * 修订历史：
 */
public class AlaConfig {
    public static final String ACTION_API_OPEN = "com.authframework.core.api.err.open";
    public static final String EXTRA_ERR_CODE = "__extra__api_err_code__";
    public static final String EXTRA_ERR_MSG = "__extra__api_err_msg__";
    private static final String ALA_CORE_SHARED_PREFERENCE_DATA = "_sp.fw.core.config_";
    private static WeakReference<Activity> currentActivity;// 当前正在显示的Activity
    private static boolean debug = true;//是否在调试模式下
    private static Application application;// 必须需要显式设置
    // 系统全局的线程池，Application启动的时候创建，不需要销毁
    private static ExecutorService es;
    private static Handler handler;// 主线程的handler，用于方便post一些事情做主线程去做
    private static ActivityLeavedLongListener activityLeavedLongListener;
    private static UserCityProvider userCityProvider;
    private static ServerProvider serverProvider;
    private static AccountProvider accountProvider;
    private static LocalBroadcastManager localBroadcastManager;
    private static LinkedBlockingQueue<String> blockQueue;//阻塞队列
    /**
     * 用户是否登录
     */
    private static boolean isLand = false;

    //审核状态
    private static boolean isRevView = false;
    private static boolean isShowTips = true;
    private static boolean isFirstEntrance = true;

    public static void init(Application application) {
        localBroadcastManager = LocalBroadcastManager.getInstance(application);
        // 首先是生成线程池，最多10个线程,最少1个，闲置1分钟后线程退出
        es = Executors.newFixedThreadPool(10);
        AlaConfig.application = application;
        // 调用此方法触发保存的动作
        getFirstLaunchTime();
        // 用于监听APP是否到后台
//        watcher = new GestureLockWatcher(application);
//        watcher.setOnScreenPressedListener(() -> {
//        });
//        watcher.startWatch();
        AlaConfig.readRevState();
        handler = new Handler(Looper.getMainLooper());
    }



    public static Activity getCurrentActivity() {
        return currentActivity != null ? currentActivity.get() : null;
    }

    public static void setCurrentActivity(Activity activity) {
        currentActivity = new WeakReference<>(activity);
    }

    public static LocalBroadcastManager getLocalBroadcastManager() {
        return localBroadcastManager;
    }

    public static <T> Future<T> submit(Callable<T> call) {
        return es.submit(call);
    }

    public static void postOnUiThread(Runnable task) {
        handler.post(task);
    }

    public static void postDelayOnUiThread(Runnable task, long delay) {
        handler.postDelayed(task, delay);
    }

    public static void execute(Runnable task) {
        es.execute(task);
    }

    public static void removeCallbacks(Runnable task) {
        handler.removeCallbacks(task);
    }


    public static int getLaunchCount() {
        SharedPreferences prefs = application.getSharedPreferences(ALA_CORE_SHARED_PREFERENCE_DATA, Application.MODE_PRIVATE);
        return prefs.getInt("lc", 0);
    }




    /**
     * 获取第一次启动时间，此方法永远不会返回null
     * ，返回的格式是 yyyy-MM-dd HH:mm:ss
     */
    public static String getFirstLaunchTime() {
        SharedPreferences prefs = application.getSharedPreferences(ALA_CORE_SHARED_PREFERENCE_DATA, Application.MODE_PRIVATE);
        String firstTime = prefs.getString("ft", "");
        if (MiscUtils.isEmpty(firstTime)) {
            firstTime = MiscUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            Editor editor = prefs.edit();
            editor.putString("ft", firstTime);
            editor.commit();
        }
        return firstTime;
    }


    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        AlaConfig.debug = debug;
    }

    public static String getPackageName() {
        Context context = getContext();
        if (context != null) {
            return context.getPackageName();
        }
        return null;
    }

    public static Application getContext() {
        return application;
    }

    public static Resources getResources() {
        return application.getResources();
    }



    public static void setUserCityProvider(UserCityProvider userCityProvider) {
        AlaConfig.userCityProvider = userCityProvider;
    }

    public static ServerProvider getServerProvider() {
        return serverProvider;
    }

    public static void setServerProvider(ServerProvider serverProvider) {
        AlaConfig.serverProvider = serverProvider;
    }

    public static AccountProvider getAccountProvider() {
        return accountProvider;
    }

    public static void setAccountProvider(AccountProvider accountProvider) {
        AlaConfig.accountProvider = accountProvider;
        RDClient.getInstance().updateRetrofit(new BasicParamsInterceptor.Builder().build());
    }

    /**
     * 更新登录状态
     * 必须在 SharedPreferences 之后
     * isLand  true 表示登录， false 表示未登录
     */
    public static void updateLand(boolean isLand) {
        setLand(isLand);
        RDClient.getInstance().updateRetrofit(new BasicParamsInterceptor.Builder().build());
    }

    public static boolean isLand() {
        return isLand;
    }

    public static void setLand(boolean land) {
        isLand = land;
    }

    public static void setIsRevView(boolean isRevView) {
        AlaConfig.isRevView = isRevView;
        SPUtil.setValue("isForAuth", isRevView);
    }
    public static void readRevState() {
        Object isForAuth = SPUtil.getValue("isForAuth");
        if (isForAuth != null) AlaConfig.isRevView = (boolean) isForAuth;
    }

    public static boolean isRevView() {
        return isRevView;
    }


}
