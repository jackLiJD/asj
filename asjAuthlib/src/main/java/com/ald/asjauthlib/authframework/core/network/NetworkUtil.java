package com.ald.asjauthlib.authframework.core.network;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.ald.asjauthlib.authframework.core.ui.CutscenesProgress;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;
import com.ald.asjauthlib.authframework.core.config.AlaActivity;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;

import io.reactivex.disposables.Disposable;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/6
 * 描述：网络请求工具类
 * 修订历史：
 */
public class NetworkUtil {
    private static CutscenesProgress cutscenesProgress = null;
    /**
     * 请求返回后是否自动关闭
     */
    private static boolean automatic = true;

    /**
     * @param title      提示框的标题，如果不想要标题，传递null
     * @param content    提示框的提示内容，可传递null
     * @param cancelable 该dialog是否可以cancel
     */
    public static CutscenesProgress init(Context context, String title, String content, boolean cancelable, boolean isTrans, DialogInterface.OnCancelListener listener) {
        CutscenesProgress dialog = new CutscenesProgress(context, isTrans);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            dialog.setMessage(content);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(listener);
        return dialog;
    }

    public static void showCutscenes(Context context, String title, String content) {
        showCutscenes(context, title, content, true, true, null);
    }

    public static void showCutscenes(Context context, String title, String content, boolean cancelable, boolean automatic) {
        showCutscenes(context, title, content, cancelable, automatic, null);
    }

    /**
     * 取消显示的同时，取消网络请求
     *
     * @param call retrofit2网络请求
     */
    public static void showCutscenes(Context context, final retrofit2.Call call) {
        showCutscenes(context, null, null, true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (null != cutscenesProgress && cutscenesProgress.isShowing()) {// 隐藏加载框
                    cutscenesProgress.dialogDismiss();
                    cutscenesProgress = null;
                }
                if (null != call) {
                    call.cancel();
                }
            }
        });
    }

    /**
     * 取消显示的同时，取消网络请求
     *
     * @param disposable 网络请求
     */
    public static void showCutscenesByRx(Context context, final Disposable disposable) {
        showCutscenes(context, null, null, true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (null != cutscenesProgress && cutscenesProgress.isShowing()) {// 隐藏加载框
                    cutscenesProgress.dialogDismiss();
                    cutscenesProgress = null;
                }
                if (null != disposable && !disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
        });
    }

    /**
     * 取消显示的同时，取消网络请求
     *
     * @param call retrofit2网络请求
     *             isTransparent 加载背景是否透明
     */
    public static void showCutscene(Context context, final retrofit2.Call call, boolean isTransparent) {
        showCutscenes(context, null, null, true, true, isTransparent, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (null != cutscenesProgress && cutscenesProgress.isShowing()) {// 隐藏加载框
                    cutscenesProgress.dialogDismiss();
                    cutscenesProgress = null;
                }
                if (null != call) {
                    call.cancel();
                }
            }
        });
    }

    /**
     * 取消显示的同时，取消网络请求
     *
     * @param call retrofit2网络请求
     *             automatic 是否自动取消动画
     */
    public static void showCutscenes(Context context, final retrofit2.Call call, boolean automatic) {
        showCutscenes(context, null, null, true, automatic, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (null != cutscenesProgress && cutscenesProgress.isShowing()) {// 隐藏加载框
                    cutscenesProgress.dialogDismiss();
                    cutscenesProgress = null;
                }
                if (null != call) {
                    call.cancel();
                }
            }
        });
    }

    /**
     * 显示加载dialog
     *
     * @param title      标题
     * @param content    内容
     * @param cancelable 是否可以取消显示
     * @param listener   取消显示后的监听
     */
    public static void showCutscenes(Context context, String title, String content, boolean cancelable, boolean automatic, DialogInterface.OnCancelListener listener) {
        try {
            NetworkUtil.automatic = automatic;
            if (null != context) {
                if (null == cutscenesProgress) {
                    cutscenesProgress = init(context, title, content, cancelable, true, listener);
                }
                if (context instanceof AlaActivity) {
                    ((AlaActivity) context).setShowingDialog(cutscenesProgress);
                }
                cutscenesProgress.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showCutscenes(Context context, String title, String content, boolean cancelable, boolean automatic, boolean isTrans, DialogInterface.OnCancelListener listener) {
        try {
            NetworkUtil.automatic = automatic;
            if (null != context) {
                if (null == cutscenesProgress) {
                    cutscenesProgress = init(context, title, content, cancelable, isTrans, listener);
                }
                if (context instanceof AlaActivity) {
                    ((AlaActivity) context).setShowingDialog(cutscenesProgress);
                }
                cutscenesProgress.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载dialog
     *
     * @param title     标题
     * @param content   内容
     * @param canCancel 是否可以取消显示
     * @param listener  取消显示后的监听
     */
    @Deprecated
    public static void showCutscenes(String title, String content, boolean canCancel, DialogInterface.OnCancelListener listener) {
        Context context = ActivityUtils.peek();
        if (null != context) {
            if (null == cutscenesProgress) {
                cutscenesProgress = init(context, title, content, canCancel, true, listener);
            }
            cutscenesProgress.show();
        }
    }

    /**
     * 隐藏加载dialog
     */
    public static void hideCutscenes() {
        if (null != cutscenesProgress && cutscenesProgress.isShowing() && automatic) {
            cutscenesProgress.hide();
        }
    }

    /**
     * 释放加载dialog
     */
    public static void dismissCutscenes() {
        try {
            Logger.d("dismissCutscenes", "dismissCutscenes");
            if (null != cutscenesProgress && automatic && cutscenesProgress.isShowing()) {
                if (ActivityUtils.activityIsAlive(cutscenesProgress.getContext()))
                    cutscenesProgress.dialogDismiss();
                cutscenesProgress = null;
            }
        } catch (Exception e) {
        }

    }

    /**
     * 释放加载dialog
     */
    public static void dismissForceCutscenes() {
        Logger.d("dismissCutscenes", "dismissCutscenes");
        if (null != cutscenesProgress) {
            Logger.d("automatic", "dismissCutscenes");
            cutscenesProgress.dialogDismiss();
            cutscenesProgress = null;
        }
    }


    /**
     * 释放加载dialog
     */
    public static void dismissCutscenes(boolean automatic) {
        NetworkUtil.automatic = automatic;
        dismissCutscenes();
    }
}
