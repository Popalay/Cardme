package com.popalay.cardme.injection;

import android.content.Context;

import com.popalay.cardme.App;
import com.popalay.cardme.data.repositories.CardRepository;
import com.popalay.cardme.data.repositories.DebtRepository;
import com.popalay.cardme.data.repositories.DeviceRepository;
import com.popalay.cardme.data.repositories.HolderRepository;
import com.popalay.cardme.data.repositories.ICardRepository;
import com.popalay.cardme.data.repositories.IDebtRepository;
import com.popalay.cardme.data.repositories.IDeviceRepository;
import com.popalay.cardme.data.repositories.IHolderRepository;

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

    @Provides
    @Singleton
    ICardRepository provideCardRepository() { return new CardRepository(); }

    @Provides
    @Singleton
    IDebtRepository provideDebtRepository() { return new DebtRepository(); }

    @Provides
    @Singleton
    IDeviceRepository provideDeviceRepository() { return new DeviceRepository(); }

    @Provides
    @Singleton
    IHolderRepository provideHolderRepository() { return new HolderRepository(); }

}