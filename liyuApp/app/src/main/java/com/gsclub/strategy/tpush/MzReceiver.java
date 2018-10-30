package com.gsclub.strategy.tpush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.meizu.cloud.pushinternal.DebugLogger;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

/**
 * Created by 640 on 2018/3/12 0012.
 */

public class MzReceiver extends MzPushMessageReceiver {
    private static final String TAG = "MzPushMessageReceiver";

    @Override
    @Deprecated
    public void onRegister(Context context, String s) {
        DebugLogger.i(TAG, "onRegister pushID " + s);
        print(context, "receive pushID " + s);
    }

    @Override
    public void onMessage(Context context, String s) {
        DebugLogger.i(TAG, "onMessage " + s);
    }

    @Override
    public void onMessage(Context context, Intent intent) {
        DebugLogger.i(TAG, "flyme3 onMessage ");
        String content = intent.getExtras().toString();
        print(context,"flyme3 onMessage " + content);
    }

    @Override
    @Deprecated
    public void onUnRegister(Context context, boolean b) {
        DebugLogger.i(TAG, "onUnRegister " + b);
        print(context,context.getPackageName() + " onUnRegister " + b);
    }

    @Override
    public void onPushStatus(Context context,PushSwitchStatus pushSwitchStatus) {
    }

    @Override
    public void onRegisterStatus(Context context,RegisterStatus registerStatus) {
        DebugLogger.i(TAG, "onRegisterStatus " + registerStatus+ " "+context.getPackageName());
    }

    @Override
    public void onUnRegisterStatus(Context context,UnRegisterStatus unRegisterStatus) {
        DebugLogger.i(TAG,"onUnRegisterStatus "+unRegisterStatus+" "+context.getPackageName());
    }

    @Override
    public void onSubTagsStatus(Context context,SubTagsStatus subTagsStatus) {
        DebugLogger.i(TAG, "onSubTagsStatus " + subTagsStatus+" "+context.getPackageName());
    }

    @Override
    public void onSubAliasStatus(Context context,SubAliasStatus subAliasStatus) {
        DebugLogger.i(TAG, "onSubAliasStatus " + subAliasStatus+" "+context.getPackageName());
    }

    @Override
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
//        pushNotificationBuilder.setmLargIcon(R.drawable.flyme_status_ic_notification);
//        pushNotificationBuilder.setmStatusbarIcon(R.drawable.mz_push_notification_small_icon);
    }

    @Override
    public void onNotificationArrived(Context context, String title, String content, String selfDefineContentString) {
        super.onNotificationArrived(context, title, content, selfDefineContentString);
        Bundle bundle = getBundle(title, content, selfDefineContentString);
        if(bundle == null) return;
        PushUtils.processMessageArrived(context, bundle);
    }

    @Override
    public void onNotificationClicked(Context context, String title, String content, String selfDefineContentString) {
        super.onNotificationClicked(context, title, content, selfDefineContentString);
        Bundle bundle = getBundle(title, content, selfDefineContentString);
        if(bundle == null) return;
        Intent intent = new Intent();
        if(!PushUtils.isAppInBackground(context)) {
            intent.setAction(MyNotificationOpenedReceiver.ACTION_NOTIFICATION_CLICKED_RESULT);
        }else {
            intent.setAction(MyNotificationOpenedReceiver.ACTION_NOTIFICATION_CLICKED_RESULT_RESTART);
        }
        intent.putExtra(MessageConstants.KEY_EXTRAS, bundle);
        context.sendBroadcast(intent);
    }

    @Override
    public void onNotificationDeleted(Context context, String title, String content, String selfDefineContentString) {
        super.onNotificationDeleted(context, title, content, selfDefineContentString);
    }

    @Override
    public void onNotifyMessageArrived(Context context, String message) {
        DebugLogger.i(TAG, "onNotifyMessageArrived messsage " + message);
    }

    private void print(final Context context, final String info){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Bundle getBundle(String title, String content, String selfDefineContentString) {
        Bundle bundle = new Bundle();
        bundle.putString(JPushInterface.EXTRA_TITLE, title);
        bundle.putString(JPushInterface.EXTRA_MESSAGE, content);
        bundle.putString(JPushInterface.EXTRA_EXTRA, selfDefineContentString);
        return bundle;
    }
}