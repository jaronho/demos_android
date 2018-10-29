package com.liyuu.strategy.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @名称：TimeUtil.java
 * @描述：时间转化类
 */
public class TimeUtil {

    /**
     * 设置帖子发表时间
     *
     * @param oldTime
     * @param currentDate
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeStr(Date oldTime, Date currentDate) {
        long time1 = currentDate.getTime();

        long time2 = oldTime.getTime();

        long time = (time1 - time2) / 1000;

        if (time >= 0 && time < 60) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time > 3600 * 24) {
            return time / (3600 * 24) + "天前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(oldTime);
        }
    }

    public static String getTimeStr(long oldTime, Date currentDate) {
        return getTimeStr(stampToDate(oldTime), currentDate);
    }

    /**
     * @param time
     * @return
     * @描述：时间格式化
     * @date：2014-6-26
     */
    public static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return new SimpleDateFormat("MM-dd HH:mm").format(new Date(time));
    }

    public static String getNowHour() {
        String timeNow = new SimpleDateFormat("HH", Locale.CHINA)
                .format(new Date());
        return timeNow;
    }

    public static String getNowTime() {
        String timeNow = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        return timeNow;
    }

    public static String getNowMinute() {
        String timeNow = new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA)
                .format(new Date());
        return timeNow;
    }

    /**
     * string类型的date转date
     *
     * @param dateStr    string类型date
     * @param dateFormat 转换成date的数据格式
     * @return
     */
    public static Date str2Date(String dateStr, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return new Date();
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s, String dateFormat) {
        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static Date stampToDate(String s) {
        s = s + "000";//服务器返回时间单位为秒
        long lt = new Long(s);
        Date date = new Date(lt);
        return date;
    }

    public static Date stampToDate(long s) {
        return stampToDate(String.valueOf(s));
    }

    public static String dateFormat(String dateStr) {

        try {
            DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(dateStr);
            //format的格式可以任意
            DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            return sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String dateFormat(String dateStr, String oldFormat, String newFormat) {
        if (TextUtils.isEmpty(oldFormat))
            oldFormat = "yyyyMMdd";

        if (TextUtils.isEmpty(newFormat))
            newFormat = "yyyy-MM-dd";

        try {
            DateFormat sdf = new SimpleDateFormat(oldFormat);
            Date date = sdf.parse(dateStr);
            //format的格式可以任意
            DateFormat sdf2 = new SimpleDateFormat(newFormat);
            return sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }
}
