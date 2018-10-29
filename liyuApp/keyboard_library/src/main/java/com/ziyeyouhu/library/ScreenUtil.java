package com.ziyeyouhu.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * Created by hlw on 2017/12/15.
 */

public class ScreenUtil {

    /**
     * 获取VIEW的高度
     *
     * @param view
     * @return
     */
    public static int getHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight(); // 获取高度

    }

    /**
     * 获取VIEW的宽度
     *
     * @param view
     * @return
     */
    public static int getwidth(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredWidth(); // 获取宽度
    }

    /**
     * 设置view的高度
     *
     * @param view
     * @param height
     */
    public static void setViewHeight(View view, int height) {
        ViewGroup.MarginLayoutParams linearParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = height;// 控件的高强制设成20
//        linearParams.width = 30;// 控件的宽强制设成30
        view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }

    /**
     * 判断view是否在屏幕范围内
     *
     * @param context
     * @param view
     * @return
     */
    public static boolean isViewShowInScreen(View view, Activity context) {
        Point p = new Point();
        context.getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth = p.x;
        int screenHeight = p.y;
        return isViewShowInScreen(view, screenWidth, screenHeight);
    }

    private static boolean isViewShowInScreen(View view, int screenWidth, int screenHeight) {
        if (view == null)
            return false;

        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (view.getLocalVisibleRect(rect)) {
            /*rect.contains(ivRect)*/
            return true;
        } else {
            return false;
        }
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}
