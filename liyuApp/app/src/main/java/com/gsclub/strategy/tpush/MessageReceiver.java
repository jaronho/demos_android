package com.gsclub.strategy.tpush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gsclub.strategy.util.LogUtil;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by 640 on 2018/3/9 0009.
 */

public class MessageReceiver extends XGPushBaseReceiver {
    private static final String TAG = "TPush";

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    // 消息透传的回调
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        String text = "收到消息:" + message.toString();
        LogUtil.i(TAG + " ++++++++++++++++透传消息");
        // APP自主处理消息的过程...
        LogUtil.i(TAG + text);
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        LogUtil.i(TAG + " 点击了通知：" + xgPushClickedResult.getCustomContent());
        LogUtil.i(TAG + " 通知详情：" + xgPushClickedResult.toString());
        Bundle bundle = getBundle(xgPushClickedResult);
        if (bundle == null) return;
        Intent intent = new Intent();
        intent.setAction(MyNotificationOpenedReceiver.ACTION_NOTIFICATION_CLICKED_RESULT_RESTART);
        intent.putExtra(MessageConstants.KEY_EXTRAS, bundle);
        context.sendBroadcast(intent);
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        if (context == null || xgPushShowedResult == null) {
            return;
        }
        LogUtil.i(TAG + " 您有1条新消息, " + "通知被展示 ， " + xgPushShowedResult.toString());
        Bundle bundle = getBundle(xgPushShowedResult);
        if (bundle == null) return;
        PushUtils.processMessageArrived(context, bundle);
    }

    private Bundle getBundle(XGPushClickedResult result) {
        if (result == null) return null;
        Bundle bundle = new Bundle();
        bundle.putString(JPushInterface.EXTRA_TITLE, result.getTitle());
        bundle.putString(JPushInterface.EXTRA_MESSAGE, result.getContent());
        bundle.putString(JPushInterface.EXTRA_EXTRA, result.getCustomContent());

        LogUtil.i(TAG + " click => title: " + result.getTitle() + " content " + result.getContent() + " customcontent " + result.getCustomContent());
        return bundle;
    }

    private Bundle getBundle(XGPushShowedResult result) {
        if (result == null) return null;
        Bundle bundle = new Bundle();
        bundle.putString(JPushInterface.EXTRA_TITLE, result.getTitle());
        bundle.putString(JPushInterface.EXTRA_MESSAGE, result.getContent());
        bundle.putString(JPushInterface.EXTRA_EXTRA, result.getCustomContent());

        LogUtil.i(TAG + " reciver => title: " + result.getTitle() + " content " + result.getContent() + " customcontent " + result.getCustomContent());
        return bundle;
    }

    //send msg to MainActivity
//    private void processCustomNotification(Context context, Bundle bundle, long msgId) {
//        if(bundle == null) return;
//        String className = PushUtils.getExtraValue(bundle, "classname");
//        if(TextUtils.isEmpty(className)) return;
//        if(className.equals("SingleSignOn")) {
////            if(!PushUtils.isAppInBackground(context)) {// 把已显示的通知清除掉，但同时会把所有通知栏里的通知都清除，汗
////                NotificationManager notificationManager = (NotificationManager) context
////                        .getSystemService(Context.NOTIFICATION_SERVICE);
////                notificationManager.cancelAll();
////                notificationManager.cancel((int) msgId);
////                DialogActivity.start(DialogActivity.DIALOG_EXIT, bundle.getString(JPushInterface.EXTRA_MESSAGE));
////            }
//            AppUtils.doClearUserInfo(context);// 单点登录
//        }
//    }

}
