package com.gsclub.strategy.di.component;

import com.gsclub.strategy.app.App;
import com.gsclub.strategy.component.QiYuGlideImageLoader;
import com.gsclub.strategy.di.cookie.CookiesManager;
import com.gsclub.strategy.di.module.AppModule;
import com.gsclub.strategy.di.module.HttpModule;
import com.gsclub.strategy.di.module.OtherModule;
import com.gsclub.strategy.http.RetrofitHelper;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.db.RealmHelper;
import com.gsclub.strategy.prefs.ImplPreferencesHelper;
import com.qiyukf.unicorn.api.YSFOptions;
import com.tencent.smtt.sdk.QbSdk;
import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Component;

/**
 * AppComponent
 * 该类中的对象可在 activity/fragment 中通过@Inject获取
 */

@Singleton
@Component(modules = {AppModule.class, HttpModule.class, OtherModule.class})
public interface AppComponent {

    App getContext();  // 提供App的Context

    DataManager getDataManager(); //数据中心

    RetrofitHelper retrofitHelper();  //提供http的帮助类

    RealmHelper realmHelper();    //提供数据库帮助类

    ImplPreferencesHelper preferencesHelper(); //提供sp帮助类

    CookiesManager getCookiesManager();

    YSFOptions getQiYuOptions();

    QiYuGlideImageLoader getQiYuGlideImageLoader();

    @Named("X5Callback")
    QbSdk.PreInitCallback getX5SdkCallback();

    @Named("appChannel")
    String getAppChannel();

}
