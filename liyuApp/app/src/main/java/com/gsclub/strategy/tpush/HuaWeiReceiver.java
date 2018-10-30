package com.gsclub.strategy.tpush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;


import com.huawei.hms.support.api.push.PushReceiver;
import com.meizu.cloud.pushinternal.DebugLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 640 on 2018/3/15 0015.
 */

public class HuaWeiReceiver extends PushReceiver {
    private static final String TAG = "TPush";
    @Override
    public void onEvent(Context context, Event arg1, Bundle arg2) {
        super.onEvent(context, arg1, arg2);
        DebugLogger.i(TAG, "onEvent" + arg1 + " Bundle " + arg2);
        if(arg1.equals(Event.NOTIFICATION_OPENED)) {
            String extras = getExtras(arg2.getString(BOUND_KEY.pushMsgKey));
            DebugLogger.i(TAG, "onEvent extras:" + extras);
            if(TextUtils.isEmpty(extras)) return;
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_EXTRA, extras);
            PushUtils.processMessageArrived(context, bundle);
            Intent intent = new Intent();
            intent.setAction(MyNotificationOpenedReceiver.ACTION_NOTIFICATION_CLICKED_RESULT_RESTART);
            intent.putExtra(MessageConstants.KEY_EXTRAS, bundle);
            context.sendBroadcast(intent);
        }
    }

    private String getExtras(String pushMsg) {
        if(TextUtils.isEmpty(pushMsg)) return null;
        // 先去掉"[]"，花括号前多余的"，以及转义字符
        String extras = pushMsg.replace("[", "").replace("]", "").replace("\"{", "{").replace("}\"", "}").replace("\\", "");
        if(!extras.contains(",")) return extras;
        //去掉每个对象最外围的{}，再在最外面加上{}
        String[] extraArray = extras.split(",");
        List<String> extraList = new ArrayList<>();
        for (String item: extraArray) {
            String extra = item.substring(1, item.length()-1);
            extraList.add(extra);
        }
        int size = extraList.size();
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (int i=0;i<size;i++) {
            sb.append(extraList.get(i));
            if(i != size-1) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean onPushMsg(Context context, byte[] arg1, Bundle arg2) {
        DebugLogger.i(TAG, "onPushMsg" + new String(arg1) + " Bundle " + arg2);
        return super.onPushMsg(context, arg1, arg2);
    }

    @Override
    public void onPushMsg(Context context, byte[] arg1, String arg2) {
        DebugLogger.i(TAG, "onPushMsg" + new String(arg1) + " arg2 " + arg2);
        super.onPushMsg(context, arg1, arg2);
    }

    @Override
    public void onPushState(Context context, boolean arg1) {
        DebugLogger.i(TAG, "onPushState" + arg1);
        super.onPushState(context, arg1);
    }

    @Override
    public void onToken(Context context, String arg1, Bundle arg2) {
        super.onToken(context, arg1, arg2);
        DebugLogger.i(TAG, " onToken" + arg1 + "bundke " + arg2);
    }

    @Override
    public void onToken(Context context, String arg1) {
        super.onToken(context, arg1);
        DebugLogger.i(TAG, " onToken" + arg1);
    }

    public  void showToast(final String toast, final Context context)
    {

        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    private void  writeToFile(String conrent) {
        String SDPATH = Environment.getExternalStorageDirectory() + "/huawei.txt";
        try {
            FileWriter fileWriter = new FileWriter(SDPATH, true);

            fileWriter.write(conrent+"\r\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
