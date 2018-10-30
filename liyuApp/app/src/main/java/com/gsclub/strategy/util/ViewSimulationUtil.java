package com.gsclub.strategy.util;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hlw on 2018/1/31.
 */

public class ViewSimulationUtil {

    /**
     * 按键模拟点击事件
     */
    public static void viewOnclick(View view, Activity activity) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        setMouseClick(x, y, activity);
    }

    /**
     * 触发屏幕点击事件
     *
     * @param x 点击位置的x值
     * @param y 点击位置的y值
     */
    private static void setMouseClick(int x, int y, Activity activity) {
        MotionEvent evenDownt = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_DOWN, x, y, 0);
        activity.dispatchTouchEvent(evenDownt);
        MotionEvent eventUp = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_UP, x, y, 0);
        activity.dispatchTouchEvent(eventUp);
        evenDownt.recycle();
        eventUp.recycle();
    }

}
