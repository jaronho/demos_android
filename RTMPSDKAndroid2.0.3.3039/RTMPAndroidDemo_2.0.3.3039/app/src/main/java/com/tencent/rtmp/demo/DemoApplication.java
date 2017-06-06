package com.tencent.rtmp.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.rtmp.ITXLiveBaseListener;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePusher;

public class DemoApplication extends Application implements ITXLiveBaseListener{

//    private RefWatcher mRefWatcher;
    private static DemoApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        TXLiveBase.getInstance().listener = this;

        int[] sdkVer = TXLivePusher.getSDKVersion(); //这里调用TXLivePlayer.getSDKVersion()也是可以的
        if (sdkVer != null && sdkVer.length >= 4) {
//            if (sdkVer[0] != 0 || sdkVer[1] != 0) {
                //启动bugly组件，bugly组件为腾讯提供的用于crash上报和分析的开放组件，如果您不需要该组件，可以自行移除
                CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
                strategy.setAppVersion(String.format("%d.%d.%d.%d",sdkVer[0],sdkVer[1],sdkVer[2],sdkVer[3]));
                CrashReport.initCrashReport(getApplicationContext(),strategy);

                Log.d("rtmpsdk","init crash report");
//            }
        }

//        mRefWatcher = LeakCanary.install(this);
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        DemoApplication application = (DemoApplication) context.getApplicationContext();
//        return application.mRefWatcher;
//    }

    public static DemoApplication getApplication() {
        return instance;
    }

    @Override
    public void OnLog(int level, String module, String log) {
        switch (level)
        {
            case TXLiveConstants.LOG_LEVEL_ERROR:
                Log.e(module, log);
                break;
            case TXLiveConstants.LOG_LEVEL_WARN:
                Log.w(module, log);
                break;
            case TXLiveConstants.LOG_LEVEL_INFO:
                Log.i(module, log);
                break;
            case TXLiveConstants.LOG_LEVEL_DEBUG:
                Log.d(module, log);
                break;
            default:
                Log.d(module, log);
        }
    }
}
