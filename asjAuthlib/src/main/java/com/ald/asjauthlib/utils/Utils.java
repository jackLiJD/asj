package com.ald.asjauthlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;

import com.ald.asjauthlib.auth.model.TypeJump;
import com.ald.asjauthlib.auth.ui.AllLimitActivity;
import com.ald.asjauthlib.auth.ui.BankCardAddIdActivity;
import com.ald.asjauthlib.auth.ui.CreditPromoteActivity;
import com.ald.asjauthlib.auth.ui.RRIdAuthActivity;
import com.ald.asjauthlib.web.HTML5WebView;
import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.UIUtils;
import com.moxie.client.MainActivity;

import java.util.List;

/*
 * Created by ywd on 2017/11/17.
 */

public class Utils {
    public static <T> boolean notEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    // 将px值转换为dip或dp值
    public static int px2dip(Context context, float pxValue) {
        final float scale = AlaConfig.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将dip或dp值转换为px值
    public static int dip2px(Context context, float dipValue) {
        final float scale = AlaConfig.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 将px值转换为sp值
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = AlaConfig.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值
    public static int sp2px(Context context, float spValue) {
        final float fontScale = AlaConfig.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    // 根据Unicode编码判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    // 判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static int colorValue(String colorStr, @ColorRes int defaultColor) {
        try {
            if (TextUtils.isEmpty(colorStr) || "#".equals(colorStr))
                return AlaConfig.getContext().getResources().getColor(defaultColor);
            colorStr = colorStr.trim();
            if (colorStr.charAt(0) != '#') colorStr = "#" + colorStr;
            return Color.parseColor(colorStr);
        } catch (Exception e) {

        }
        return defaultColor;
    }

    public static int colorIntValue(String colorStr, @ColorInt int defaultColor) {
        try {
            if (TextUtils.isEmpty(colorStr) || "#".equals(colorStr))
                return defaultColor;
            colorStr = colorStr.trim();
            if (colorStr.charAt(0) != '#') colorStr = "#" + colorStr;
            return Color.parseColor(colorStr);
        } catch (Exception e) {

        }
        return defaultColor;
    }

    public static Class<? extends Activity> loginClass;

    public static void jumpToLoginNoResult() {
        if(loginClass!=null) ActivityUtils.push(loginClass);
        else UIUtils.showToast("跳钱包登录啦");
    }

    /**
     * 记录从主页进入登录后返回的参数
     * @param intent
     */
    public static void jumpToLoginWithParams(Intent intent) {
//        ActivityUtils.push(GrayCodeLoginActivity.class, intent, BundleKeys.REQUEST_CODE_LOGIN);
    }

    private static void jumpToLogin(Intent intent) {
//        ActivityUtils.push(GrayCodeLoginActivity.class, intent, BundleKeys.REQUEST_CODE_LOGIN);
    }

    // 获取菠萝蜜url
    private static void getOTOUrl(String shopId, Context context) {
        JSONObject object = new JSONObject();
        object.put("shopId", shopId);
        requestOTOUrl(object, context);
    }


    // 请求菠萝蜜地址
    private static void requestOTOUrl(JSONObject object, Context context) {
//        Call<BrandUrlModel> call = RDClient.getService(MainApi.class).getBrandUrl(object);
//        NetworkUtil.showCutscenes(context, call);
//        call.enqueue(new RequestCallBack<BrandUrlModel>() {
//            @Override
//            public void onSuccess(Call<BrandUrlModel> call, Response<BrandUrlModel> response) {
//                jumpH5(response.body().getShopUrl());
//            }
//        });
    }


    public static void unifyJumpType(TypeJump typeJump, Context context) {
        if (null == typeJump) return;
        unifyJump(typeJump.getType(), typeJump.getContent(), context);
    }

    public static void unifyJump(String type, String content, Context context) {
        /*if (MiscUtils.isEqualsIgnoreCase(type, HomeToolsEnum.H5.getType()) || MiscUtils.isEqualsIgnoreCase(type, ModelEnum.HOME_H5.getModel())) {
            jumpH5AuthLogin(content);
        } else if (type.equals(HomeToolsEnum.OTO.getType()) || type.equals(ModelEnum.BRAND.getModel())) {
            if (AlaConfig.isLand()) {
                Utils.getOTOUrl(content, context);
            } else {
                Utils.jumpToLoginNoResult();
            }
        } else if (type.equals(HomeToolsEnum.BORROW.getType())) {
//            if (AlaConfig.isLand()) {
            Intent i = new Intent();
            i.putExtra(BundleKeys.CASH_LOAN_SHOW_BLD_INDEX, content);
            ActivityUtils.push(CashLoanActivity.class, i);
//            } else {
//                Utils.jumpToLoginNoResult();
//            }
        } else if (type.equals(HomeToolsEnum.GOODSSORT.getType())) {
            Intent intent = new Intent();
//            intent.putExtra(BundleKeys.MAIN_DATA_TAB, content);
            intent.putExtra(BundleKeys.MAIN_DATA_TAB, 1);
            ActivityUtils.push(MainActivity.class, intent);
        } else if (type.equals(ModelEnum.CATEGORY_ID.getModel())) {
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.CATEGORY_ID, content);
            ActivityUtils.push(ThirdLevelActivity.class, intent);
        } else if (type.equals(ModelEnum.COUPON_LIST.getModel())) { // 优惠券
            if (AlaConfig.isLand()) {
                ActivityUtils.push(MyTicketActivity.class);
            } else {
                Utils.jumpToLoginNoResult();
            }
        } else if (type.equals(ModelEnum.SEARCH_TAG.getModel())) {
            ActivityUtils.push(GoodsSearchActivity.class);
        } else if ((type.equals(HomeToolsEnum.NATIVE.getType()))) {
            jumpToNative(content.split("_"));
        } else if ((type.equals(ModelEnum.HOME_GOODS.getModel()))) {
            jumpGoodDetails(content, "");
        }else if((type.equals(HomeToolsEnum.CUSTOMER_SERVICE.getType()))){
            jumpCustomegService(context);
        }*/
    }


    public static void jumpH5AuthLogin(String baseUrl) {
        Uri uri = Uri.parse(baseUrl);
        String isNeedLogin = uri.getQueryParameter("isNeedLogin");
        if ("true".equals(isNeedLogin) && !AlaConfig.isLand()) {
            Intent intent = new Intent();
            intent.putExtra(BundleKeys.CONTENT, baseUrl);
            Utils.jumpToLogin(intent);
        } else {
            jumpH5(baseUrl);
        }
    }

    public static void jumpH5(String baseUrl) {
        Intent intent = new Intent();
        intent.putExtra(HTML5WebView.INTENT_BASE_URL, baseUrl);
        ActivityUtils.push(HTML5WebView.class, intent);
    }

    /**
     * @author Yangyang
     * @desc 拼接写死的H5参数, 后加的请按顺序传进来
     */
    public static void jumpH5byPinH5(String baseUrl, Object... parmas) {
        Intent intent = new Intent();
        String result = genH5Url(baseUrl, parmas);
        intent.putExtra(HTML5WebView.INTENT_BASE_URL, result);
        ActivityUtils.push(HTML5WebView.class, intent);
    }

    /**
     * @author Yangyang
     * @desc 拼接写死的H5参数, 后加的请按顺序传进来, 带requestCode
     */
    public static void jumpH5byPinH5(int requestCode, String baseUrl, Object... parmas) {
        Intent intent = new Intent();
        String result = genH5Url(baseUrl, parmas);
        intent.putExtra(HTML5WebView.INTENT_BASE_URL, result);
        ActivityUtils.push(HTML5WebView.class, intent, requestCode);
    }

    /**
     * @author Yangyang
     * @desc 拼接写死的H5参数, 后加的请按顺序传进来, 带requestCode, 暂时支持追加3参数
     */
    public static String genH5Url(String baseUrl, Object... parmas) {
        String h5Url = AlaConfig.getServerProvider().getH5Server() + baseUrl;
        String appUrl = AlaConfig.getServerProvider().getAppServer();
        String[] url = appUrl.split(":");
        String httpType = url[0];
        String javaUrl = url[1].substring(2, url[1].substring(url[1].length() - 1).equals("/") ? url[1].length() - 1 : url[1].length());
        String port = "";
        if (url.length == 3) port = url[2].substring(0, url[2].length() - 1);
        StringBuilder result;
        switch (parmas.length) {
            case 1:
                result = new StringBuilder(String.format(h5Url, javaUrl, httpType, parmas[0]));
                break;
            case 2:
                result = new StringBuilder(String.format(h5Url, javaUrl, httpType, parmas[0], parmas[1]));
                break;
            case 3:
                result = new StringBuilder(String.format(h5Url, javaUrl, httpType, parmas[0], parmas[1], parmas[2]));
                break;
            default:
                result = new StringBuilder(String.format(h5Url, javaUrl, httpType));
        }
        if (!"".equals(port)) {
            result.append("&port=");
            result.append(port);
        }
        return result.toString();
    }

    // 原生无参跳转
    public static void jumpToNative(String... pageID) {
        if (AlaConfig.isLand()) {
            try {
                if (MainActivity.class.getName().equals(pageID[0])) {
                    String s = pageID[1];
                    int mainTab = Integer.valueOf(s);
                    Intent i = new Intent();
                    i.putExtra(BundleKeys.MAIN_DATA_TAB, mainTab);
                    ActivityUtils.push(MainActivity.class, i);
                } else {
                    if (pageID[0].equals("GG")) {
                        String s = pageID[1];
                        if (MiscUtils.isNotEmpty(s)) {
                                Class clazz = Class.forName(s);
                                ActivityUtils.push(clazz);

                        }
                    } else {
                        Class clazz = Class.forName(pageID[0]);
                        ActivityUtils.push(clazz);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            jumpToLoginNoResult();
        }
    }

    public static SpannableString strikethrough(String str, int start, int end) {
        SpannableString spannableString = new SpannableString(str);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan(); // 中划线
        spannableString.setSpan(strikethroughSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static void doAuth( String sceneStatus,String realName) {
        AllLimitActivity.refreshFlag = true;
        Intent intent = new Intent();
        intent.putExtra(BundleKeys.INTENT_KEY_CREDIT_PROMOTE_SCENE, BundleKeys.CREDIT_PROMOTE_SCENE_ONLINE);
        intent.putExtra(BundleKeys.STAGE_JUMP, StageJumpEnum.STAGE_LIMIT_AUTH.getModel());
        switch (sceneStatus) {
            case "1":
                ActivityUtils.push(RRIdAuthActivity.class, intent, BundleKeys.REQUEST_CODE_ALL_lIMITS);
                break;
            case "5":
//                状态为5 已完成人脸识别 未绑定银行卡 跳转银行卡页面
                intent.putExtra(BundleKeys.BANK_CARD_NAME, realName);
                ActivityUtils.push(BankCardAddIdActivity.class, intent, BundleKeys.REQUEST_CODE_ALL_lIMITS);
                break;
            default:
                //跳转信用页面
                ActivityUtils.push(CreditPromoteActivity.class, intent, BundleKeys.REQUEST_CODE_ALL_lIMITS);
                break;
        }
    }

}
