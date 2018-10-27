package com.liyuu.strategy.ui.home.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.TextViewDrawCode;

import java.text.DecimalFormat;

public class StockUtils {
    private static DecimalFormat mFormat = new DecimalFormat("#0.00");


    public static void setStockShow(TextView tv, double f, Context context, boolean isNeedSymbol, boolean isNeedPercent) {
        setStockShow(tv, f, context, isNeedSymbol, isNeedPercent, "", TextViewDrawCode.NONE);
    }


    /**
     * 拼装显示股票
     *
     * @param f             浮动
     * @param isNeedPercent 是否显示百分比
     * @param moreStr       拼接的其他字段
     * @param dr            拼接字段的位置
     */
    static void setStockShow(TextView tv, double f, Context context,
                             boolean isNeedSymbol, boolean isNeedPercent, String moreStr, @TextViewDrawCode int dr) {
        String text = (isNeedPercent) ? mFormat.format(f) + "%" : mFormat.format(f);

        String space = (TextUtils.isEmpty(moreStr) ? "" : " ");

        drawStockColor(context, f, tv);
        if (f > 0.f) {
            tv.setText(space + symbolDelete(installString(String.format("+%s", text), moreStr, dr), isNeedSymbol));
        } else if (f < 0.f) {
            //负数自带符号
            tv.setText(space + symbolDelete(installString(text, moreStr, dr), isNeedSymbol));
        } else if (Double.isNaN(f)) {
            tv.setText("--");
        } else {
            tv.setText(symbolDelete(installString(text, moreStr, dr), isNeedSymbol));
        }
    }

    private static void drawStockColor(Context context, double f, TextView tv) {
        if (f > 0.f) {
            tv.setTextColor(context.getResources().getColor(R.color.stock_red_e5462d));
        } else if (f < 0.f) {
            //负数自带符号
            tv.setTextColor(context.getResources().getColor(R.color.stock_green_73a848));
        } else if (Double.isNaN(f)) {
            tv.setText("--");
        } else {
            tv.setTextColor(context.getResources().getColor(R.color.stock_grey_color));
        }
    }

    public static int getStockColor(double f) {
        int color = -1;
        if (f > 0.f) {
            color = R.color.stock_red_e5462d;
        } else if (f < 0.f) {
            //负数自带符号
            color = R.color.stock_green_73a848;
        } else {
            color = R.color.stock_grey_color;
        }
        return color;
    }

    private static String symbolDelete(String s, boolean isNeedSymbol) {
        if (!isNeedSymbol) {
            s = s.replaceAll("-", "").replaceAll("\\+", "");
        }
        return s;
    }

    private static String installString(String mainStr, String addStr, @TextViewDrawCode int dr) {
        if (dr == TextViewDrawCode.STRING_LEFT)
            mainStr = addStr + " " + mainStr;
        else if (dr == TextViewDrawCode.STRING_RIGHT)
            mainStr = mainStr + " " + addStr;
        return mainStr;
    }

    /**
     * 获取相应的股票状态
     */
    public static String getPayState(String payState) {
        switch (payState) {
            case "HALT":
                return "暂停";
            case "POSIT":
                return "盘后";
            case "OCALL":
                return "竞价";
            case "ENDTR":
                return "收盘";
            case "TRADE":
                return "交易";
            case "SUSP":
                return "停盘";
            case "DELISTED":
                return "退市";
            case "PRETR":
                return "盘前";
            case "BREAK":
                return "休市";
            case "STOPT":
                return "停盘";
            case "START":
                return "启动";
            default:
                return "异常";
        }
    }

    public static String getVolUnit(float num) {

        int e = (int) Math.floor(Math.log10(num));

        if (e >= 8) {
            return "亿手";
        } else if (e >= 4) {
            return "万手";
        } else {
            return "手";
        }


    }

    public static String getNumber2China(float num) {
        int e = (int) Math.floor(Math.log10(num));
        num = (float) (num / Math.pow(10, e - 1));
        DecimalFormat fnum = new DecimalFormat("##0.00");
        String rf = fnum.format(num);
        if (e >= 8) {
            return rf + "亿";
        } else if (e >= 4) {
            return rf + "万";
        } else {
            return num + "";
        }
    }

    public static int getStockTextColor(Context context, double value) {
        int color = context.getResources().getColor(R.color.text_grey_666666);
        if (value > 0.f) {
            color = context.getResources().getColor(R.color.stock_red_color);
        } else if (value < 0.f) {
            color = context.getResources().getColor(R.color.stock_green_color);
        }
        return color;
    }

}
