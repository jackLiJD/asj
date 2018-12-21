package com.ald.asjauthlib.authframework.core.utils;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.ald.asjauthlib.R;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.utils.log.DateFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/1/3
 * 描述：显示数据格式化
 * 修订历史：
 */
public class DisplayFormat {
    /**
     * 金额两位小数格式化
     *
     * @param formatArgs 待格式化数据
     * @param type       0 - 不足一万，则单位为“元”，否则单位为“万元”
     *                   1 - 单位均为“元”
     */
    public static String doubleMoney(Object formatArgs, int type) {
        if (formatArgs != null && !TextUtils.isEmpty(formatArgs.toString())) {
            String arg = formatArgs.toString();
            switch (type) {
                case 0:
                    try {
                        double money = Double.valueOf(arg);
                        if (money > 10000) {
                            return doubleFormat(money / 10000) + "万元";
                        } else {
                            return doubleFormat(money) + "元";
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return arg;
                    }

                case 1:
                default:
                    return doubleFormat(formatArgs) + "元";
            }
        } else {
            return "0.00元";
        }
    }

    /**
     * 金额两位小数格式化 - (type = 1)
     */
    public static String doubleMoney(Object formatArgs) {
        return doubleMoney(formatArgs, 1);
    }

    /**
     * 金额整数格式化
     *
     * @param formatArgs 待格式化数据
     * @param type       0 - 不足一万，则单位为“元”，否则单位为“万元”
     *                   1 - 单位均为“元”
     */
    public static String intMoney(Object formatArgs, int type) {
        if (formatArgs != null && !TextUtils.isEmpty(formatArgs.toString())) {
            String arg = formatArgs.toString();
            switch (type) {
                case 0:
                    try {
                        double money = Double.valueOf(arg);
                        if (money > 10000) {
                            return intFormat(money / 10000) + "万元";
                        } else {
                            return intFormat(money) + "元";
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return arg;
                    }

                case 1:
                default:
                    return intFormat(formatArgs) + "元";
            }
        } else {
            return "0元";
        }
    }

    /**
     * 金额整数格式化 - (type = 1)
     */
    public static String intMoney(Object formatArgs) {
        return intMoney(formatArgs, 1);
    }

    /**
     * String 转换为 long
     *
     * @param transfString
     * @return
     */
    public static long stringToLong(String transfString) {
        try {
            return Long.valueOf(transfString);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * String 转换为 double
     *
     * @param transfString
     * @return
     */
    public static double stringToDouble(String transfString) {
        try {
            return Double.parseDouble(reMoveComma(transfString));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Srring 2位小数转换成int
     */
    public static int StringToInt(String transfString) {
        try {
            double j = (Double.parseDouble(transfString) * 100);
            int i = (int) j;
            return i;
        } catch (Exception e) {
            return 0;
        }


    }

    /**
     * 当 String 为0时，返回true；
     */
    public static boolean string2Boolean(String value) {
        if (value.equals("0")) {
            return true;
        }
        return false;
    }

    /**
     * 清楚钱格式化中的,
     *
     * @param transfString
     * @return
     */
    public static String reMoveComma(String transfString) {
        return transfString.replace(",", "");
    }

    /**
     * 数值格式化 - 12,345.00
     */
    public static String doubleFormat(Object formatArgs) {
        if (formatArgs != null && !TextUtils.isEmpty(formatArgs.toString())) {
            String number = formatArgs.toString();
            try {
                DecimalFormat df = new DecimalFormat();
                if (Double.valueOf(number) < 1) {
                    df.applyPattern("0.00");
                } else {
                    df.applyPattern("##,###.00");
                }
                return df.format(Double.valueOf(number));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return number;
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0.00";
        }
    }


    /**
     * 数值格式化 - 12,345
     */
    public static String intFormat(Object formatArgs) {
        if (formatArgs != null && !TextUtils.isEmpty(formatArgs.toString())) {
            String number = formatArgs.toString();
            try {
                DecimalFormat df = new DecimalFormat();
                if (Double.valueOf(number) < 1) {
                    df.applyPattern("0");
                } else {
                    df.applyPattern("##,###.##");
                }
                return df.format(Double.valueOf(number));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return number;
            } catch (Exception e) {
                e.printStackTrace();
                return number;
            }
        } else {
            return "0";
        }
    }

    public static String doubleFormatMoney(double money, int type) {
        DecimalFormat df = new DecimalFormat();
        if (type == 1) {
            df.applyPattern("#.00");
        } else {
            df.applyPattern("#.##");
        }
        return df.format(money);
    }


    /**
     * 使用java正则表达式去掉多余的.与0
     */
    public static String subZeroAndDot(Object formatArgs) {
        String str = formatArgs.toString();
        if (str.indexOf(".") > 0) {
            // 去掉多余的0
            str = str.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            str = str.replaceAll("[.]$", "");
        }
        return str;
    }

    /**
     * 替换手机号4-7位为*号 / 保留用户名前后各一位，其它全为*
     */
    public static String getSecurityName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        String result;
        Pattern pattern = Pattern.compile("[0-9]*");
        if (pattern.matcher(name).matches()) {
            // 括号表示组，被替换的部分$n表示第n组的内容
            result = name.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else {
            result = name.substring(0, 1) + "***" + name.substring(name.length() - 1, name.length());
        }
        return result;
    }

    /**
     * 银行卡格式化
     *
     * @param cardNo 银行卡号,例如"6225880137706868"
     */
    public static String formBankCard(String cardNo) {
        return cardNo.replaceAll("([\\d]{4})(?=\\d)", "$1 ");
    }

    /**
     * 获得隐私保护银行账号
     */
    public static String getHideBankNO(String no) {
        return no.substring(0, 4) + " **** **** " + no.substring(no.length() - 4);
    }

    /**
     * 获得银行卡号后四位
     */
    public static String getShortBankNO(String no) {
        if (no == null) {
            return "";
        }
        if (no.length() < 4) {

            return no;
        } else {
            return no.substring(no.length() - 4);
        }
    }

    /**
     * 获得隐私保护邮箱号
     */
    public static String getHideEmail(String email) {
        String add = email.split("@")[0];
        return add.substring(0, 1) + "*****" + add.substring(add.length() - 1) + email.substring(email.indexOf("@"));

    }

    /**
     * 对params中数值部分进行颜色格式化
     */
    public static Spannable XLIFFNumFormat(String params, int color) {
        Matcher matcher = Pattern.compile("[0-9,.%]+").matcher(params);
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            Spannable span = new SpannableString(params);
            // 设置字体大小
            // span.setSpan(new AbsoluteSizeSpan(38), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 设置字体颜色
            span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            return new SpannableString(params);
        }
    }

    /**
     * 截取字符串中的数值部分
     */
    public static String substringNumFormat(String params) {
        Matcher matcher = Pattern.compile("[0-9,.%]+").matcher(params);
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            return params.substring(start, end);
        } else {
            return "0";
        }
    }

    /**
     * 对params中括号内进行颜色格式化
     */
    public static Spannable BracketFormat(String params, int color) {
        Matcher matcher = Pattern.compile("（(.*?)）").matcher(params);
        if (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            Spannable span = new SpannableString(params);
            // 设置字体大小
            // span.setSpan(new AbsoluteSizeSpan(38), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 设置字体颜色
            span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            return new SpannableString(params);
        }
    }

    /**
     * 对年化利率的小数点之后的格式化
     */
    public static Spannable XLIFFAprFormat(String params) {
        params = aprDisplayFormat(params) + "%";
        if (params.contains(".")) {
            int start = 0;
            int end = params.indexOf(".");
            Spannable span = new SpannableString(params);
            // 设置字体大小
            span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 30)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 设置字体颜色
            // span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            return new SpannableString(params);
        }
    }

    /**
     * 对年化利率的小数点之后的格式化,自定义字体大小
     */
    public static Spannable XLIFFAprFormat(String params, int size) {
        params = aprDisplayFormat(params) + "%";
        if (params.contains(".")) {
            int start = 0;
            int end = params.indexOf(".");
            Spannable span = new SpannableString(params);
            // 设置字体大小
            span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), size)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 设置字体颜色
            // span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else {
            return new SpannableString(params);
        }
    }

    /**
     * 格式化时间，满足印章格式
     */
    public static String stampTimeFormat(Object params) {
        String time = params == null ? "0" : params.toString();
        if (TextUtils.isEmpty(time)) {
            time = "0";
        }
        return coverTimeHHmm(time) + "\n" + coverTimeMMDD(time) + "\n" + "开售";
    }

    /**
     * 获得指定时间格式的小时，分
     */
    public static String coverTimeHHmm(Object obj) {
        long time = Long.valueOf(obj.toString());
        return new SimpleDateFormat("HH:mm", Locale.CHINESE).format(new Date(time));
    }

    /**
     * 获得指定时间格式的小时，分
     */
    public static String coverTimeHHmmss(Object obj) {
        long time = Long.valueOf(obj.toString());
        return new SimpleDateFormat("HH:mm:ss", Locale.CHINESE).format(new Date(time));
    }

    /**
     * 获得指定时间格式的月日
     */
    public static String coverTimeMMDD(Object obj) {
        long time = Long.valueOf(obj.toString());
        return new SimpleDateFormat("MM月dd日", Locale.CHINESE).format(new Date(time));
    }

    /**
     * 获得指定时间格式的年月日时分
     */
    public static String coverTimeYYMMDDHHmm(Object obj) {
        return coverTime(obj, DateFormatter.NORMAL.getValue());
    }

    /**
     * 获得指定时间格式的年月日
     */
    public static String coverTimeYYMMDD(Object obj) {
        return coverTime(obj, DateFormatter.DD.getValue());
    }

    /**
     * 获得指定时间格式的年月日时分秒
     */
    public static String coverTimeYYMMDDmmss(Object obj) {
        Log.d("logger", "logger" + obj);
        return coverTime(obj, DateFormatter.SS.getValue());
    }

    public static String coverTime(Object obj, String type) {
        if (obj == null || obj.toString().isEmpty()) {
            return "";
        }
        long time = Long.valueOf(obj.toString());
        return new SimpleDateFormat(type, Locale.CHINESE).format(new Date(time));
    }

    /**
     * 红包优惠券“￥50”显示设置
     */
    public static Spannable couponFormat(String str) {
        str = "￥" + str;
        Spannable span = new SpannableString(str);
        // 设置字体大小
        span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 40)), 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 预期收益率“12.00%”显示设置
     */
    public static Spannable aprFormat(String str) {
        str = str + "%";
        Spannable span = new SpannableString(str);
        String[] s = str.split("\\.");
        // 设置字体大小
        span.setSpan(new AbsoluteSizeSpan(DensityUtils.dp2px(ActivityUtils.peek(), 30)), 0, s[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 加息券“0.12%”显示设置
     */
    public static Spannable aprPercentFormat(String str) {
        str = str + "%";
        Spannable span = new SpannableString(str);
        // 设置字体大小
        span.setSpan(new AbsoluteSizeSpan(DensityUtils.dp2px(ActivityUtils.peek(), 30)), 0, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public static String periodFormat(int period) {
        return period + "期";
    }

    /**
     * 已售“12.35%”显示设置
     */
    public static Spannable sellPercentFormat(String str) {
        str = "已售" + str + "%";
        Spannable span = new SpannableString(str);
        // 设置字体大小
//        span.setSpan(new AbsoluteSizeSpan(DensityUtils.dp2px(ActivityUtils.peek(), 10)), 0, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 设置小号字体
     */
    public static Spannable smallText(String str) {
        Spannable span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 14)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.WHITE), 0, str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    /**
     * 根据不同的状态，设置marginLeft的值
     */
    public static int marginLeft(int type) {
        if (type == 0) {
            return R.dimen.x0;
        } else {
            return R.dimen.x20;
        }
    }

    /**
     * 年化利率格式化成 123.45元
     */
    private static String aprDisplayFormat(String args) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(Converter.getDouble(args));
    }

    /**
     * 从object中获取 string 型，如果字符串为空，则返回"0"
     */
    public static String getString(Object args) {
        if (args == null) {
            return "0";
        } else {
            return String.valueOf(args);
        }
    }

    /**
     * 添加渐变背景图片 setColorFilter
     *
     * @param drawable
     * @return
     */
    public static Drawable getBackground(Drawable drawable) {
        drawable.setColorFilter(ContextCompat.getColor(AlaConfig.getContext(), R.color.fw_app_color_principal), PorterDuff.Mode.MULTIPLY);
        return drawable;
    }

    /**
     * 添加纯色背景图片 setColorFilter
     *
     * @param drawable
     * @return
     */
    public static Drawable getSimpleBackground(Drawable drawable) {
        return drawable;
    }


}
