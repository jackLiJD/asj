package com.ald.asjauthlib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ald.asjauthlib.authframework.core.config.AlaConfig;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;

/**
 * Created by luckyliang on 2017/11/16.
 */

public class KeyboardUtil {
    /**
     * @param activity activity
     */
    public static void hideKeyBoard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("KeyboardUtil", "hideKeyBoard : error = " + e.getMessage());
        }
    }

    /**
     * @param context  context
     * @param editText editText
     */
    public static void hideKeyBoard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && editText.isFocusable()) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    /**
     * 显示键盘
     */
    public static void showKeyBoard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyBoardImplicit(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isKeyBoardShow(Activity context) {
        return context.getWindow().getAttributes().softInputMode == SOFT_INPUT_STATE_UNSPECIFIED;
    }

    public static int getStatusHeight(Activity activity) {
        int statusHeight;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height")
                        .get(localObject).toString());
                statusHeight = AlaConfig.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                Log.e(activity.getClass().getSimpleName(), "KeyboardUtil:getStatusHeight : error = " + e.getMessage());
            }
        }
        return statusHeight;
    }
}
