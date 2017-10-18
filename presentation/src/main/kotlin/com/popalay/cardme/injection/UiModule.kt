package com.popalay.cardme.injection

import android.arch.lifecycle.ViewModelProvider
import com.popalay.cardme.base.ViewModelFactory
import com.popalay.cardme.screens.addcard.AddCardActivity
import com.popalay.cardme.screens.addcard.AddCardModule
import com.popalay.cardme.screens.adddebt.AddDebtActivity
import com.popalay.cardme.screens.adddebt.AddDebtModule
import com.popalay.cardme.screens.carddetails.CardDetailsActivity
import com.popalay.cardme.screens.carddetails.CardDetailsModule
import com.popalay.cardme.screens.holderdetails.HolderDetailsActivity
import com.popalay.cardme.screens.holderdetails.HolderDetailsModule
import com.popalay.cardme.screens.home.HomeActivity
import com.popalay.cardme.screens.home.HomeModule
import com.popalay.cardme.screens.settings.SettingsActivity
import com.popalay.cardme.screens.settings.SettingsModule
import com.popalay.cardme.screens.splash.SplashActivity
import com.popalay.cardme.screens.trash.TrashActivity
import com.popalay.cardme.screens.trash.TrashModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UiModule {

    @Binds
    abstract fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun contributeSplashActivity(): SplashActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [AddCardModule::class])
    abstract fun contributeAddCardActivity(): AddCardActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeActivity(): HomeActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [HolderDetailsModule::class])
    abstract fun contributeHolderDetailsActivity(): HolderDetailsActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [SettingsModule::class])
    abstract fun contributeSettingsActivity(): SettingsActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [AddDebtModule::class])
    abstract fun contributeAddDebtActivity(): AddDebtActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [TrashModule::class])
    abstract fun contributeTrashActivity(): TrashActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [CardDetailsModule::class])
    abstract fun contributeCardDetailsActivity(): CardDetailsActivity

}