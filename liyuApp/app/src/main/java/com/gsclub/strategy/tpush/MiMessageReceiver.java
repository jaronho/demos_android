package com.gsclub.strategy.tpush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/**
 * Created by 640 on 2018/3/2 0002.
 */

public class MiMessageReceiver extends PushMessageReceiver {
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage);
        Bundle bundle = getBundle(miPushMessage);
        if(bundle == null) return;
        PushUtils.processMessageArrived(context, bundle);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage);
        Bundle bundle = getBundle(miPushMessage);
        if(bundle == null) return;
        Intent intent = new Intent();
        intent.setAction(MyNotificationOpenedReceiver.ACTION_NOTIFICATION_CLICKED_RESULT_RESTART);
        intent.putExtra(MessageConstants.KEY_EXTRAS, bundle);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
    }

    private Bundle getBundle(MiPushMessage miPushMessage) {
        Bundle bundle = new Bundle();
        bundle.putString(JPushInterface.EXTRA_TITLE, miPushMessage.getTitle());
        bundle.putString(JPushInterface.EXTRA_MESSAGE, miPushMessage.getContent());
        bundle.putString(JPushInterface.EXTRA_EXTRA, miPushMessage.getExtra().toString());
        return bundle;
    }
}
