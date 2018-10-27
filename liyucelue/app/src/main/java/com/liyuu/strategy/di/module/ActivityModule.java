package com.liyuu.strategy.di.module;

import android.app.Activity;

import com.liyuu.strategy.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * ActivityModule
 */

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }
}
