package com.gsclub.strategy.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 解决底部屏幕按键适配(底部虚拟键屏幕适配问题)
 * Created by Mercury on 2016/10/25.
 */

public class AndroidWorkaround {

    /**
     * @param content          虚拟键盘更改需变化的布局（需整体上移的布局：移动高度为虚拟键盘的高度/系统导航栏高度按需上升）
     * @param context
     * @param isNeedUpStateBar 是否需要上移顶部系统导航栏的高度（对于UI置顶至系统导航栏的界面，填入true）
     */
    public static void assistActivity(View content, Context context, boolean isNeedUpStateBar) {
        new AndroidWorkaround(content, context, isNeedUpStateBar);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    private AndroidWorkaround(View content, final Context context, final boolean isNeedUpStateBar) {
        mChildOfContent = content;
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent(context, isNeedUpStateBar);
            }
        });
        frameLayoutParams = mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent(Context context, boolean isNeedUpStateBar) {
        int usableHeightNow = computeUsableHeight(context, isNeedUpStateBar);
        if (usableHeightNow != usableHeightPrevious) {

            frameLayoutParams.height = usableHeightNow;
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight(Context context, boolean isNeedUpStateBar) {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - ((isNeedUpStateBar) ? getStatusBarHeight(context) : 0));
    }

    /**
     * 判断是否有虚拟键
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

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
