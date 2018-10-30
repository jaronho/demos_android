package com.gsclub.strategy.app;

import android.graphics.Color;

/**
 * Created by hlw on 2018/1/10.
 */

public interface RGBColor {
    /**
     * 绿色,红色
     */
    int[] STOCKS_COLORS = {
            Color.rgb(115, 168, 72), Color.rgb(229, 70, 45)
    };

    /**
     * 红色,绿色
     */
    int[] STOCKS_COLORS_CONTRARY = {
            Color.rgb(229, 70, 45), Color.rgb(115, 168, 72)
    };

}
