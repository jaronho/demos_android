package com.example.util;

import android.widget.Toast;

import com.example.nyapp.MyApplication;

/**
 * Created by NY on 2017/2/10.
 * Toast工具类
 */

public class MyToastUtil {

    public static void showShortMessage(String message) {
        Toast.makeText(MyApplication.sContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMessage(String message) {
        Toast.makeText(MyApplication.sContext, message, Toast.LENGTH_LONG).show();
    }
}
