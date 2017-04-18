package com.nongyi.nylive;

import android.app.Application;
import android.os.Environment;

import com.jaronho.sdk.third.okhttpwrap.OkHttpUtil;
import com.jaronho.sdk.third.okhttpwrap.annotation.CacheLevel;
import com.jaronho.sdk.third.okhttpwrap.annotation.CacheType;
import com.jaronho.sdk.third.okhttpwrap.cookie.PersistentCookieJar;
import com.jaronho.sdk.third.okhttpwrap.cookie.cache.SetCookieCache;
import com.jaronho.sdk.third.okhttpwrap.cookie.persistence.SharedPrefsCookiePersistor;
import com.nongyi.nylive.Model.Global;
import com.nongyi.nylive.Model.NetHelper;
import com.tencent.ilivesdk.ILiveSDK;

public class BaseApplication extends Application {

    public static BaseApplication baseApplication;

    public static BaseApplication getApplication() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;

        NetHelper.init(getApplicationContext());
        // 初始化ILiveSDK
        ILiveSDK.getInstance().initSdk(getApplicationContext(), Global.APP_ID, Global.ACCOUNT_TYPE);
        // 初始化okhttp
        String downloadFileDir = Environment.getExternalStorageDirectory().getPath()+"/okhttp_download/";
        OkHttpUtil.init(this)
                .setConnectTimeout(30)//连接超时时间
                .setWriteTimeout(30)//写超时时间
                .setReadTimeout(30)//读超时时间
                .setMaxCacheSize(10 * 1024 * 1024)//缓存空间大小
                .setCacheLevel(CacheLevel.FIRST_LEVEL)//缓存等级
                .setCacheType(CacheType.FORCE_NETWORK)//缓存类型
                .setShowHttpLog(true)//显示请求日志
                .setShowLifecycleLog(false)//显示Activity销毁日志
                .setRetryOnConnectionFailure(false)//失败后不自动重连
                .setDownloadFileDir(downloadFileDir)//文件下载保存目录
                .addResultInterceptor(null)//请求结果拦截器
                .addExceptionInterceptor(null)//请求链路异常拦截器
                .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this)))//持久化cookie
                .build();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
