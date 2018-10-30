package com.gsclub.strategy.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.gsclub.strategy.app.App;
import com.gsclub.strategy.app.SPKeys;

public class CommonUtil {

    public static boolean isCorrectPhone(String phone) {
        if(phone.length() < 11) {
            ToastUtil.showMsg("请输入11位有效手机号");
            return false;
        }
        return true;
    }

    public static boolean isCorrectPwd(String password) {
        if (password.length() < 6) {
            ToastUtil.showMsg("密码格式错误");
            return false;
        }
        return true;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---
            Context context = App.getInstance();
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     *
     * @return 是否是审核中状态
     */
    public static boolean isReview() {
        int activity_status = PreferenceUtils.getInt(SPKeys.FILE_COMMON, SPKeys.ACTIVITY_STATUS, 1);
        if(activity_status == 1) return true;
        return false;
    }
}
