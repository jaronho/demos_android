package com.liyuu.strategy.di.module;

import com.liyuu.strategy.app.App;
import com.liyuu.strategy.http.HttpHelper;
import com.liyuu.strategy.http.RetrofitHelper;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.db.DBHelper;
import com.liyuu.strategy.model.db.RealmHelper;
import com.liyuu.strategy.prefs.ImplPreferencesHelper;
import com.liyuu.strategy.prefs.PreferencesHelper;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * AppModule
 * 单例创建的Module，都和Application关联起来。
 */

@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    App provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(RetrofitHelper retrofitHelper) {
        return retrofitHelper;
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(RealmHelper realmHelper) {
        return realmHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(ImplPreferencesHelper implPreferencesHelper) {
        return implPreferencesHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(HttpHelper httpHelper, DBHelper DBHelper, PreferencesHelper preferencesHelper) {
        return new DataManager(httpHelper, DBHelper, preferencesHelper);
    }
}
