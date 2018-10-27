package com.liyuu.strategy.tpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.util.PreferenceUtils;


/**
 * Created by 640 on 2018/1/29 0029.
 */

public class MyNotificationOpenedReceiver extends BroadcastReceiver {
    public static final String ACTION_NOTIFICATION_OPEN = "com.liyuu.strategy.android.intent.NOTIFICATION_OPENED";
    public static final String ACTION_NOTIFICATION_CLICKED_RESULT = "com.liyuu.strategy.android.intent.NOTIFICATION_CLICKED_RESULT";
    public static final String ACTION_NOTIFICATION_CLICKED_RESULT_RESTART = "com.liyuu.strategy.android.intent.NOTIFICATION_CLICKED_RESULT_RESTART";
    public static final String ACTION_SPLASH_STARTED = "com.liyuu.strategy.android.intent.SPLASH_STARTED";
    private static final String TAG = "JIGUANG-Example";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_NOTIFICATION_CLICKED_RESULT.equals(intent.getAction())) {// 魅族当应用在前台时不重启应用，走这里
            Bundle bundle = intent.getBundleExtra(MessageConstants.KEY_EXTRAS);
            PushUtils.handlePushNew(context, bundle);
        } else if (ACTION_NOTIFICATION_CLICKED_RESULT_RESTART.equals(intent.getAction())) {
            PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_IS_NOTIFICATION_CLICKED, true);
            Bundle bundle = intent.getBundleExtra(MessageConstants.KEY_EXTRAS);
            String jsonStr = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (TextUtils.isEmpty(jsonStr)) return;
            PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_PUSH_EXTRAS, jsonStr);
        } else if (ACTION_SPLASH_STARTED.equals(intent.getAction())) {
            String push_extras = PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.COMMON_PUSH_EXTRAS);
            if (TextUtils.isEmpty(push_extras)) return;
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_EXTRA, push_extras);
            PushUtils.handlePushNew(context, bundle);
        }
    }
}