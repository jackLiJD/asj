package com.ald.asjauthlib.authframework.core.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

import com.ald.asjauthlib.authframework.core.config.AlaConfig;

/**
 * Created by wjy on 2017/11/17.
 *
 * 尺寸适配类,按设计给出的标准来
 */

public class ScreenMatchUtils {

    public static int billYearWidth(Activity act) {
        Rect appRect = DensityUtils.getAppRect(act);
        return appRect.width() * 78 / 370;
    }

    public static int billMonthWidth(Activity act) {
        Rect appRect = DensityUtils.getAppRect(act);
        return appRect.width() * 292 / 370 / 5;
    }

    public static int hasBillsItemADWidth(Context context) {
        return (AlaConfig.getResources().getDisplayMetrics().widthPixels - DensityUtils.getPxByDip(17) * 2) * 170 / 375 + DensityUtils.dp2px(context, 10);
    }

    public static int hasBillsItemADHeight(int adWidth, Context context) {
        return adWidth * 110 / 179 + DensityUtils.dp2px(context, 10);
    }

    public static int notBillsItemADHeight(int adWidth) {
        return adWidth * 220 / 375;
    }

    public static int repayStatusADHeight(int adWidth, int paddingTop) {
        return adWidth * 90 / 375 + paddingTop;
    }

    public static int screenWidth() {
        return AlaConfig.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int halfScreenWidth() {
        int screenWidth = AlaConfig.getContext().getResources().getDisplayMetrics().widthPixels;
        return screenWidth / 2;
    }

    public static int oneThirdOfScreenWidth() {
        int screenWidth = AlaConfig.getContext().getResources().getDisplayMetrics().widthPixels;
        return screenWidth / 3;
    }

    public static int calculateSize(int width, int height) {
        int screenWidth = AlaConfig.getContext().getResources().getDisplayMetrics().widthPixels;
        return screenWidth * height / width;
    }
}
