package com.ald.asjauthlib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by luckyliang on 2017/12/14.
 */

public class TimeUtils {

    /**
     * 返回GTM+8 北京时区的时间
     *
     * @param serverTime 服务器时间
     * @return GTM+8 的时区Date
     */
    public static Date longToDate(long serverTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(serverTime);
        return calendar.getTime();
    }

    public static String longToBeijingTimeString(long lDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date dateDB = new Date(lDate);
        return simpleDateFormat.format(dateDB);
    }

    public static String dateToBeijingTimeString(Date Date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(Date);
    }

    public static String timeHandle(int time) {
        String TIME;
        if (time < 10) TIME = "0" + time;
        else TIME = time + "";
        return TIME;
    }
}
