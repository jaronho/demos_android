package com.liyuu.strategy.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * snackbar 显示工具类
 */

public class SnackbarUtil {

    public static void show(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showShort(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
}
