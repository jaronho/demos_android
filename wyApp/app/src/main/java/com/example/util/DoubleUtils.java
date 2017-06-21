package com.example.util;

import java.text.DecimalFormat;

/**
 * Created by NY on 2017/3/8.
 * double类型数字
 */

public class DoubleUtils {
    //保留2位小数点
    public static String format2decimals(double number) {
        DecimalFormat df = new DecimalFormat("#####0.00");
        return df.format(number);
    }
}
