package com.liyuu.strategy.di.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.liyuu.strategy.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * FragmentModule
 */

@Module
public class FragmentModule {

    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return fragment.getActivity();
    }
}
