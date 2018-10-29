package com.liyuu.strategy.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.liyuu.strategy.BuildConfig;
import com.liyuu.strategy.R;
import com.liyuu.strategy.component.CrashHandler;
import com.liyuu.strategy.di.component.AppComponent;
import com.liyuu.strategy.di.component.DaggerAppComponent;
import com.liyuu.strategy.di.module.AppModule;
import com.liyuu.strategy.di.module.HttpModule;
import com.liyuu.strategy.di.module.OtherModule;
import com.liyuu.strategy.util.StorageUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.qiyukf.unicorn.api.Unicorn;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.HashSet;
import java.util.Set;

public class App extends Application {
    public static AppComponent appComponent;
    private static App instance;

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.bg_ui, R.color.text_grey_b3b3b3);//全局设置主题颜色
                return new ClassicsHeader(context).setPrimaryColor(getInstance().getResources().getColor(R.color.bg_ui));
                //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.bg_ui, R.color.text_grey_b3b3b3);//全局设置主题颜色
                //指定为经典Footer，默认是BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    private Set<Activity> allActivities;

    {
        PlatformConfig.setWeixin(AppConfig.WX_APP_KEY, AppConfig.WX_APP_SECRET);
    }

    public static synchronized App getInstance() {
        return instance;
    }

    /**
     * {@link com.liyuu.strategy.model.db.RealmHelper}  Realm数据库操作类
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        getAppComponent();//初始化dagger

        StorageUtil.initialize();

        //友盟初始化
        initUmeng();

        //七鱼初始化
//        initQiYu();

        //初始化日志
        Logger.addLogAdapter(new AndroidLogAdapter());

        //初始化错误收集
        CrashHandler.init(new CrashHandler(getApplicationContext()));

        //初始化内存泄漏检测
//        LeakCanary.install(App.getInstance());

        //初始化过度绘制检测
//        BlockCanary.install(getApplicationContext(), new AppBlockCanaryContext()).start();

        //初始化腾讯x5
        initX5();
    }

    private void initUmeng() {
        UMConfigure.init(this, AppConfig.UMENG_APP_KEY, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(BuildConfig.SHOW_LOG);
    }

    private void initX5() {
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), appComponent.getX5SdkCallback());
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public String getAppChannel() {
        return appComponent.getAppChannel();
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .otherModule(new OtherModule(instance))
                    .build();
        }
        return appComponent;
    }

    public void removeCookies() {
        getAppComponent().getCookiesManager().removeAll();
    }


    /**
     * {@link com.liyuu.strategy.di.module.OtherModule #provideUICustomization}  七鱼聊天界面ui修改
     */
    private void initQiYu() {
        // appKey 可以在七鱼管理系统->设置->APP接入 页面找到
        Unicorn.init(this, "af5ea05852bafa0a31189eb46cc8c39e", appComponent.getQiYuOptions(), appComponent.getQiYuGlideImageLoader());
    }

}