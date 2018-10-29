package com.liyuu.strategy.util.status;

import android.app.Activity;
import android.os.Build;
import android.view.View;

public class AndroidMHelper implements IHelper {
    /**
     * @return if version is lager than M
     */
    @Override
    public boolean setStatusBarLightMode(Activity activity, boolean isFontColorDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

}