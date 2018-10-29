package com.liyuu.strategy.util;

import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import com.liyuu.strategy.app.App;

/**
 * toast 工具类
 */

public final class ToastUtil {

    private static Toast mToast = Toast.makeText(App.getInstance(), "", Toast.LENGTH_SHORT);

    public static void showMsg(@StringRes int resId) {
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(resId);
        mToast.show();
    }

    public static void showMsg(CharSequence text) {
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(text);
        mToast.show();
    }

    public static void showLongMsg(@StringRes int resId) {
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(resId);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showLongMsg(CharSequence text) {
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(text);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

}
