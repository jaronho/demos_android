package com.gsclub.strategy.util;

import com.gsclub.strategy.BuildConfig;

public class LogUtil {

    public static final String TAG = "st_http";
    public static boolean isDebug = BuildConfig.SHOW_LOG;

    public static void d(Object... msg) {
        if (isDebug) {
            Logger.d(msg.toString());
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Logger.i(msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Logger.i(msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Logger.e(msg);
        }
    }
}
