package com.example.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by NY on 2016/11/18.
 * 时间工具类
 */

public class MyDateUtils {
    /**
     * 获取格林威治时间(1970年至今的秒数)
     */
    public static long getGMTime1() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/Greenwich"));
        String format = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date gmDate = null;
        try {
            gmDate = sdf1.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gmDate.getTime() / 1000;
    }

    /**
     * 获取格林威治时间 即1970年至今的秒数
     */
    public static long getGMTime2() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 获取时间HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        //"\\s"以空格截断
        String[] split = date.split("\\s");
        if (split.length > 1) {
            time = split[1];
        }
        return time;
    }

    /**
     * 获取当前时间的年月日时分秒
     * @return
     */
    public static String current() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        return year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分" + second + "秒";
    }

    /**
     * 得到昨天的日期
     *
     * @return
     */
    public static String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yestoday = sdf.format(calendar.getTime());
        return yestoday;
    }

    /**
     * 得到今天的日期
     *
     * @return
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        return date;
    }

    /**
     * 得到明天的日期
     *
     * @return
     */
    public static String getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * 时间转化为时间格式
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timeStamp * 1000);
    }

    /**
     * 时间转化为时间格式
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToStr1(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timeStamp * 1000);
    }

    /**
     * 时间转化为时间(几点)
     *
     * @param time
     * @return
     */
    public static String timeStampToTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(time* 1000);
    }

    /**
     * 将日期格式转化为时间(秒数)
     *
     * @param time
     * @return
     */
    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }
    public static long getStringToDate2(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * 将日期格式转化为时间(秒数)
     *
     * @param time
     * @return
     */
    public static long getString2Date(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * 判断是否大于当前时间
     *
     * @param time
     * @return
     */
    public static boolean judgeCurrTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
            long t = date.getTime();
            long round = System.currentTimeMillis();
            if (t - round > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    /**
     * 判断是否大于当前时间
     *
     * @param time
     * @return
     */
    public static boolean judgeCurrTime(long time) {
        long round = System.currentTimeMillis();
        if (time - round > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较后面的时间是否大于前面的时间
     *
     * @param
     * @return
     */
    public static boolean judgeTime2Time(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            //转化为时间
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            //获取秒数作比较
            long l1 = date1.getTime() / 1000;
            long l2 = date2.getTime() / 1000;
            if (l2 - l1 > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 比较后面的时间是否大于前面的时间
     *
     * @param
     * @return
     */
    public static boolean judgeTime2Time2(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //转化为时间
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            //获取毫秒数作比较
            long l1 = date1.getTime();
            long l2 = date2.getTime();
            if (l2 - l1 > 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 得到日期 yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String formatDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(time * 1000);
        return date;
    }

    /**
     * 得到时间 HH:mm:ss
     *
     * @param timeStamp
     * @return
     */
    public static String getTime(long timeStamp) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(timeStamp * 1000);
        String[] split = date.split("\\s");
        if (split.length > 1) {
            time = split[1];
        }
        return time;
    }

    /**
     * 将一个时间转换成提示性时间字符串，(多少分钟)
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;
        return time / 60 + "";
    }

    /**
     * 获得当前时间差
     *
     * @param timeStamp
     * @return
     */
    public static int nowCurrentTime(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = timeStamp - curTime;
        return (int) time;
    }


    /**
     * 获取当前的时 -->flag==true
     * 获取当前的分 -->flag==false
     *
     * @return
     */
    public static String nowCurrentPoint(boolean flag) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String date = sdf.format(System.currentTimeMillis());
        String[] split = date.split(":");
        String hour = null;
        String minute = null;
        if (flag) {
            if (split.length > 1) {
                hour = split[0];
                return hour;
            }
        } else {
            if (split.length > 1) {
                minute = split[1];
                return minute;
            }
        }
        return null;
    }

    /**
     * 将标准时间格式HH:mm:ss化为当前的时间差值
     *
     * @param str
     * @return
     */
    public static String StandardFormatStr(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sdf.parse(str);
            long timeStamp = d.getTime();

            long curTime = System.currentTimeMillis() / (long) 1000;
            long time = curTime - timeStamp / 1000;
            return time / 60 + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将一个时间转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }


    /**
     * 日期变量转成对应的星期字符串
     *
     * @param date
     * @return
     */

    public static final int WEEKDAYS = 7;
    //星期字符数组
    public static String[] WEEK = {"周日", "周一", "周二", "周三",
            "周四", "周五", "周六"};

    public static String DateToWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > WEEKDAYS) {
            return null;
        }
        return WEEK[dayIndex - 1];
    }

    /**
     * 获取指定网站的日期时间
     *
     * @param webUrl
     * @return
     * @author SHANHY
     * @date   2015年11月27日
     * String webUrl1 = "http://www.bjtime.cn";//bjTime
     * String webUrl2 = "http://www.baidu.com";//百度
     * String webUrl3 = "http://www.taobao.com";//淘宝
     * String webUrl4 = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
     * String webUrl5 = "http://www.360.cn";//360
     * String webUrl6 = "http://www.beijing-time.org";//beijing-time
     */
    public static String getWebsiteDatetime(String webUrl){
        try {
            URL url = new URL(webUrl);// 取得资源对象
            URLConnection uc = url.openConnection();// 生成连接对象
            uc.connect();// 发出连接
            long ld = uc.getDate();// 读取网站日期时间
            Date date = new Date(ld);// 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuilder sb = new StringBuilder();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }

}

