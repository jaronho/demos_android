package com.liyuu.strategy.tpush;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.ui.DialogActivity;
import com.liyuu.strategy.ui.MainActivity;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.SystemUtil;
import com.liyuu.strategy.util.UserInfoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by 640 on 2018/1/29 0029.
 */

public class PushUtils {

    /**
     * 此处处理点击notification的操作事件（软重启APP后，在闪屏界面）
     *
     * @param context
     * @param bundle
     */
    public static void handlePushNew(Context context, Bundle bundle) {
        if (bundle == null) return;
        String className = PushUtils.getExtraValue(bundle, MessageConstants.KEY_CLASSNAME);
        if (TextUtils.isEmpty(className)) return;
        if (className.equals(MessageConstants.VALUE_SINGLE_SIGN_ON)) {
            DialogActivity.start(DialogActivity.DIALOG_EXIT, bundle.getString(JPushInterface.EXTRA_MESSAGE));// 单点登录
        } else if (className.equals(MessageConstants.VALUE_WEB_VIEW_ACTIVITY)) {
            goToDetailPage(context, bundle, false);
        } else if (className.equals(MessageConstants.VALUE_POSITION_DETAIL_ACTIVITY) ||
                className.equals(MessageConstants.VALUE_SETTLEMENT_DETAIL_ACTIVITY)) {
            goToDetailPage(context, bundle, true);
        } else if (className.equals(MessageConstants.VALUE_TRADE_LIST_CHOOSE)) {
            Intent appIntent = new Intent();// 万能跳转
            appIntent.setAction(MessageConstants.VALUE_MAINACTIVITY);
            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            String paramsString = PushUtils.getExtraValue(bundle, MessageConstants.KEY_PARAMS);
            Bundle bundle_params = getParamsBundle(paramsString);
            appIntent.putExtra(MainActivity.GO_TO_TRANSACTION, true);
            appIntent.putExtras(bundle_params);
            context.startActivity(appIntent);
        }
    }

    public static void goToDetailPage(Context context, Bundle bundle, boolean isNeedLogin) {
        if (isNeedLogin) {
            String openId = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_OPEN_ID, "");
            String reciverOpenId = PushUtils.getExtraValue(bundle, MessageConstants.KEY_OPEN_ID);
            if (TextUtils.isEmpty(reciverOpenId)) reciverOpenId = "";
            if (!UserInfoUtil.isLogin() || !reciverOpenId.equals(openId))
                return;
        }
        Intent appIntent = new Intent();// 万能跳转
        String actionClassName = getExtraValue(bundle, MessageConstants.KEY_CLASSNAME);
        LogUtil.i("TPush actionClassName " + actionClassName);
        appIntent.setAction(actionClassName);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String paramsString = PushUtils.getExtraValue(bundle, MessageConstants.KEY_PARAMS);
        Bundle bundle_params = getParamsBundle(paramsString);
        appIntent.putExtras(bundle_params);
        context.startActivity(appIntent);
    }

    /**
     * @param context
     * @param bundle
     */
    public static void processMessageArrived(Context context, Bundle bundle) {
        if (bundle == null) return;
        String className = getExtraValue(bundle, MessageConstants.KEY_CLASSNAME);
        if (TextUtils.isEmpty(className)) return;
        if (className.equals(MessageConstants.VALUE_SINGLE_SIGN_ON)) {
            UserInfoUtil.logout();// 单点登录
        }
    }

    /**
     * @param bundle
     * @param jsonStr
     * @return 获取推送bundle中的参数值
     */
    public static String getExtraValue(Bundle bundle, String jsonStr) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (TextUtils.isEmpty(extras)) return null;
        try {
            JSONObject object = new JSONObject(extras);
            return object.optString(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取万能跳转参数
     *
     * @param params
     * @return
     */
    public static Bundle getParamsBundle(String params) {
        LogUtil.i("getParamsBundle " + params);
        Bundle bundle = new Bundle();
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(params, type);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
            Log.v("TAG", entry.getKey() + "|" + entry.getValue());
        }
        return bundle;
    }

    /**
     * 判断应用是否是在后台
     */
    public static boolean isAppInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
