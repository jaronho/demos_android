package com.liyuu.strategy.ui.stock.fragment;

import android.text.TextUtils;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;

import java.text.DecimalFormat;

/**
 * Created by hlw on 2018/3/9.
 */

public class KLineMaUtils {
    public static void draw(int dataSetIndex, CombinedChart combinedchart, TextView tvMa5, TextView tvMa10, TextView tvMa20) {
        int dataSize = combinedchart.getData().getLineData().getDataSets().size();

        //ma5
        if (dataSize < 1) {
            drawAllMa(tvMa5, tvMa10, tvMa20, " ", " ", " ");
            return;
        }


        Object ma5 = combinedchart.getData().getLineData().getDataSets().get(0).getEntryForXIndex(dataSetIndex).getVal();
        tvMa5.setText((ma5 != null && !TextUtils.isEmpty(ma5.toString()) && dataSetIndex >= 5) ? formatValue(ma5.toString()) : " ");


        //ma10
        if (dataSize < 2) {
            drawAllMa(tvMa5, tvMa10, tvMa20, formatValue(ma5.toString()), " ", " ");
            return;
        }
        Object ma10 = combinedchart.getData().getLineData().getDataSets().get(1).getEntryForXIndex(dataSetIndex).getVal();
        tvMa10.setText((ma10 != null && !TextUtils.isEmpty(ma10.toString()) && dataSetIndex >= 10) ? formatValue(ma10.toString()) : " ");


        //ma20
        if (dataSize < 3) {
            drawAllMa(tvMa5, tvMa10, tvMa20, formatValue(ma5.toString()), formatValue(ma10.toString()), " ");
            return;
        }
        Object ma20 = combinedchart.getData().getLineData().getDataSets().get(2).getEntryForXIndex(dataSetIndex).getVal();
        tvMa20.setText((ma20 != null && !TextUtils.isEmpty(ma20.toString()) && dataSetIndex >= 20) ? formatValue(ma20.toString()) : " ");
    }

    private static void drawAllMa(TextView tvMa5, TextView tvMa10, TextView tvMa20,
                                  String fma5, String fma10, String fma20) {
        drawMa(tvMa5, fma5);
        drawMa(tvMa10, fma10);
        drawMa(tvMa20, fma20);
    }

    private static void drawMa(TextView tv, String f) {
        if (TextUtils.isEmpty(f))
            tv.setText("");
        else
            tv.setText(String.valueOf(f));
    }

    private static DecimalFormat mFormat = new DecimalFormat("#0.00");

    static String formatValue(String floatString) {
        float f = Float.parseFloat(floatString);
        return mFormat.format(f);
    }
}
