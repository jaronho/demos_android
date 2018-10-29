package com.liyuu.strategy.di.module;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;

import com.liyuu.strategy.R;
import com.liyuu.strategy.component.QiYuGlideImageLoader;
import com.orhanobut.logger.Logger;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.YSFOptions;
import com.tencent.smtt.sdk.QbSdk;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class OtherModule {
    private final Context applicationContext;

    public OtherModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return applicationContext;
    }

    /**
     * @return 七鱼界面配置
     * @see <a href="https://qiyukf.com/newdoc/html/Android_SDK_Guide.html#%E6%B6%88%E6%81%AF">七鱼开发指南</a>
     */
    @Singleton
    @Provides
    @Named("UICustomization")
    UICustomization provideUICustomization(Context applicationContext) {
        UICustomization customization = new UICustomization();
        //标题栏风格，影响标题和标题栏上按钮的颜色 目前支持： 0浅色系，1深色系
        customization.titleBarStyle = 2;
//        customization.titleBackgroundResId = R.drawable.my_ysf_title_bar_bg;//标题栏背景图

        //顶部提示栏(没有客服，排队状态等)背景色
        customization.topTipBarBackgroundColor = 0xFFDCF2F5;
        //顶部提示栏文字颜色
        customization.topTipBarTextColor = 0xFF4E97D9;

        // Glide
        //客服消息窗口背景图片设置
//        customization.msgBackgroundUri = "file:///android_asset/msg_bg.png";
        //左侧 （客服消息）头像图片 uri
        customization.leftAvatar = "android.resource://" + applicationContext.getPackageName() + "/" + R.mipmap.icon_robot_header;
        //右侧 （访客消息）头像图片 uri
        customization.rightAvatar = "android.resource://" + applicationContext.getPackageName() + "/" + R.mipmap.icon_default_header;
        //对话框文字颜色
        customization.textMsgColorLeft = Color.parseColor("#333333");
        customization.textMsgColorRight = Color.parseColor("#373e56");
        //标题剧中
        customization.titleCenter = true;
        //0 为仅竖屏显示，1 为仅横屏显示，2 为根据重力感应切换
        customization.screenOrientation = 0;
        //聊天界面背景颜色
        customization.msgBackgroundColor = Color.parseColor("#f4f4f4");
        return customization;
    }

    /**
     * 提供网易七鱼的配置
     */
    @Singleton
    @Provides
    YSFOptions provideQiYuOptions(@Named("UICustomization") UICustomization uiCustomization) {
        // 如果返回值为null，则全部使用默认参数。
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.uiCustomization = uiCustomization;
        return options;
    }

    @Singleton
    @Provides
    QiYuGlideImageLoader provideQiYuGlideImageLoader(Context applicationContext) {
        return new QiYuGlideImageLoader(applicationContext);
    }


    @Singleton
    @Provides
    @Named("X5Callback")
    QbSdk.PreInitCallback provideX5SdkCallback() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        return new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.w("app onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
    }

    /**
     * 获取当前app的渠道
     */
    @Singleton
    @Provides
    @Named("appChannel")
    String provideAppChannel(Context applicationContext) {
        try {
            ApplicationInfo info = applicationContext.getPackageManager()
                    .getApplicationInfo(applicationContext.getPackageName(),
                            PackageManager.GET_META_DATA);
            return info.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "error_channel";
    }

}
