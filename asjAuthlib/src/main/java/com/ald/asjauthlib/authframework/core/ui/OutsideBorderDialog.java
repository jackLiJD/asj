package com.ald.asjauthlib.authframework.core.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

/**
 * Created by wjy on 2018/4/12.
 *
 * android的dialog实现触摸外部关闭时多加了ViewConfiguration.get(context).getScaledWindowTouchSlop()长度,
 * 所以不是你所设置的布局外面点击就会关闭dialog,重写去掉这部分长度
 */
public class OutsideBorderDialog extends Dialog {

    private boolean mCancelable = true;
    private DialogListener listener;

    public OutsideBorderDialog(@NonNull Context context) {
        this(context, 0);
    }

    public OutsideBorderDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        Window window = getWindow();

        if (window == null) return false;
        final View decorView = window.getDecorView();
        if (mCancelable && isShowing() && shouldCloseOnTouch(event, decorView)) {
            if (null != listener) listener.onCancelListener(this);
            return true;
        }
        return false;
    }

    private boolean shouldCloseOnTouch(MotionEvent event, View decorView) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        return (x <= 0) || (y <= 0)
                || (x > decorView.getWidth())
                || (y > decorView.getHeight())
                && event.getAction() == MotionEvent.ACTION_DOWN;
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable = flag;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        mCancelable = cancel;
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    public void cancelDialog() {
        if (null != listener) listener.onCancelListener(this);
    }

    public interface DialogListener {

        void onCancelListener(DialogInterface dialog);

    }
}
