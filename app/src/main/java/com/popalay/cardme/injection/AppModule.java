package com.popalay.cardme.injection;

import android.content.Context;

import com.popalay.cardme.App;
import com.popalay.cardme.presentation.base.CustomFactory;
import com.popalay.cardme.presentation.base.NavigationExtrasHolder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton
    Context provideApplicationContext() {
        return app;
    }

    @Provides @Singleton
    NavigationExtrasHolder provideNavigationExtras() { return new NavigationExtrasHolder();}

    @Provides @Singleton
    CustomFactory provideViewModelFactory(NavigationExtrasHolder extras) { return new CustomFactory(app, extras); }
}