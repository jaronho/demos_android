package com.tencent.qcloud.xiaozhibo;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.common.utils.TCHttpEngine;
import com.tencent.qcloud.xiaozhibo.common.utils.TCLog;
import com.tencent.qcloud.xiaozhibo.im.TCIMInitMgr;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLivePusher;

import com.umeng.socialize.PlatformConfig;

import java.util.Locale;


/**
 * 小直播应用类，用于全局的操作，如
 * sdk初始化,全局提示框
 */
public class TCApplication extends MultiDexApplication {

//    private RefWatcher mRefWatcher;

    private static TCApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initSDK();

        //配置分享第三方平台的appkey
        PlatformConfig.setWeixin(TCConstants.WEIXIN_SHARE_ID, TCConstants.WEIXIN_SHARE_SECRECT);
        PlatformConfig.setSinaWeibo(TCConstants.SINA_WEIBO_SHARE_ID, TCConstants.SINA_WEIBO_SHARE_SECRECT, TCConstants.SINA_WEIBO_SHARE_REDIRECT_URL);
        PlatformConfig.setQQZone(TCConstants.QQZONE_SHARE_ID, TCConstants.QQZONE_SHARE_SECRECT);


//        mRefWatcher =
//        LeakCanary.install(this);
    }

    public static TCApplication getApplication() {
        return instance;
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        TCApplication application = (TCApplication) context.getApplicationContext();
//        return application.mRefWatcher;
//    }

    /**
     * 初始化SDK，包括Bugly，IMSDK，RTMPSDK等
     */
    public void initSDK() {


        //注册crash上报 bugly组件
        int[] sdkVer = TXLivePusher.getSDKVersion(); //这里调用TXLivePlayer.getSDKVersion()也是可以的
        if (sdkVer != null && sdkVer.length >= 4) {
//            if (sdkVer[0] != 0 || sdkVer[1] != 0) {
                //启动bugly组件，bugly组件为腾讯提供的用于crash上报和分析的开放组件，如果您不需要该组件，可以自行移除
                CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
                strategy.setAppVersion(String.format(Locale.US, "%d.%d.%d.%d",sdkVer[0],sdkVer[1],sdkVer[2],sdkVer[3]));
                CrashReport.initCrashReport(getApplicationContext(), TCConstants.BUGLY_APPID, true, strategy);
//            }
        }

        TCIMInitMgr.init(getApplicationContext());

        //设置rtmpsdk log回调，将log保存到文件
        TXLiveBase.getInstance().listener = new TCLog(getApplicationContext());

        //初始化httpengine
        TCHttpEngine.getInstance().initContext(getApplicationContext());

        Log.w("TCLog","app init sdk");
    }

}
