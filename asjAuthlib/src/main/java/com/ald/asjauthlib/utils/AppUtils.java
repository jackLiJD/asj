package com.ald.asjauthlib.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.ald.asjauthlib.authframework.core.config.AlaConfig;
import com.ald.asjauthlib.authframework.core.location.LocationResult;
import com.ald.asjauthlib.authframework.core.location.LocationUtils;
import com.ald.asjauthlib.authframework.core.utils.ActivityUtils;
import com.ald.asjauthlib.authframework.core.utils.DensityUtils;
import com.ald.asjauthlib.authframework.core.utils.InfoUtils;
import com.ald.asjauthlib.authframework.core.utils.MiscUtils;
import com.ald.asjauthlib.authframework.core.utils.SPUtil;
import com.ald.asjauthlib.authframework.core.utils.log.Logger;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ald.asjauthlib.authframework.core.utils.MiscUtils.isEmpty;

/**
 * 版权：XXX公司 版权所有
 * 作者：Jacky Yu
 * 版本：1.0
 * 创建日期：2017/2/23 11:16
 * 描述：
 * 修订历史：
 */
public class AppUtils {
    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    /**
     * 金钱数字2位小数处理
     */
    public static String formatAmount(String amount) {
        if (MiscUtils.isEmpty(amount)) {
            amount = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(amount);
        return formatAmount(bigDecimal);
    }


    /**
     * 金钱数字2位小数处理
     */
    public static String endformatAmount(String amount) {
        if (MiscUtils.isEmpty(amount)) {
            amount = "0";
        }
        BigDecimal bigDecimal = new BigDecimal(amount);
        return endformatAmount(bigDecimal);
    }

    /**
     * 金钱数字2位小数处理保留符号
     */
    public static String formatAmountKeepMinus(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return "0.00";
        }
        DecimalFormat format = new DecimalFormat("#####0.00");
        return format.format(bigDecimal);
    }

    /**
     * 金钱数字2位小数处理
     */
    public static String formatAmount(BigDecimal bigDecimal) {
        if (bigDecimal == null || bigDecimal.doubleValue() <= 0) {
            return "0.00";
        }
        DecimalFormat format = new DecimalFormat("#####0.00");
        return format.format(bigDecimal);
    }

    /**
     * 金钱数字2位小数处理(小数位为0不显示)
     */
    public static String endformatAmount(BigDecimal bigDecimal) {
        if (bigDecimal == null || bigDecimal.doubleValue() <= 0) {
            return "0";
        }
        DecimalFormat format = new DecimalFormat("#####0.00");
        String result = format.format(bigDecimal);
        return result.replace(".00", "");
    }

    /**
     * 金额格式化 返回整数或者一位小数
     *
     * @param bigDecimal 金额
     * @return 格式化金额
     */
    public static String formatCouponAmount(BigDecimal bigDecimal) {
        DecimalFormat format = new DecimalFormat("#####0.0");
        String formatAmount = format.format(bigDecimal);
        return formatAmount.replace(".0", "");
    }

    /**
     * 金钱数字去2位小数处理
     */
    public static String deleteFormatAmount(String amountStr) {
        if (MiscUtils.isNotEmpty(amountStr)) {
            String arr[] = amountStr.split("[.]");
            if (arr.length < 2) {
                return amountStr;
            }
            return arr[0];
        }
        return amountStr;
    }

    /**
     * 红包优惠券“￥50”显示设置
     */
    public static Spannable couponFormat(String str) {
        str = "¥" + deleteFormatAmount(str);
        Spannable span = new SpannableString(str);
        // 设置字体大小
        span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 40)), 1, str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }


    /**
     * 红包优惠券“￥50”显示设置
     */
    public static Spannable couponOrderFormat(String str) {
        str = "¥" + str;
        Spannable span = new SpannableString(str);
        // 设置字体大小
        span.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(ActivityUtils.peek(), 13)), 1, str.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public static SpannableString setForegroundColorSpan(String str, @ColorInt int color, int startPostion, int endPostion) {
        SpannableString spannableString = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, startPostion, endPostion, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    /**
     * 转换日期 转换为更为人性化的时间
     *
     * @param time 时间
     */
    public static String translateDate(long time) {
        time = time / 1000;
        long curTime = System.currentTimeMillis();
        long oneDay = 24 * 60 * 60;
        Calendar today = Calendar.getInstance();    //今天
        today.setTimeInMillis(curTime * 1000);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStartTime = today.getTimeInMillis() / 1000;
        if (time >= todayStartTime) {
            long d = curTime - time;
            if (d <= 60) {
                return "1分钟前";
            } else if (d <= 60 * 60) {
                long m = d / 60;
                if (m <= 0) {
                    m = 1;
                }
                return m + "分钟前";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("今天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            }
        } else {
            if (time < todayStartTime && time > todayStartTime - oneDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("昨天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {

                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            } else if (time < todayStartTime - oneDay && time > todayStartTime - 2 * oneDay) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("前天 HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date(time * 1000);
                String dateStr = dateFormat.format(date);
                if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }
                return dateStr;
            }
        }
    }

    /**
     * 姓名处理
     */
    public static String formatRealName(String realName) {
        if (MiscUtils.isEmpty(realName)) {
            return " * ";
        }
        if (realName.length() >= 2) {
            return " * " + realName.substring(1);
        } else {
            return realName;
        }
    }

    /**
     * 银行卡后3位截取处理
     */
    public static String formatBankCardNo(String cardNo) {
        if (isEmpty(cardNo)) {
            return "**** **** **** 000";
        }
        if (cardNo.length() <= 3) {
            return "**** **** **** " + cardNo;
        }
        return "**** **** **** " + cardNo.substring(cardNo.length() - 4);
    }

    /**
     * 获得指定时间格式的月日
     */
    public static String coverTimeMMDD(Object obj) {
        long time = Long.valueOf(obj.toString());
        return new SimpleDateFormat("MM月dd日", Locale.CHINESE).format(new Date(time));
    }

    public static String coverTimeYMD(Object obj) {
        long time = Long.valueOf(obj.toString());
        return new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(new Date(time));
    }

    /**
     * 获得指定时间格式的月日
     */
    public static String coverTimeYYMMDD(Object obj) {
        long time = Long.valueOf(obj.toString());
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(time));
    }

    /**
     * 日期转换
     */
    public static String convertSimpleData(Long time) {
        return new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(new Date(time));
    }

    /**
     * 日期相差天数
     */
    public static String returnDay(long day1, long day2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(day1);
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(day2);
        if (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR) < 0 || c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR) == 0) {
            int a = c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR);
            if (a < 0) {
                return "已到期";
            } else if (a == 0) {
                return "今天到期";
            } else if (a == 1) {
                return "明天到期";
            } else if (a > 3) {
                return AppUtils.coverTimeYYMMDD(day1) + "到期";
            } else {
                return a + "天后到期";
            }
        } else {
            int a = c1.get(Calendar.DAY_OF_YEAR) + 365 - c2.get(Calendar.DAY_OF_YEAR);
            return a + "天后到期";
        }
    }


    /**
     * 获得指定时间格式的月日时分秒
     */
    public static String coverTimeYMDHMS(Object obj) {
        if (obj != null) {
            long time = Long.valueOf(obj.toString());
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
        }
        return "";
    }

    /**
     * long转Date
     */
    public static String longToDate(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
    }

    /**
     * 格式化手机号:13**00
     */
    public static String formatPhoneFourStar(String phone) {
        if (isEmpty(phone)) {
            return "";
        } else if (phone.length() < 11) {
            return phone;
        } else {
            String startPhone = phone.substring(0, 3);
            String endPhone = phone.substring(7);
            return startPhone + "****" + endPhone;
        }
    }

    /**
     * 检验手机号(只验证位数)
     *
     * @param phone 手机号
     */
    public static boolean isPhoneNo(String phone) {
        return !(isEmpty(phone) || phone.length() != 11);
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email 电子邮件地址
     */
    public static boolean isEmail(String email) {
        return !(email == null || email.trim().length() == 0) && emailer.matcher(email).matches();
    }

    private static boolean isMatch(String text, String pattern) {
        if (isEmpty(text)) {
            return false;
        }
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    /**
     * 检查是否为密码
     *
     * @param pwd 密码
     */
    public static boolean isPassword(String pwd) {
        return isMatch(pwd, "^(?![^a-zA-Z]+$)(?!\\D+$).{6,18}$");
    }

    /**
     * 检查是否为数字
     */
    public static boolean isNumber(String str) {
        return isMatch(str, "^[0-9]*$");
    }


    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top == 0 ? 50 : rect.top;
    }

    /**
     * 获取系统当前月份
     */
    public static String getCurrentMonth() {
        int month;
        Calendar calendar = Calendar.getInstance();//获取日历对象
        month = calendar.get(Calendar.MONTH);//获取当前月份
        Log.d("月份", month + "月");
//        String[] monthArr={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
        String[] monthArr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        for (int i = 0; i < monthArr.length; i++) {
            if (month == i) {
                return monthArr[i];
            }
        }
        return null;
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
//        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取指定格式的当前时间
     */
    public static String getCurrentTime(SimpleDateFormat formatter) {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static long getDifferentTime(long startTime, long endTime) {
        long diff = endTime - startTime;
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
        long hour1 = diff / (60 * 60 * 1000);
        String hourString = hour1 + "";
        long min1 = ((diff / (60 * 1000)) - hour1 * 60);
//        return hour1 + "小时" + min1 + "分";
        return min;
    }

    public static long getDifferentTimeMS(long startTime, long endTime) {
        long diff = endTime - startTime;
        long day = diff / (24 * 60 * 60 * 1000);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
        long hour1 = diff / (60 * 60 * 1000);
        String hourString = hour1 + "";
        long min1 = ((diff / (60 * 1000)) - hour1 * 60);
//        return hour1 + "小时" + min1 + "分";
        return ms;
    }


    public static void fitsSystemWindows(boolean isTranslucentStatus, View view) {
        if (isTranslucentStatus) {
            view.getLayoutParams().height = calcStatusBarHeight(view.getContext());
        }
    }

    public static int calcStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = AlaConfig.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 截取字符串
     *
     * @param str         原字符串
     * @param formatStr   格式化字符串
     * @param startIndex  截取起始位置
     * @param middleIndex 截取中间位置
     * @param endIndex    截取结束位置
     */
    public static String formatStrWithStar(String str, String formatStr, int startIndex, int middleIndex, int endIndex) {
        if (isEmpty(str)) {
            return "";
        }
        //手机号
        if (str.length() == 11) {
            String startPhone = str.substring(startIndex, middleIndex);
            String endPhone = str.substring(endIndex);
            return startPhone + "**" + endPhone;
        }
        //身份证号
//        if (str.length() == 18) {
        String startStr = str.substring(startIndex, middleIndex);
        String endStr = str.substring(endIndex);
        return startStr + formatStr + endStr;
//        }
//        return str;
    }

    /**
     * List转逗号分隔的字符串
     */
    public static String listToString(List list) {
        StringBuilder stringBuilder = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    stringBuilder.append(list.get(i)).append(",");
                } else {
                    stringBuilder.append(list.get(i));
                }
            }
        }
        return stringBuilder.toString();
    }

    public static Bitmap getBitmapFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null) {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }
        return null;
    }

    /**
     * List转逗号分隔的加密字符串(url)
     */
    public static String listToEncodeString(List list) {
        StringBuilder stringBuilder = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    try {
                        stringBuilder.append(URLEncoder.encode(list.get(i).toString(), "UTF-8")).append(",");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        stringBuilder.append(URLEncoder.encode(list.get(i).toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return stringBuilder.toString();
    }


    private static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            @SuppressWarnings("rawtypes")
            Class c;
            try {
                c = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, displayMetrics);
                dpi = displayMetrics.heightPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dpi;
    }

    private static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null)
            wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 2      * 获取 虚拟按键的高度
     * 3      *
     * 4      * @param context
     * 5      * @return
     * 6
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight - contentHeight;
    }

    public static boolean isSoftShowing(Context context) {
        //获取当前屏幕内容的高度
        int screenHeight = ((Activity) context).getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }

    /*
     * 设置控件所在的位置YY，并且不改变宽高，
     * XY为绝对位置
     */
    public static void setLayout(View view, int x, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, x + margin.width, y + margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    /**
     * 判断应用是否在后台
     * public static final int IMPORTANCE_BACKGROUND = 400//后台
     * public static final int IMPORTANCE_EMPTY = 500//空进程
     * public static final int IMPORTANCE_FOREGROUND = 100//在屏幕最前端、可获取到焦点 可理解为Activity生命周期的OnResume();
     * public static final int IMPORTANCE_SERVICE = 300//在服务中
     * public static final int IMPORTANCE_VISIBLE = 200//在屏幕前端、获取不到焦点可理解为Activity生命周期的OnStart();
     */
    public static boolean isForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                    if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        Logger.d("前台", appProcess.processName);
                        return true;
                    } else {
                        Logger.d("后台", appProcess.processName);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 金额每隔三位加上一个逗号
     */
    public static String formatPriceDot(float data) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(data);
    }

    /**
     * 金额每隔三位加上一个逗号
     */
    public static String formatPriceDotInt(int data) {
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(data);
    }

    /**
     * @param data 向上取整
     */
    public static int getCeil(BigDecimal data) {
        return (int) (Math.ceil((data.doubleValue())));
    }



    /**
     * 判断手机是否安装某个应用
     *
     * @param context        context
     * @param appPackageName 包名
     * @return true false
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.toLowerCase().equals(pn.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean downloadStatus(DownloadManager downloadManager, int downloadStatus, long id) {
        // to check if the file is in cache, get its destination from the database
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(id);
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            if (cursor == null) {
                return false;
            }
            if (cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                if (downloadStatus == status) {
                    return true;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // downloaded file not found or its status is not 'successfully completed'
        return false;
    }


    /**
     * @author Yangyang
     * @desc 计算信用卡手续费, 保留两位小数, 第三位向上取整
     */
    public static String decimalCalculation(String startVal, double feeRadio) {
        if (MiscUtils.isEmpty(startVal)) startVal = "0";
        double creditAmount = Double.valueOf(startVal);
        if (creditAmount <= 0.01) return "0.01";
        DecimalFormat df = new DecimalFormat("#####0.00");
        double ceil = Math.ceil(creditAmount * feeRadio * 100);
        String format = df.format(ceil / 100);
        return format;
       /* String startStr = startVal.toString();
        String startDecimal = startStr.split("\\.")[1];
        Double jishu = 0.01;
        Double endVal = 0.0;
        if(startDecimal.length()>2 && "5".equals(String.valueOf(startDecimal.charAt(2))) &&
                Integer.parseInt(String.valueOf(startDecimal.charAt(1)))%2==0){
            endVal = Double.valueOf(startStr.substring(0, startStr.length()-1))+jishu;
        } else{
            endVal = Double.valueOf(df.format(startVal));
        }
        return Double.valueOf(df.format(endVal));*/
    }


    /**
     * @author Yangyang
     * @desc 提供反欺诈需要的设备信息
     */
    public static void genFraudInfo(JSONObject jsonObject, Activity activity) {
        jsonObject.put("uuid", InfoUtils.getDeviceId());
        jsonObject.put("deviceType", "2");//1IOS,2ANDROID,3H5
        jsonObject.put("evirenment", InfoUtils.getEvirenmentInfo());
        jsonObject.put("deviceinfo", pinDeviceInfo());
        jsonObject.put("mainscreen", DensityUtils.getWidth(activity) + "x" + DensityUtils.getHeight(activity));
        jsonObject.put("mobilemodel", InfoUtils.getDeviceName());
    }

    private static String pinDeviceInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", InfoUtils.getIMEI());
        jsonObject.put("uuid", InfoUtils.getDeviceId());
        jsonObject.put("serial", Build.SERIAL);
        jsonObject.put("IP", InfoUtils.getIp());
        jsonObject.put("mac", InfoUtils.getAdresseMAC(AlaConfig.getContext()));
        return jsonObject.toString();
    }


    /**
     * @author Yangyang
     * @desc 判断是否是该用户今日首次进入我的界面, 走服务器了暂时无用
     */
    public static boolean isTodayFirstToMinFg() {
        HashMap<String, String> timeMap = (HashMap<String, String>) SPUtil.getEntity(HashMap.class, Constant.FIRST_TIME, null);
        if (timeMap == null) timeMap = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);// 设置日期格式
        String todayTime = df.format(new Date());// 获取当前的日期
        String userName = AlaConfig.getAccountProvider().getUserName();//当前用户名
        //第一次打开app或者第一次存储该用户或者日期不想等
        if (timeMap.size() == 0 || timeMap.get(userName) == null || (!timeMap.get(userName).equals(todayTime))) {
            timeMap.put(userName, todayTime);
            SPUtil.setEntity(timeMap, Constant.FIRST_TIME, false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 每个用户两次弹窗间隔2天及以上，每个用户最多弹10次，首次下载启动打开的用户在当天不弹此提醒
     */
    public static boolean isAppMessageOpen() {
        long lastTimeL = SPUtil.getLong(Constant.CURRENT_TIME, 0);
        int sysMessCount = SPUtil.getInt(Constant.SYS_MESS_COUNT, 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        long timeL = System.currentTimeMillis();
        if (0 != lastTimeL) {
            SPUtil.setValue(Constant.CURRENT_TIME, timeL);
            Date date = new Date();
            date.setTime(timeL);
            int thisTime = Integer.parseInt(simpleDateFormat.format(date)); // 获取当前时间
            date.setTime(lastTimeL);
            int lastTime = Integer.parseInt(simpleDateFormat.format(date));
            if (lastTime == thisTime) return false;
            int thisYear = thisTime / 10000; // 如 20180801
            int lastYear = lastTime / 10000;
            int thisMonth = thisTime % 10000 / 100;
            int lastMonth = lastTime % 10000 / 100;
            int thisDay = thisTime % 100;
            int lastDay = lastTime % 100;
            Calendar calendar = Calendar.getInstance();
            if (lastYear == thisYear) {
                if (lastMonth == thisMonth) { // 日期不同
                    return sysMessCount <= 10 && thisDay - lastDay >= 2 && SPUtil.setValue(Constant.SYS_MESS_COUNT, ++sysMessCount);
                } else { // 月份不同
                    date.setTime(lastTimeL);
                    if (isLastDayOfMonth(date, calendar)) {
                        date.setTime(timeL);
                        return sysMessCount <= 10 && (1 != thisMonth - lastMonth || !isFirstDayOfMonth(date, calendar)) && SPUtil.setValue(Constant.SYS_MESS_COUNT, ++sysMessCount);
                    } else {
                        return sysMessCount <= 10 && SPUtil.setValue(Constant.SYS_MESS_COUNT, ++sysMessCount);
                    }
                }
            } else { // 年份不同
                if (thisYear - lastYear != 1) {
                    return sysMessCount <= 10 && SPUtil.setValue(Constant.SYS_MESS_COUNT, ++sysMessCount);
                } else if (12 == lastMonth) {
                    date.setTime(lastTimeL);
                    if (isLastDayOfMonth(date, calendar)) {
                        date.setTime(timeL);
                        return sysMessCount <= 10 && (11 != lastMonth - thisMonth || !isFirstDayOfMonth(date, calendar)) && SPUtil.setValue(Constant.SYS_MESS_COUNT, ++sysMessCount);
                    } else {
                        return sysMessCount <= 10 && SPUtil.setValue(Constant.SYS_MESS_COUNT, ++sysMessCount);
                    }
                } else {
                    return sysMessCount <= 10 && SPUtil.setValue(Constant.SYS_MESS_COUNT, ++sysMessCount);
                }
            }
        } else {
            SPUtil.setValue(Constant.CURRENT_TIME, timeL);
            return false;
        }
    }

    /**
     * 是否是该月第一天
     */
    public static boolean isFirstDayOfMonth(Date date, Calendar calendar) {
        if (null == calendar) calendar = Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.MONTH));
        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * 是否是该月最后一天
     */
    public static boolean isLastDayOfMonth(Date date, Calendar calendar) {
        if (null == calendar) calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH) == calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH);
    }




    /**
     * 获取埋点参数
     */
    public static JSONObject getNativeMaidianInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appVersion", InfoUtils.getVersionName());//app 版本号
        jsonObject.put("channelName", "");
        LocationResult result = LocationUtils.getCurrentLocation();
        if (result != null) {
            jsonObject.put("latLng", result.getLatitude() + "," + result.getLongitude());
            jsonObject.put("gps", result.getAddress());
        }
        jsonObject.put("netType", InfoUtils.getNetworkType());
        jsonObject.put("phoneType", Build.MODEL);
        jsonObject.put("reqClient", "android");
        jsonObject.put("reqIp", InfoUtils.getIp());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");// 2018-11-20 14:11:23
        jsonObject.put("reqTime", getCurrentTime(formatter));
        jsonObject.put("sysVersion", InfoUtils.getDeviceVersion());
        jsonObject.put("userId", SPUtil.getLong(Constant.USER_ID, 0L));
        jsonObject.put("uuid", InfoUtils.getDeviceId());
        return jsonObject;
    }

}
