package com.liyuu.strategy.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17-8-31.
 */

public class StringUtils {
    private static DecimalFormat decimalTwo = new DecimalFormat("0.00");
    private static DecimalFormat decimalOne = new DecimalFormat("0.0");

    /**
     * 裁剪string
     *
     * @param body  需要裁剪的字段
     * @param start 开始的位置
     * @param end   结束的位置
     * @return
     */
    public static String cutString(String body, int start, int end) {
        StringBuilder sb = new StringBuilder(body);//构造一个StringBuilder对象
        sb.delete(end, body.length());
        sb.delete(0, start);
        return sb.toString();
    }

    /**
     * 在string中插入字段
     *
     * @param body       待插入的字段
     * @param insertBody 插入的字段
     * @param position   插入的位置
     * @return
     */
    public static String insertStr(String body, String insertBody, int position) {
        StringBuilder sb = new StringBuilder(body);//构造一个StringBuilder对象
        sb.insert(position, insertBody);//在指定的位置1，插入指定的字符串
        body = sb.toString();
        sb.delete(0, sb.length());
        sb = null;
        return body;
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        string = unicode.toString();
        unicode = null;
        return string;
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        unicode = string.toString();
        string = null;
        return unicode;
    }

    /**
     * 将float转换成*万/*亿
     */
    public static String changeFloatTwo(float num) {
        String result = null;
        if (num >= 100000000) {
            result = decimalTwo.format(num / 100000000.f) + "亿";
        } else if (num >= 10000) {
            result = decimalTwo.format(num / 10000.f) + "万";
        } else {
            result = decimalTwo.format(num);
        }
        return result;
    }

    /**
     * 将float转换成*万/*亿
     *
     * @return
     */
    public static String changeFloatOne(int num) {
        String result = null;
        if (num >= 100000000) {
            result = decimalOne.format(num / 100000000.f) + "亿";
        } else if (num >= 10000) {
            result = decimalOne.format(num / 10000.f) + "万";
        } else {
            result = String.valueOf(num);
        }
        return result;
    }

    public static boolean stringIsEquals(String one, String two) {
        if (one == null || two == null) return false;
        if (TextUtils.isEmpty(one)) one = "";
        if (TextUtils.isEmpty(two)) two = "";
        return one == two || two.equals(one);
    }

    /**
     * 获取strings字符串中所有str字符所在的下标
     *
     * @param strings 母字符串
     * @param str     子字符串
     * @return 字符串在母字符串中下标集合，如果母字符串中不包含子字符串，集合长度为零
     */
    public static List<Integer> getIndex(String strings, String str) {
        List<Integer> list = new ArrayList<>();
        int flag = 0;
        while (strings.indexOf(str) != -1) {
            //截取包含自身在内的前边部分
            String aa = strings.substring(0, strings.indexOf(str) + str.length());
            flag = flag + aa.length();
            list.add(flag - str.length());
            strings = strings.substring(strings.indexOf(str) + str.length());
        }
        return list;
    }

    /**
     * 对一段文字设置不同颜色
     *
     * @param wholeStr 整体文本
     * @param colorStr 上色文本
     * @param color    颜色
     */
    public static void setColorFulText(TextView textView, String wholeStr, String colorStr, int color) {
        if (TextUtils.isEmpty(wholeStr) || TextUtils.isEmpty(colorStr))
            return;
        if (!wholeStr.contains(colorStr)) {
            textView.setText(wholeStr);
            return;
        }
        int start = wholeStr.indexOf(colorStr);
        int end = start + colorStr.length();
        //创建一个SpannableString对象
        SpannableString span = new SpannableString(wholeStr);
        //设置字体前景色
        span.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(span);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 对一段文字设置不同颜色
     *
     * @param wholeStr 整体文本
     * @param colorStrings 上色文本
     * @param color    颜色
     */
    public static void setColorFulTexts(TextView textView, int color, String wholeStr, String... colorStrings) {
        if(colorStrings.length == 0) return;
        //创建一个SpannableString对象
        SpannableString span = new SpannableString(wholeStr);
        for (String colorStr: colorStrings) {
            if (!wholeStr.contains(colorStr)) {
                continue;
            }
            int start = wholeStr.indexOf(colorStr);
            int end = start + colorStr.length();
            //设置字体前景色
            span.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(span);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 字符串转为int类型
     * @param moneyStr
     * @return
     */
    public static int parseInt(String moneyStr) {
        if(TextUtils.isEmpty(moneyStr)) return 0;
        try {
            int money = Integer.parseInt(moneyStr);
            return money;
        }catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static double parseDouble(String moneyStr) {
        if(TextUtils.isEmpty(moneyStr)) return 0;
        try {
            double money = Double.parseDouble(moneyStr);
            return money;
        }catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isMoreThanZero(String str) {
        Double str_d = parseDouble(str);
        return str_d >= 0;
    }

    /**
     * 获取非零数字所在的位置
     * @param input
     * @return int
     */
    public static int getNonZeroIndex(String input) {
        int len = input.length();
        for(int i=0;i<len;i++) {
            String a = String.valueOf(input.charAt(i));
            if("0".equals(a)) continue;
            if(".".equals(a)) {
                return i-1;
            }
            return i;
        }
        return -1;
    }
}
