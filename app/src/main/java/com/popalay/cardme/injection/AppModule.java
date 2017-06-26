package com.popalay.cardme.injection;

import android.content.Context;

import com.popalay.cardme.App;
import com.popalay.cardme.data.repositories.card.CardRepository;
import com.popalay.cardme.data.repositories.card.DefaultCardRepository;
import com.popalay.cardme.data.repositories.debt.DebtRepository;
import com.popalay.cardme.data.repositories.debt.DefaultDebtRepository;
import com.popalay.cardme.data.repositories.device.DefaultDeviceRepository;
import com.popalay.cardme.data.repositories.device.DeviceRepository;
import com.popalay.cardme.data.repositories.holder.DefaultHolderRepository;
import com.popalay.cardme.data.repositories.holder.HolderRepository;
import com.popalay.cardme.data.repositories.settings.DefaultSettingsRepository;
import com.popalay.cardme.data.repositories.settings.SettingsRepository;
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
    CardRepository provideCardRepository() { return new DefaultCardRepository(); }

    @Provides @Singleton
    DebtRepository provideDebtRepository() { return new DefaultDebtRepository(); }

    @Provides @Singleton
    DeviceRepository provideDeviceRepository() { return new DefaultDeviceRepository(); }

    @Provides @Singleton
    HolderRepository provideHolderRepository() { return new DefaultHolderRepository(); }

    @Provides @Singleton
    SettingsRepository provideSettingsRepository(Context context) { return new DefaultSettingsRepository(context); }

    @Provides @Singleton
    NavigationExtrasHolder provideNavigationExtras() { return new NavigationExtrasHolder();}

    @Provides @Singleton
    CustomFactory provideViewModelFactory(NavigationExtrasHolder extras) { return new CustomFactory(app, extras); }
}