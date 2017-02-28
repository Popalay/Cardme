package com.popalay.yocard.injection;

import android.content.Context;

import com.popalay.yocard.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context mApplicationContext;

    public AppModule(App app) {
        mApplicationContext = app;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplicationContext;
    }

}