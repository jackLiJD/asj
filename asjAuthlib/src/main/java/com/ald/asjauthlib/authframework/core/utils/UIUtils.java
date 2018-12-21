package com.ald.asjauthlib.authframework.core.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/4
 * 描述：UI相关的工具类
 * 修订历史：
 */
public class UIUtils {

    /*   private static final long Interval = 3 * 1000;
       private static final SoftMap<String, Long> map = new SoftMap<>();*/
    private static Toast CURR_TOAST;

    public static synchronized void sendBroadcast(String action) {
        sendBroadcast(new Intent(action));
    }

    public static synchronized void sendBroadcast(Intent intent) {
        AlaConfig.getContext().sendBroadcast(intent);
    }

    /**
     * 通过此方法得到的ProgressDialog需要在activity中显示的dismiss掉，比方说在onStop里面。
     */
    public static ProgressDialog showWaiting(final Activity activity, final String message) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }

        if (onUiThread()) {
            return createWaitingAndShow(activity, message);
        } else {
            return (ProgressDialog) showDialogFromBackgroundThread(activity, new Callable<Dialog>() {
                @Override
                public Dialog call() throws Exception {
                    return createWaitingAndShow(activity, message);
                }
            });
        }
    }

    private static ProgressDialog createWaitingAndShow(Activity activity, String message) {
        ProgressDialog pd = new ProgressDialog(activity);
        pd.setCancelable(false);
        pd.setMessage(message);
        pd.show();
        return pd;
    }

    /**
     * 通过此方法得到的AlertDialog需要在activity中显示的dismiss掉，比方说在onStop里面。
     */
    public static AlertDialog showDialog(final Activity activity, final String title, final String message) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }

        if (onUiThread()) {
            return createDialogAndShow(activity, title, message);
        } else {
            return (AlertDialog) showDialogFromBackgroundThread(activity, new Callable<Dialog>() {
                @Override
                public Dialog call() throws Exception {
                    return createDialogAndShow(activity, title, message);
                }
            });
        }
    }

    private static AlertDialog createDialogAndShow(Activity activity, String title, String message) {
        Builder builder = new Builder(activity);
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("确定", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * 通过此方法得到的AlertDialog需要在activity中显示的dismiss掉，比方说在onStop里面。
     */
    public static AlertDialog showConfirm(final Activity activity, final String message,
                                          final OnClickListener okListener, final OnClickListener cancelListener) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }

        if (onUiThread()) {
            return createConfirmAndShow(activity, message, okListener, cancelListener);
        } else {
            return (AlertDialog) showDialogFromBackgroundThread(activity, new Callable<Dialog>() {
                @Override
                public Dialog call() throws Exception {
                    return createConfirmAndShow(activity, message, okListener, cancelListener);
                }
            });
        }
    }

    private static AlertDialog createConfirmAndShow(Activity activity, String message, OnClickListener okListener,
                                                    OnClickListener cancelListener) {
        Builder builder = new Builder(activity);
        builder.setTitle("确认").setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", okListener);
        builder.setNegativeButton("取消", cancelListener);
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * 通过异步转同步的方法显示对话框，并且返回对话框的引用，以方便关闭。
     */
    private static Dialog showDialogFromBackgroundThread(final Activity activity, final Callable<Dialog> callable) {
        final Dialog[] dialog = new Dialog[1];
        final CountDownLatch latch = new CountDownLatch(1);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialog[0] = callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return dialog[0];
    }

    public static boolean onUiThread() {
        return Thread.currentThread().equals(Looper.getMainLooper().getThread());
    }

    /**
     * 显示toast
     *
     * @param id
     */
    public static void showToast(int id) {
        Context context = ActivityUtils.peek();
        toast(context, context.getString(id));
    }

    /**
     * 显示toast
     */
    public static void showToast(String msg) {
        toast(ActivityUtils.peek(), msg);
    }


    public static void toast(final Context mContext, final String content) {
        if (mContext == null) return;
        if (TextUtils.isEmpty(content)) return;
        AlaConfig.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (CURR_TOAST == null)
                    CURR_TOAST = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
                else
                    CURR_TOAST.setText(content);
                CURR_TOAST.show();
            }
        });


    }

   /* public static void toast(final Context context, final String msg) {
        long preTime = 0;
        if (map.containsKey(msg)) {
            preTime = map.get(msg);
        }
        final long now = System.currentTimeMillis();
        if (now >= preTime + Interval) {
            if (CURR_TOAST != null) {
                CURR_TOAST.cancel();
            }
            if (context != null) {
                AlaConfig.postOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (CURR_TOAST == null) {
                            Toast toast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
                            toast.show();
                            CURR_TOAST = toast;
                        } else {
                            CURR_TOAST.setText(msg);
                            CURR_TOAST.show();
                        }
                        map.put(msg, now);
                    }
                });

            }
        }
    }*/


    /**
     * 产生shape类型的drawable
     */
    public static GradientDrawable getBackgroundDrawable(@ColorInt int solidColor, @ColorInt int strokeColor, int strokeWidth, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        if (0 != solidColor) drawable.setColor(solidColor);
        if (0 != strokeColor && -2 != strokeWidth) drawable.setStroke(strokeWidth, strokeColor);
        if (0 != radius) drawable.setCornerRadius(radius);
        return drawable;
    }

    public static GradientDrawable getBackgroundDrawable(@ColorInt int solidColor, @ColorInt int strokeColor, int strokeWidth, float[] radius) {
        GradientDrawable drawable = new GradientDrawable();
        if (0 != solidColor) drawable.setColor(solidColor);
        if (0 != strokeColor && -2 != strokeWidth) drawable.setStroke(strokeWidth, strokeColor);
        if (0 != radius.length) drawable.setCornerRadii(radius);
        return drawable;
    }

    /**
     * 判断应用消息通知是否开启
     */
    public static boolean getNoticeStatus() {
        if (ActivityUtils.peek() == null) return false;
        NotificationManagerCompat manager = NotificationManagerCompat.from(ActivityUtils.peek());
        return manager.areNotificationsEnabled();
    }

    /**
     * 引导用户去系统应用中查看消息通知
     */
    public static void openNoticeStatus() {
        Activity activity = ActivityUtils.peek();
        NotificationManagerCompat manager = NotificationManagerCompat.from(activity);
        if (!manager.areNotificationsEnabled()) {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(Uri.fromParts("package", activity.getPackageName(), null));
            activity.startActivity(i);
        }
    }


    /**
     * pop需要显示的位置
     */
    public static int[] calculatePopWindowPos(Context context, final View anchorView, final View popview, int gravity, int x, int y) {
        int windowPos[] = new int[2];
        int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        // 获取屏幕的高宽
        int screenWidth = AlaConfig.getResources().getDisplayMetrics().widthPixels;
        popview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        int popHeight = popview.getMeasuredHeight();
        int popWidth = popview.getMeasuredWidth();
        switch (gravity) {
            case CommonConstant.TOP:
                windowPos[0] = screenWidth - popWidth + x;
                windowPos[1] = anchorLoc[1] - popHeight + y;
                break;
            case CommonConstant.LEFT:
                windowPos[0] = anchorLoc[0] - popWidth + x;
                windowPos[1] = anchorLoc[1] + anchorView.getWidth() / 2 - popHeight / 2 + y;
        }

        return windowPos;
    }

}
